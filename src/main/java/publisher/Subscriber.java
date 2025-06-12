package publisher;

import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.util.ArrayList;

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
                String type = json.getString("type");
                String sessionId = json.getString("sessionId");

                Repository repo = Repository.getInstance();
                if (!repo.getSessionID().equals(sessionId)) {
                    return; // Ignore messages from other sessions
                }

                switch (type) {
                    case "start_voting":
                        handleStartVoting(json);
                        break;
                    case "stories_sync":
                        handleStoriesSync(json);
                        break;
                    case "request_stories":
                        handleStoriesRequest(json);
                        break;
                }
            });

            System.out.println("Subscribed to topic: " + topic);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void handleStartVoting(JSONObject json) {
        Repository repo = Repository.getInstance();
        int index = json.getInt("storyIndex");
        repo.setCurrentStoryIndex(index);
        SwingUtilities.invokeLater(() -> {
            Voting voting = new Voting((ArrayList<String>) repo.getStories());
            voting.showVotingPopup(repo.getName());
        });
    }

    private void handleStoriesSync(JSONObject json) {
        Repository repo = Repository.getInstance();
        JSONArray stories = json.getJSONArray("stories");
        repo.setFetchedStories(stories);

        // Refresh the story entry screen if it's open
        SwingUtilities.invokeLater(() -> {
            new StoryEntryScreen(repo.getSessionID(), repo.getName());
        });
    }

    private void handleStoriesRequest(JSONObject json) {
        // Only the session creator should respond
        Repository repo = Repository.getInstance();
        JSONArray stories = repo.getFetchedStories();
        if (stories != null) {
            JSONObject response = new JSONObject();
            response.put("type", "stories_sync");
            response.put("sessionId", repo.getSessionID());
            response.put("stories", stories);

            Publisher.getInstance().publish("software/360", response.toString());
        }
    }
}
