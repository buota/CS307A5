/**
 * Logan Lumetta 5/28
 * Subsciber class for MQTT
 */

package publisher;

import org.eclipse.paho.client.mqttv3.*;

public class Subscriber implements Runnable, MqttCallback {

    private MqttClient client;
    private final String broker = "tcp://broker.hivemq.com:1883";
    private final String topic = "software/360";
    private final String clientId = "ASU-subscriber-" + System.currentTimeMillis();

    @Override
    public void run() {
        try {
            client = new MqttClient(broker, clientId);
            client.setCallback(this);
            client.connect();
            client.subscribe(topic);
            System.out.println("Subscribed to topic: " + topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection lost! " + throwable.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String payload = new String(message.getPayload());
        System.out.println("Received message: " + payload);

        try {
            if (payload.contains("Session:")) {
                String[] parts = payload.split(",");
                String sessionId = parts[0].split(":")[1].trim();
                int storyIndex = Integer.parseInt(parts[1].split("#")[1].trim()) - 1;

                String votePart = parts[2].split(":")[1].trim();
                votePart = votePart.substring(1, votePart.length() - 1);

                String[] voteStrings = votePart.split(",");

                int[] votes = new int[voteStrings.length];
                for (int i = 0; i < voteStrings.length; i++) {
                    votes[i] = Integer.parseInt(voteStrings[i].trim());
                }

                Repository repo = Repository.getInstance();
                repo.setSessionID(sessionId);
                repo.setCurrentStoryIndex(storyIndex);
                repo.setVotes(votes);
            }
        } catch (Exception e) {
            System.out.println("Failed to parse message: " + e.getMessage());
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
