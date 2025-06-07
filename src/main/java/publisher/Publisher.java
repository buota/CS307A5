/**
 * Logan Lumetta 5/28
 * Publisher class for MQTT
 */

package publisher;

import org.eclipse.paho.client.mqttv3.*;

public class Publisher implements Runnable, RepositoryObserver {
    private MqttClient client;
    private final String broker = "tcp://broker.hivemq.com:1883";
    private final String topic = "software/360";
    private final String clientId = "ASU-publisher-" + System.currentTimeMillis();

    @Override
    public void run() {
        try {
            client = new MqttClient(broker, clientId);
            client.connect();
            System.out.println("Connected to broker: " + broker);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        Repository.getInstance().addObserver(this);
    }

    @Override
    public void update() {
        if (client != null && client.isConnected()) {
            Repository repo = Repository.getInstance();
            String sessionId = repo.getSessionID();
            int[] votes = repo.getVotes();
            int currentStory = repo.getCurrentStoryIndex();

            StringBuilder voteString = new StringBuilder();
            for (int vote : votes) {
                voteString.append(vote).append(",");
            }
            if (voteString.length() > 0) {
                voteString.setLength(voteString.length() - 1);
            }

            String message = "Session:" + sessionId +
                    ",Story#" + (currentStory + 1) +
                    ",Votes:[" + voteString + "]";

            try {
                client.publish(topic, new MqttMessage(message.getBytes()));
                System.out.println("Published: " + message);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Client not connected. Cannot publish.");
        }
    }
}
