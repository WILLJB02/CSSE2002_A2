package bms.hazardevaluation;

import bms.sensors.HazardSensor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Evaluates the hazard level of a location using weightings for the sensor
 * values.
 * The sum of the weightings of all sensors must equal 100.
 */
public class WeightingBasedHazardEvaluator implements HazardEvaluator {
    /**
     * Map which holds sensors in the hazard evaluator and their weightings
     */
    private final Map<HazardSensor, Integer> sensors;

    /**
     * Creates a new weighting-based hazard evaluator with the given sensors
     * and weightings.
     * Each weighting must be between 0 and 100 inclusive, and the total sum
     * of all weightings must equal 100.
     * @param sensors mapping of sensors to their respective weighting
     * @throws IllegalArgumentException  if any weighting is below 0 or above
     * 100; or if the sum of all weightings is not equal to 100
     */
    public WeightingBasedHazardEvaluator(Map<HazardSensor, Integer> sensors)
            throws IllegalArgumentException {
        boolean illegalArgument = false;
        int total = 0;
        // adding up the weightings of all sensors in the map
        for (Map.Entry<HazardSensor, Integer> entry: sensors.entrySet()) {
            if (entry.getValue() < 0 || entry.getValue() > 100) {
                illegalArgument = true;
            }
            total += entry.getValue();
        }
        if (illegalArgument || total != 100) {
            throw new IllegalArgumentException();
        }
        this.sensors = sensors;
    }

    /**
     * Returns the weighted average of the current hazard levels of all
     * sensors in the map passed to the constructor.
     * The weightings given in the constructor should be used. The final
     * evaluated hazard level should be rounded to the nearest integer between
     * 0 and 100.
     * @return weighted average of current sensor hazard levels
     */
    public int evaluateHazardLevel() {
        float weightedAverage = 0;
        for (Map.Entry<HazardSensor, Integer> entry: sensors.entrySet()) {
            float hazardLevel = (float) entry.getKey().getHazardLevel();
            float weighing = (float) entry.getValue();
            weightedAverage +=  hazardLevel * (weighing / 100);
        }
        return Math.round(weightedAverage);
    }

    /**
     * Returns a list containing the weightings associated with all of the
     * sensors monitored by this hazard evaluator.
     * @return weightings
     */
    public List<Integer> getWeightings() {
        ArrayList<Integer> weightings = new ArrayList<>();
        for (Map.Entry<HazardSensor, Integer> entry: sensors.entrySet()) {
            weightings.add(entry.getValue());
        }
        return weightings;
    }

    /**
     * Returns the string representation of this hazard evaluator.
     * The format of the string to return is simply "WeightingBased"
     * without the double quotes.
     *
     * See the demo save file for an example (uqstlucia.txt).
     * @return string representation of this hazard evaluator
     */
    public String toString() {
        return "WeightingBased";
    }
}
