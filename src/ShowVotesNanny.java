/**
 * @author: Vardaan-K
 */

import javax.swing.*;
import java.awt.*;
public class ShowVotesNanny {

    public JLabel averageLabel = new JLabel("AVERAGE");
    public JLabel votingLabel = new JLabel("VOTES");
    public boolean displayVotingResults(JPanel eastPanel,boolean votesDisplayed, Blackboard blackboard){
        averageLabel.setText("AVERAGE: " + blackboard.getCurrentStory().getAverage());
        votingLabel.setText("VOTES: " + blackboard.getCurrentStory().getVotes());
        if(!votesDisplayed) {
            eastPanel.add(averageLabel);
            eastPanel.add(votingLabel);
            votesDisplayed = true;

        }
        else{
            eastPanel.remove(averageLabel);
            eastPanel.remove(votingLabel);
            votesDisplayed = false;
        }
        eastPanel.revalidate();
        eastPanel.repaint();
        return votesDisplayed;
    }





}
