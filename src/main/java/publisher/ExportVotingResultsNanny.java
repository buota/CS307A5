/**
 * @author: Vardaan-K
 */
package publisher;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ExportVotingResultsNanny {

    public void export(ArrayList<String> stories, List<List<Integer>> partitions){
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("VOTING RESULTS");
                for (int i = 0; i < stories.size(); i++){
                    int sum = 0;
                    for(int n:partitions.get(i)){
                        sum += n;
                    }
                    double avg = (double) sum/partitions.get(i).size();
                    writer.println("Story Name:" + stories.get(i));
                    writer.print("Votes:" + partitions.get(i));

                    writer.println("Average:" +  avg);
                }

                JOptionPane.showMessageDialog(null, "Export successful!");
            }
            catch(IOException e){
                System.out.println("Error creating file.");
            }

        }
    }



}

