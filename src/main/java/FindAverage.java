/**
 * Logan Lumetta 5/28
 * Helper class to average an array of scores
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
}
