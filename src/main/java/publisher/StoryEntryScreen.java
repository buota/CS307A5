package publisher;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoryEntryScreen extends JFrame {
    private DefaultListModel<String> storyListModel;
    private JList<String> storyListDisplay;
    private Map<String, String> participantNames;
    private List<String> storyList;
    private String currentUserName;


    public StoryEntryScreen(String sessionId, String currentUserName) {
        super("Story Entry - PlanItPoker");
        Repository repo = Repository.getInstance();
        this.currentUserName = currentUserName;
        JSONArray storiesArray = repo.getFetchedStories();
        storyList = new ArrayList<>();

        if (storiesArray != null) {
            for (int i = 0; i < storiesArray.length(); i++) {
                JSONObject story = storiesArray.getJSONObject(i);
                String subject = story.optString("subject", "(no title)");
                storyList.add(subject);
            }
        }

        if (storyList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No stories found. Please log in again.");
            dispose();
            return;
        }

        participantNames = repo.getInstance().getParticipants();


        // GUI setup
        setLayout(new BorderLayout());
        JLabel title = new JLabel("Fetched Stories from Taiga");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        storyListModel = new DefaultListModel<>();
        for (String story : storyList) {
            storyListModel.addElement(story);
        }

        storyListDisplay = new JList<>(storyListModel);
        JScrollPane scrollPane = new JScrollPane(storyListDisplay);

        JButton startVotingBtn = new JButton("Start Voting");
        startVotingBtn.addActionListener((ActionEvent e) -> startVoting());

        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(startVotingBtn, BorderLayout.SOUTH);

        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    private void startVoting() {
        if (storyList == null || storyList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add at least one story.", "No Stories", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Repository repo = Repository.getInstance();
        String sessionId = repo.getSessionID();
        repo.addParticipant(currentUserName, currentUserName); // id = name for creator

        Map<String, String> allParticipants = repo.getAllParticipants();
        if (allParticipants.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No participants joined yet.", "Waiting for Participants", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Voting voting = new Voting(new ArrayList<>(storyList));
        voting.setStories(storyList);

        for (String participantId : allParticipants.keySet()) {
            voting.registerParticipant(participantId, allParticipants.get(participantId));
            voting.showVotingPopup(participantId);
        }

        dispose();
    }


}
