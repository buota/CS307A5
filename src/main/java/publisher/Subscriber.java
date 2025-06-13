package publisher;

import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONObject;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Subscriber implements Runnable {
    private final String broker = "tcp://broker.hivemq.com:1883";
    private final String topic = "software/360";
    private final String clientId = "ASU-subscriber-" + System.currentTimeMillis();

    private MqttClient client;

    @Override
    public void run() {
        try {
            client = new MqttClient(broker, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            client.connect(options);

            client.subscribe(topic, (receivedTopic, message) -> {
                String payload = new String(message.getPayload());
                System.out.println("Received message: " + payload);

                JSONObject json = new JSONObject(payload);
                if (json.getString("type").equals("start_voting")) {
                    String sessionId = json.getString("sessionId");
                    int index = json.getInt("storyIndex");
                    String story = json.getString("story");
                    String stories = json.getString("stories");

                    Repository repo = Repository.getInstance();
                    if (repo.getSessionID().equals(sessionId)) {
                        repo.setCurrentStoryIndex(index);
                        repo.setStories(List.of(stories.split(",")));
                        SwingUtilities.invokeLater(() -> {
                            Voting voting = new Voting((ArrayList<String>) repo.getStories());
                            voting.showVotingPopup(repo.getName());
                        });
                    }
                }
            });

            System.out.println("Subscribed to topic: " + topic);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
