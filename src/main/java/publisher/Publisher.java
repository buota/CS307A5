package publisher;

import org.eclipse.paho.client.mqttv3.*;

public class Publisher implements Runnable, RepositoryObserver {
    private static Publisher instance;

    private MqttClient client;
    private final String broker = "tcp://broker.hivemq.com:1883";
    private final String topic = "software/360";
    private final String clientId = "ASU-publisher-" + System.currentTimeMillis();

    private Publisher() {
        // private constructor to enforce singleton
    }

    public static synchronized Publisher getInstance() {
        if (instance == null) {
            instance = new Publisher();
        }
        return instance;
    }

    @Override
    public void run() {
        try {
            client = new MqttClient(broker, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            client.connect(options);
            System.out.println("Connected to broker: " + broker);

            Repository.getInstance().addObserver(this);

        } catch (MqttException e) {
            e.printStackTrace();
        }
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

            publish(topic, message);
        } else {
            System.out.println("Client not connected. Cannot publish.");
        }
    }

    // ✅ Public publish method
    public void publish(String topic, String message) {
        try {
            if (client != null && client.isConnected()) {
                client.publish(topic, new MqttMessage(message.getBytes()));
                System.out.println("Published: " + message);
            } else {
                System.out.println("MQTT client not connected.");
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // Optional overload for default topic
    public void publish(String message) {
        publish(this.topic, message);
    }
}
