package publisher;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Voting {
    private JFrame voteFrame;
    private ArrayList<String> stories;
    private int currentStoryIndex;

    private Map<String, String> participantNames;
    private Set<String> participantIds;

    private Map<String, Map<String, Integer>> allVotingResults;
    private Map<String, Float> storyAverages;
    private Map<String, Integer> currentStoryVotes;

    private FindAverage averageCalculator;

    public Voting(ArrayList<String> stories) {
        this.stories = stories;
        this.participantNames = new HashMap<>();
        this.participantIds = new HashSet<>();
        this.allVotingResults = new HashMap<>();
        this.storyAverages = new HashMap<>();
        this.currentStoryIndex = 0;
        this.averageCalculator = new FindAverage();
    }

    public void registerParticipant(String participantId, String name) {
        participantNames.put(participantId, name);
        participantIds.add(participantId);
    }

    public void showVotingPopup(String participantId) {
        if (!participantNames.containsKey(participantId)) {
            System.out.println("Participant not registered!");
            return;
        }

        if (currentStoryIndex >= stories.size()) {
            System.out.println("No more stories to vote on.");
            return;
        }

        String storyTitle = stories.get(currentStoryIndex);
        currentStoryVotes = allVotingResults.getOrDefault(storyTitle, new HashMap<>());

        voteFrame = new JFrame("Vote - " + participantNames.get(participantId));
        voteFrame.setSize(400, 300);
        voteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Vote for: " + storyTitle, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel votingPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        votingPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        String[] voteOptions = {"0", "1", "2", "3", "5", "8", "13", "?"};

        for (final String vote : voteOptions) {
            JButton voteButton = new JButton(vote);
            voteButton.addActionListener(e -> {
                int voteValue;
                try {
                    voteValue = Integer.parseInt(vote);
                } catch (NumberFormatException ex) {
                    voteValue = -1;
                }

                currentStoryVotes.put(participantId, voteValue);
                allVotingResults.put(storyTitle, new HashMap<>(currentStoryVotes));
                voteFrame.dispose();

                System.out.println(participantNames.get(participantId) + " voted " + (voteValue == -1 ? "?" : voteValue) + " on \"" + storyTitle + "\"");

                if (currentStoryVotes.size() == participantIds.size()) {
                    calculateAndStoreAverage(storyTitle);
                    nextStory();
                }
            });
            votingPanel.add(voteButton);
        }

        mainPanel.add(votingPanel, BorderLayout.CENTER);
        voteFrame.add(mainPanel);
        voteFrame.setLocationRelativeTo(null);
        voteFrame.setVisible(true);
    }

    private void calculateAndStoreAverage(String storyTitle) {
        Map<String, Integer> votes = allVotingResults.get(storyTitle);
        List<Integer> validVotes = new ArrayList<>();

        for (int vote : votes.values()) {
            if (vote != -1) {
                validVotes.add(vote);
            }
        }

        float average = 0;
        if (!validVotes.isEmpty()) {
            int[] voteArray = validVotes.stream().mapToInt(Integer::intValue).toArray();
            average = averageCalculator.findAverage(voteArray);
        }

        storyAverages.put(storyTitle, average);
        System.out.println("Average for \"" + storyTitle + "\": " + average);
    }

    private void nextStory() {
        currentStoryIndex++;
        if (currentStoryIndex < stories.size()) {
            for (String participantId : participantIds) {
                showVotingPopup(participantId);
            }
        } else {
            displayFinalResults();
        }
    }

    private void displayFinalResults() {
        JTabbedPane finalTabs = new JTabbedPane();


        JFrame resultsFrame = new JFrame("Final Voting Results");
        resultsFrame.setSize(400, 500);
        resultsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        resultsFrame.setLayout(new BorderLayout());

        JTextArea resultsArea = new JTextArea();
        resultsArea.setEditable(false);

        for (String story : stories) {
            resultsArea.append("Story: " + story + "\n");
            Map<String, Integer> votes = allVotingResults.get(story);
            for (String participantId : votes.keySet()) {
                int vote = votes.get(participantId);
                resultsArea.append("  " + participantNames.get(participantId) + ": " + (vote == -1 ? "?" : vote) + "\n");
            }
            Float avg = storyAverages.get(story);
            resultsArea.append("  → Average: " + (avg != null ? String.format("%.2f", avg) : "N/A") + "\n\n");
        }
        JScrollPane scrollPane = new JScrollPane(resultsArea);


        ChartPanel chartPane = new ChartPanel(stories,allVotingResults);
        JScrollPane scrollPane1 = new JScrollPane(chartPane);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton exportButton = new JButton("Export Results");
        ExportVotingResultsNanny buttonListener = new ExportVotingResultsNanny();
        exportButton.addActionListener(e-> {
            buttonListener.export(stories,allVotingResults,participantNames,storyAverages);
        });
        buttonPanel.add(exportButton);


        finalTabs.addTab("Results",scrollPane);
        finalTabs.addTab("Plots",scrollPane1);

        resultsFrame.add(finalTabs,BorderLayout.CENTER);
        resultsFrame.add(buttonPanel,BorderLayout.SOUTH);
        resultsFrame.setLocationRelativeTo(null);
        resultsFrame.setVisible(true);
    }

    public Map<String, Map<String, Integer>> getAllVotingResults() {
        return allVotingResults;
    }

    public Map<String, Float> getStoryAverages() {
        return storyAverages;
    }


    public void setStories(List<String> stories) {
        this.stories = (ArrayList<String>) stories;
    }
}
