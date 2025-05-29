/**
 * Logan Lumetta 4/28
 * sprint1
 */

import java.util.Random;

public class CreateSessionID {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static int ID_LEN = 6;
    private Random random = new Random();

    public String createSessionID() {
        StringBuilder sb = new StringBuilder(ID_LEN);
        for (int i = 0; i < ID_LEN; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}
