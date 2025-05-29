import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Voting{
    private JFrame voteFrame;
    private Map<String, Integer> votingResults; // Maps participant ID to their vote
    private Map<String, String> participantNames; // Maps participant ID to their name
    private boolean voteComplete = false;

    public Voting() {
        votingResults = new HashMap<>();
        participantNames = new HashMap<>();
    }

    // Method to register a participant
    public void registerParticipant(String participantId, String name) {
        participantNames.put(participantId, name);
    }

    // Method to show voting popup for a participant
    public void showVotingPopup(String participantId) {
        if (!participantNames.containsKey(participantId)) {
            System.out.println("Participant not registered!");
            return;
        }

        voteComplete = false;

        voteFrame = new JFrame("Vote - " + participantNames.get(participantId));
        voteFrame.setSize(400, 300);
        voteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Select your story point estimate", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Create voting options panel
        JPanel votingPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        votingPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Common voting values in PlanIt Poker
        String[] voteOptions = {"0", "1", "2", "3", "5", "8", "13", "?"};

        for (final String vote : voteOptions) {
            JButton voteButton = new JButton(vote);
            voteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Register the vote for this participant
                    int voteValue;
                    try {
                        voteValue = Integer.parseInt(vote);
                    } catch (NumberFormatException ex) {
                        // Handle the "?" case
                        voteValue = -1;
                    }

                    registerVote(participantId, voteValue);
                    voteComplete = true;
                    voteFrame.dispose();
                }
            });
            votingPanel.add(voteButton);
        }

        mainPanel.add(votingPanel, BorderLayout.CENTER);

        voteFrame.add(mainPanel);
        voteFrame.setLocationRelativeTo(null);
        voteFrame.setVisible(true);
    }

    // Method to register a vote
    private void registerVote(String participantId, int vote) {
        votingResults.put(participantId, vote);
        System.out.println("Vote registered: " + participantNames.get(participantId) + " voted " + (vote == -1 ? "?" : vote));
    }

    // Method to get all voting results
    public Map<String, Integer> getVotingResults() {
        return new HashMap<>(votingResults);
    }

    // Method to clear votes for a new round
    public void clearVotes() {
        votingResults.clear();
        System.out.println("All votes cleared.");
    }

    // Method to check if vote is complete
    public boolean isVoteComplete() {
        return voteComplete;
    }

    // Method to wait for vote completion
    public void waitForVoteCompletion() {
        while (!isVoteComplete()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}