/**
 * Logan Lumetta 5/28
 *Nanny for the home page
 */
public class HomePageNanny {
    private final Repository repository;

    public HomePageNanny() {
        this.repository = Repository.getInstance();
    }

    public void createRoom(String name, String mode) {
        String sessionID = new CreateSessionID().createSessionID();
        repository.setSessionID(sessionID);
        repository.setCurrentStoryIndex(0);
        repository.setVotes(new int[0]);

        System.out.println("Room created:");
        System.out.println("Name: " + name);
        System.out.println("Mode: " + mode);
        System.out.println("Session ID: " + sessionID);
    }

    public void joinRoom(String sessionID) {
        repository.setSessionID(sessionID);
        System.out.println("Joined room with session ID: " + sessionID);
    }
}
