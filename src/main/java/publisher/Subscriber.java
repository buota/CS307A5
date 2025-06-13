package publisher;

import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONObject;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Subscriber implements Runnable {

    String sessionId = null;
    List<String> stories = new ArrayList<>();
    List<Integer> votes = new ArrayList<>();
    private final String broker = "tcp://broker.hivemq.com:1883";
    private final String topic = "software/360";
    private final String clientId = "ASU-subscriber-" + System.currentTimeMillis();

    private MqttClient client;

    @Override
    public void run() {
        try {
            System.out.println("RECIEVED\n\n\n\n");
            client = new MqttClient(broker, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            client.connect(options);

            client.subscribe(topic, (receivedTopic, message) -> {
                String payload = new String(message.getPayload());
                System.out.println("Received message: " + payload);


                String[] parts = payload.split(",(?=Story#|Votes:|Stories:)");

                for (String part : parts) {
                    part = part.trim();

                    if (part.startsWith("Session:")) {
                        sessionId = part.substring("Session:".length()).trim();
                    } else if (part.startsWith("Votes:[")) {
                        String voteSection = part.substring("Votes:[".length(), part.length() - 1); // Remove surrounding brackets
                        if (!voteSection.trim().isEmpty()) {
                            for (String voteStr : voteSection.split(",")) {
                                votes.add(Integer.parseInt(voteStr.trim()));
                            }
                        }
                    } else if (part.startsWith("Stories: ")) {
                        String storySection = part.substring("Stories: ".length(), part.length() - 1); // Remove surrounding brackets
                        if (!storySection.trim().isEmpty()) {
                            for (String story : storySection.split(":")) {
                                stories.add(story.trim());
                            }
                        }
                    }
                }
                    System.out.println("ID" + sessionId);
                    System.out.println("STORIES");
                    System.out.println(stories);
                    Repository repo = Repository.getInstance();
                    //if (repo.getSessionID().equals(sessionId)) {
                        repo.setStories(stories);
                        SwingUtilities.invokeLater(() -> {
                            Voting voting = new Voting((ArrayList<String>) repo.getStories());
                            voting.showVotingPopup(repo.getName());
                        });
                    //}

            });

            System.out.println("Subscribed to topic: " + topic);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}