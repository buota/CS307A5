package publisher;

/**
 * Logan Lumetta 5/29
 * Nanny class
 */

public class CreateRoomNanny {

    public void createRoom(String name, String mode, String sessionID) {
        Repository repo = Repository.getInstance();
        repo.setSessionID(sessionID);
        repo.setMode(mode);
        repo.setName(name);
    }
}
