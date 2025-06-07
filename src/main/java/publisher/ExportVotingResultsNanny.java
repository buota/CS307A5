package publisher; /**
 * @author: Vardaan-K
 */

import javax.swing.*;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class ExportVotingResultsNanny {

    public void export(Blackboard blackboard){
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(null);

        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("Room Name: " + blackboard.getCurrentRoom());
                writer.println("Participants: " + blackboard.getNames());
                writer.println("Room Mode: " + blackboard.getMode());

                LinkedList<Blackboard.Story> stories = blackboard.getStories();

                for (Blackboard.Story story  : stories) {
                    writer.printf("%s,%f,%d,%s\n",story.getTitle(),story.getAverage(),story.getNumberOfVotes(),story.getVotes());
                }

                JOptionPane.showMessageDialog(null, "Export successful!");
            }
            catch(IOException e){
                System.out.println("Error creating file.");
            }

        }
    }



}

