/**
 * Logan Lumetta 5/28
 * Screen to enter stories
 */

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StoryEntryScreen extends JFrame {
    private JTextField storyInputField;
    private DefaultListModel<String> storyListModel;
    private JList<String> storyListDisplay;
    private ArrayList<String> storyList;
    private Map<String, String> participantNames; // Replace with actual participant map

    public StoryEntryScreen(String sessionId, String currentUserName) {
        super("Enter Stories - PlanItPoker");

        storyList = new ArrayList<>();
        participantNames = new HashMap<>();
        participantNames.put("current_user", currentUserName); // Add more as needed

        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Enter Story Titles for Voting");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // Center panel for input and story list
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        // Input field + Add button
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        storyInputField = new JTextField(20);
        JButton addButton = new JButton("Add Story");

        addButton.addActionListener(e -> {
            String story = storyInputField.getText().trim();
            if (!story.isEmpty()) {
                storyListModel.addElement(story);
                storyList.add(story);
                storyInputField.setText("");
            }
        });

        inputPanel.add(new JLabel("Story Title:"));
        inputPanel.add(storyInputField);
        inputPanel.add(addButton);

        // Story list display
        storyListModel = new DefaultListModel<>();
        storyListDisplay = new JList<>(storyListModel);
        JScrollPane listScrollPane = new JScrollPane(storyListDisplay);

        centerPanel.add(inputPanel, BorderLayout.NORTH);
        centerPanel.add(listScrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Done button to start voting
        JButton doneButton = new JButton("Done");
        doneButton.addActionListener(e -> {
            if (storyList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please add at least one story.", "No Stories", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Launch voting phase
            Voting voting = new Voting(storyList);
            for (String participantId : participantNames.keySet()) {
                voting.registerParticipant(participantId, participantNames.get(participantId));
                voting.showVotingPopup(participantId);
            }

            // Close this window
            dispose();
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(doneButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Window settings
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
