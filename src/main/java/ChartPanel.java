import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;

import java.awt.*;
import java.util.*;

public class ChartPanel extends JPanel {
    private ArrayList<String> stories;
    private Map<String, Map<String, Integer>> allVotingResults;


    public ChartPanel(ArrayList<String> stories, Map<String, Map<String, Integer>> allVotingResults){
        this.allVotingResults = allVotingResults;
        this.stories = stories;
        JPanel charts = new JPanel();
        charts.setLayout(new BoxLayout(charts, BoxLayout.Y_AXIS));
        charts.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        for (String story: stories){
            Collection<Integer> votes = allVotingResults.get(story).values();
            Map<Integer, Integer> voteCounts = new HashMap<>();

            for (int vote: votes){
                voteCounts.put(vote,voteCounts.getOrDefault(vote,0)+1);
            }

            DefaultPieDataset dataset = createData(voteCounts);
            JFreeChart chart =ChartFactory.createPieChart(story,dataset,true,false,false);
            charts.setSize(200,200);
            org.jfree.chart.ChartPanel chartPanel = new org.jfree.chart.ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(300, 225));

            charts.add(Box.createVerticalStrut(10));
            charts.add(chartPanel);
        }
        JScrollPane scrollPane = new JScrollPane(charts);
        add(scrollPane,BorderLayout.CENTER);

    }
    public DefaultPieDataset createData(Map<Integer, Integer> voteCounts){
            DefaultPieDataset dataset = new DefaultPieDataset();
            for(Map.Entry<Integer,Integer> entry : voteCounts.entrySet()){
                dataset.setValue(entry.getKey() + " Point(s)",entry.getValue());

            }
            return dataset;

    }

}
