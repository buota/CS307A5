/**
 * Logan Lumetta 4/28
 * sprint1
 */

public class FindAverage {
    public float findAverage(int[] scores) {
        float average = 0;
        for (int i = 0; i < scores.length; i++) {
            average += scores[i];
        }
        average /= scores.length;
        return average;
    }
    public static void main(String[] args) {
        FindAverage findAvg = new FindAverage();

        int[] scores = {1, 2, 3, 4, 5};

        float average = findAvg.findAverage(scores);

        System.out.println("The average score is: " + average);
    }
}
