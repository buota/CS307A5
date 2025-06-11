package publisher;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repository {
    private static Repository instance;

    private String sessionID;
    private int[] votes = new int[0];
    private int currentStoryIndex = 0;

    // Store fetched stories (e.g., from Taiga)
    private List<String> stories;

    // Map of participant IDs to their display names
    private final Map<String, String> participants = new HashMap<>();

    // Map for votes: key=story index, value = (map of participant id -> vote)
    private final Map<Integer, Map<String, Integer>> voteMap = new HashMap<>();

    private final List<RepositoryObserver> observers = new ArrayList<>();

    // Additional fields (mode, name, etc.)
    private String mode;
    private String name;

    // Fields for Taiga credentials if needed
    private String taigaUsername;
    private String taigaPassword;
    private String projectSlug;

    private Repository() { }

    public static synchronized Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    // Getters and setters...
    public synchronized void setSessionID(String sessionID) {
        this.sessionID = sessionID;
        notifyObservers();
    }

    public synchronized String getSessionID() {
        return sessionID;
    }

    public synchronized void setStories(List<String> stories) {
        this.stories = new ArrayList<>(stories);
        notifyObservers();
    }

    public synchronized List<String> getStories() {
        return new ArrayList<>(stories);
    }

    public synchronized void addParticipant(String participantId, String name) {
        participants.put(participantId, name);
        notifyObservers();
    }

    public synchronized String getParticipantName(String participantId) {
        return participants.get(participantId);
    }

    public synchronized int[] getVotes() {
        return votes != null ? votes.clone() : new int[0];
    }

    public synchronized void setVotes(int[] votes) {
        this.votes = votes;
    }

    public synchronized int getCurrentStoryIndex() {
        return currentStoryIndex;
    }

    public synchronized void setCurrentStoryIndex(int index) {
        this.currentStoryIndex = index;
    }


    public synchronized Map<String, String> getParticipants() {
        return new HashMap<>(participants);
    }

    // Submit a vote for a particular story (by index) from a participant.
    public synchronized void submitVoteForParticipant(String participantId, int storyIndex, int vote) {
        Map<String, Integer> votesForStory = voteMap.getOrDefault(storyIndex, new HashMap<>());
        votesForStory.put(participantId, vote);
        voteMap.put(storyIndex, votesForStory);
        notifyObservers();
    }

    public synchronized Map<Integer, Map<String, Integer>> getVoteMap() {
        return new HashMap<>(voteMap);
    }

    // Observer support:
    public synchronized void addObserver(RepositoryObserver observer) {
        observers.add(observer);
    }

    private synchronized void notifyObservers() {
        for (RepositoryObserver obs : observers) {
            obs.update();
        }
    }

    // ... other getters and setters for mode, name, etc.

    public synchronized void setName(String name) {
        this.name = name;
        notifyObservers();
    }

    public synchronized String getName() {
        return name;
    }

    // Methods for Taiga credentials:
    public synchronized void setTaigaCredentials(String username, String password, String slug) {
        this.taigaUsername = username;
        this.taigaPassword = password;
        this.projectSlug = slug;
    }

    public synchronized String getTaigaUsername() {
        return taigaUsername;
    }

    public synchronized String getTaigaPassword() {
        return taigaPassword;
    }

    public synchronized String getProjectSlug() {
        return projectSlug;
    }

    // Fetched stories from Taiga
    private JSONArray fetchedStories;
    public synchronized void setFetchedStories(JSONArray stories) {
        this.fetchedStories = stories;
        notifyObservers();
    }

    public synchronized JSONArray getFetchedStories() {
        return fetchedStories;
    }

    public synchronized Map<String, String> getAllParticipants() {
        return new HashMap<>(participants);
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }
}
