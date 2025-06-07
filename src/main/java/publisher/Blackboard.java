package publisher;

import java.util.LinkedList;
import java.util.List;

public class Blackboard {
    private static LinkedList<String> names = new LinkedList();
    private static LinkedList<Story> stories = new LinkedList();
    private static String currentRoom;
    private static String mode;
    public static Story currentStory;

    public Blackboard() {
    }

    public void addName(String name) {
        names.add(name);
    }

    public LinkedList<String> getNames() {
        return names;
    }

    public void addStory(String title, int numberOfVotes, List<Integer> votes, double average) {
        stories.add(new Story(title, numberOfVotes, votes, average));
    }

    public LinkedList<Story> getStories() {
        return stories;
    }

    public void addCurrentRoom(String name) {
        currentRoom = name;
    }

    public String getCurrentRoom() {
        return currentRoom;
    }

    public void addCurrentMode(String modeType) {
        mode = modeType;
    }

    public String getMode() {
        return mode;
    }

    public void setCurrentStory(Story current) {
        currentStory = current;
    }

    public Story getCurrentStory() {
        return currentStory;
    }

    public static class Story {
        private String title;
        private int numberOfVotes;
        private List<Integer> votes;
        private double average;

        public Story(String title, int numberOfVotes, List<Integer> votes, double average) {
            this.title = title;
            this.numberOfVotes = numberOfVotes;
            this.votes = votes;
            this.average = average;
        }

        public double getAverage() {
            return this.average;
        }

        public int getNumberOfVotes() {
            return this.numberOfVotes;
        }

        public List<Integer> getVotes() {
            return this.votes;
        }

        public String getTitle() {
            return this.title;
        }
    }
}

