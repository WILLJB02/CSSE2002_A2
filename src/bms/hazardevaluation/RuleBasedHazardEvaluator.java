package bms.hazardevaluation;

import bms.sensors.HazardSensor;
import bms.sensors.OccupancySensor;
import java.util.List;

/**
 * Evaluates the hazard level of a location using a rule based system.
 */
public class RuleBasedHazardEvaluator implements HazardEvaluator {
    /**
     * List of sensors to be included in hazard evaluator
     */
    private final List<HazardSensor> sensors;

    /**
     * Creates a new rule-based hazard evaluator with the given list of sensors.
     * @param sensors sensors to be used in the hazard level calculation
     */
    public RuleBasedHazardEvaluator(List<HazardSensor> sensors) {
        this.sensors = sensors;
    }

    /**
     * Returns a calculated hazard level based on applying a set of rules to
     * the list of sensors passed to the constructor.
     * The rules to be applied are as follows. Note that square brackets [] have
     * been used to indicate mathematical grouping.
     *      - If there are no sensors, return 0.
     *      - If there is only one sensor, return that sensor's current hazard
     *      level as per HazardSensor.getHazardLevel().
     *      - If there is more than one sensor:
     *      - If any sensor that is not an OccupancySensor has a hazard level
     *      of 100, return 100.
     *      - Calculate the average hazard level of all sensors that are not an
     *      OccupancySensor. Floating point division should be used when finding
     *      the average.
     *      - If there is an OccupancySensor in the list, multiply the average
     *      calculated in the previous step by [the occupancy sensor's current
     *      hazard level divided by 100, using floating point division].
     *      - Return the final average rounded to the nearest integer between 0
     *      and 100.
     * You can assume that there is no more than one OccupancySensor in the list
     * passed to the constructor.
     * @return calculated hazard level according to a set of rules
     */
    public int evaluateHazardLevel() {
        if (sensors.size() == 0) {
            return 0;
        } else if (sensors.size() == 1) {
            return sensors.get(0).getHazardLevel();
        } else {
            boolean occupancySensor = false;
            boolean maxHazardLevel = false;
            int occupancySensorValue = 0;
            float total = 0;
            float average;

            // adding up the comfort level of all non-occupancy sensors
            for (HazardSensor s : sensors) {
                if (s instanceof OccupancySensor) {
                    occupancySensor = true;
                    occupancySensorValue = s.getHazardLevel();
                } else if (s.getHazardLevel() == 100) {
                    maxHazardLevel = true;
                    total += s.getHazardLevel();
                } else {
                    total += s.getHazardLevel();
                }
            }
            // returning result
            if (maxHazardLevel) {
                return 100;
            } else if (!occupancySensor) {
                average = total / sensors.size();
                return Math.round(average);
            } else {
                average = total / (sensors.size() - 1);
                return Math.round(average * ((float) occupancySensorValue
                        / 100));
            }
        }
    }

    /**
     * Returns the string representation of this hazard evaluator.
     * The format of the string to return is simply "RuleBased" without
     * double quotes.
     * @return tring representation of this room
     */
    @Override
    public String toString() {
        return "RuleBased";
    }
}
