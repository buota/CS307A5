/**
 * Logan Lumetta 5/29
 *Repository Class
 */
package publisher;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    private static Repository instance;

    private String sessionID;
    private int[] votes = new int[0];
    private int currentStoryIndex = 0;

    private final List<RepositoryObserver> observers = new ArrayList<>();
    private String mode;
    private String name;
    private ArrayList<String> stories;
    private JSONArray fetchedStories;
    private String taigaUsername;
    private String taigaPassword;
    private String projectSlug;

    private Repository() {}

    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
        notifyObservers();
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setVotes(int[] votes) {
        this.votes = votes;
        notifyObservers();
    }

    public int[] getVotes() {
        return votes;
    }

    public void setCurrentStoryIndex(int index) {
        this.currentStoryIndex = index;
        notifyObservers();
    }

    public int getCurrentStoryIndex() {
        return currentStoryIndex;
    }

    public void addObserver(RepositoryObserver observer) {
        observers.add(observer);
    }



    private void notifyObservers() {
        for (RepositoryObserver observer : observers) {
            observer.update();
        }
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setName(String name) {
        this.name = name;
        notifyObservers();
    }
    public String getName(){
        return this.name;
    }
    public String getMode(){
        return this.mode;
    }

    public void setStories(List<String> stories) {
        this.stories = new ArrayList<>(stories);
        notifyObservers();
    }
    public List<String> getStories() {
        return new ArrayList<>(stories);
    }

    public void setFetchedStories(JSONArray stories) {
        this.fetchedStories = stories;

    }
    public JSONArray getFetchedStories() {
        return fetchedStories;
    }
    public void setTaigaCredentials(String username, String password, String slug) {
        this.taigaUsername = username;
        this.taigaPassword = password;
        this.projectSlug = slug;
    }

    public String getTaigaUsername() {
        return taigaUsername;
    }
    public String getTaigaPassword() {
        return taigaPassword;
    }
    public String getProjectSlug() {
        return projectSlug;
    }
}
