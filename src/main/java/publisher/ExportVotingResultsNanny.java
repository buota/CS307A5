/**
 * @author: Vardaan-K
 */
package publisher;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

public class ExportVotingResultsNanny {

    public void export(ArrayList<String> stories,  Map<String, Map<String, Integer>> allVotingResults,Map<String, String> participantNames, Map<String, Float> storyAverages){
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(null);

        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("VOTING RESULTS");
                for (String story : stories){
                    writer.println("Story Name:" + story);
                    Map<String, Integer> votes = allVotingResults.get(story);
                    for (String pId : votes.keySet()){
                        int vote = votes.get(pId);
                        writer.println(participantNames.get(pId) + ":" +(vote == -1 ? "?" : vote) + "\n" );
                    }
                    Float avg = storyAverages.get(story);
                    writer.println("Average:" +  (avg != null ? String.format("%.2f", avg) : "N/A"));
                }

                JOptionPane.showMessageDialog(null, "Export successful!");
            }
            catch(IOException e){
                System.out.println("Error creating file.");
            }

        }
    }



}

