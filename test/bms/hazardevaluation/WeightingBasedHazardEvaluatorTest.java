package bms.hazardevaluation;

import bms.exceptions.DuplicateSensorException;
import bms.exceptions.FileFormatException;
import bms.sensors.*;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class WeightingBasedHazardEvaluatorTest {

    @Test
    public void WeightingBasedEvlautor() throws DuplicateSensorException, IOException, FileFormatException {
        int[] readings1 = {40,45,50};
        HazardSensor occupancySensor = new OccupancySensor(readings1,1,100);
        int[] readings2 = {64,45,50};
        HazardSensor noise = new NoiseSensor(readings2,1);
        int[] readings3 = {30,45,50};
        HazardSensor temp = new TemperatureSensor(readings3);

        int[] readings4 = {2600,45,50};
        HazardSensor carbondioxide = new CarbonDioxideSensor(readings4,2,400,100);

        System.out.println(occupancySensor.getHazardLevel());
        System.out.println(noise.getHazardLevel());
        System.out.println(temp.getHazardLevel());
        System.out.println(carbondioxide.getHazardLevel());

        Map<HazardSensor,Integer> weightings = new HashMap<>();
        weightings.put(occupancySensor, 20);
        weightings.put(noise,30);
        weightings.put(temp,50);
        WeightingBasedHazardEvaluator hazard = new WeightingBasedHazardEvaluator(weightings);
        System.out.println(hazard.evaluateHazardLevel());

        assertEquals(28,hazard.evaluateHazardLevel());
        assertEquals("WeightingBased", hazard.toString());

        List<Integer> numbers = new ArrayList<>();
        numbers.add(50);
        numbers.add(30);
        numbers.add(20);
        assertEquals(numbers, hazard.getWeightings());
    }

    @Test (expected = IllegalArgumentException.class)
    public void WeightingBasedEvlautor3 () {
        int[] readings1 = {40,45,50};
        HazardSensor occupancySensor = new OccupancySensor(readings1,1,100);
        int[] readings2 = {64,45,50};
        HazardSensor noise = new NoiseSensor(readings2,1);
        int[] readings3 = {30,45,50};
        HazardSensor temp = new TemperatureSensor(readings3);

        int[] readings4 = {2600,45,50};
        HazardSensor carbondioxide = new CarbonDioxideSensor(readings4,2,400,100);

        System.out.println(occupancySensor.getHazardLevel());
        System.out.println(noise.getHazardLevel());
        System.out.println(temp.getHazardLevel());
        System.out.println(carbondioxide.getHazardLevel());

        Map<HazardSensor,Integer> weightings = new HashMap<>();
        weightings.put(occupancySensor, -20);
        weightings.put(noise,30);
        weightings.put(temp,50);
        WeightingBasedHazardEvaluator hazard = new WeightingBasedHazardEvaluator(weightings);
    }

    @Test (expected = IllegalArgumentException.class)
    public void WeightingBasedEvlautor2 () {
        int[] readings1 = {40,45,50};
        HazardSensor occupancySensor = new OccupancySensor(readings1,1,100);
        int[] readings2 = {64,45,50};
        HazardSensor noise = new NoiseSensor(readings2,1);
        int[] readings3 = {30,45,50};
        HazardSensor temp = new TemperatureSensor(readings3);

        int[] readings4 = {2600,45,50};
        HazardSensor carbondioxide = new CarbonDioxideSensor(readings4,2,400,100);

        System.out.println(occupancySensor.getHazardLevel());
        System.out.println(noise.getHazardLevel());
        System.out.println(temp.getHazardLevel());
        System.out.println(carbondioxide.getHazardLevel());

        Map<HazardSensor,Integer> weightings = new HashMap<>();
        weightings.put(occupancySensor, 20);
        weightings.put(noise,30);
        weightings.put(temp,40);
        WeightingBasedHazardEvaluator hazard = new WeightingBasedHazardEvaluator(weightings);
    }

}