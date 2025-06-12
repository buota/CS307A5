import java.util.ArrayList;
import java.util.List;

/**
 * Logan Lumetta 5/28
 * Class to keep track of votes
 */

public class Votes {
    private List<Integer> votes;
    private int numPlayers;

    public Votes(int numPlayers) {
        this.numPlayers = numPlayers;
        this.votes = new ArrayList<>(numPlayers);
    }

    /**
     * returns 1 if the array is full after the addition (everyone has voted)
     * reutrns 0 if the array is not full after the addition
     * returns -1 if the array is already full before the addition
     */
    public int addVote(int vote) {
        if (votes.size() >= numPlayers) {
            System.out.println("Cannot add more votes. Voting is already full.");
            return -1;
        }

        votes.add(vote);

        if (votes.size() == numPlayers) {
            return 1;
        } else {
            return 0;
        }
    }

    // resets the list of votes
    public void clearVotes() {
        votes.clear();
    }

    public void printVotes() {
        System.out.println("Votes: " + votes);
    }

    public static void main(String[] args) {
        Votes votes = new Votes(3);

        System.out.println(votes.addVote(5));
        System.out.println(votes.addVote(8));
        System.out.println(votes.addVote(3));
        System.out.println(votes.addVote(7));

        votes.printVotes();

        votes.clearVotes();
        votes.printVotes();
    }
}
