package bms.hazardevaluation;

import bms.sensors.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RuleBasedHazardEvaluatorTest {

    @Test
    public void evaluateHazardLevel() {
        List<HazardSensor> sensors = new ArrayList<>();
        RuleBasedHazardEvaluator ruleBasedHazardEvaluator = new RuleBasedHazardEvaluator(sensors);
        assertEquals(0, ruleBasedHazardEvaluator.evaluateHazardLevel());


        CarbonDioxideSensor carbonDioxideSensor = new CarbonDioxideSensor(new int[]{2500,500,600},1,450,100);
        sensors.add(carbonDioxideSensor);
        ruleBasedHazardEvaluator = new RuleBasedHazardEvaluator(sensors);
        System.out.println(carbonDioxideSensor.getHazardLevel());
        System.out.println(ruleBasedHazardEvaluator.evaluateHazardLevel());
        assertEquals(carbonDioxideSensor.getHazardLevel(), ruleBasedHazardEvaluator.evaluateHazardLevel());

        TemperatureSensor temperatureSensor = new TemperatureSensor(new int[]{400,500,600});
        sensors.add(temperatureSensor);
        ruleBasedHazardEvaluator = new RuleBasedHazardEvaluator(sensors);
        System.out.println(temperatureSensor.getHazardLevel());
        System.out.println(ruleBasedHazardEvaluator.evaluateHazardLevel());
        assertEquals(temperatureSensor.getHazardLevel(), ruleBasedHazardEvaluator.evaluateHazardLevel());


        sensors = new ArrayList<>();
        OccupancySensor occupancySensor2 = new OccupancySensor(new int[]{1000,500,600},2,50);
        sensors.add(carbonDioxideSensor);
        sensors.add(occupancySensor2);
        ruleBasedHazardEvaluator = new RuleBasedHazardEvaluator(sensors);
        System.out.println(carbonDioxideSensor.getHazardLevel() + "a");
        System.out.println(occupancySensor2.getHazardLevel() + "b");
        System.out.println(ruleBasedHazardEvaluator.evaluateHazardLevel());


        sensors = new ArrayList<>();
        carbonDioxideSensor = new CarbonDioxideSensor(new int[]{2500,500,600},1,450,100);
        NoiseSensor noiseSensor = new NoiseSensor(new int[]{66,500,600},2);
        sensors.add(carbonDioxideSensor);
        sensors.add(noiseSensor);
        ruleBasedHazardEvaluator = new RuleBasedHazardEvaluator(sensors);
        System.out.println(noiseSensor.getHazardLevel());
        System.out.println(ruleBasedHazardEvaluator.evaluateHazardLevel());
        assertEquals(63, ruleBasedHazardEvaluator.evaluateHazardLevel());

        OccupancySensor occupancySensor = new OccupancySensor(new int[]{41,500,600},2,50);
        sensors.add(occupancySensor);
        ruleBasedHazardEvaluator = new RuleBasedHazardEvaluator(sensors);
        System.out.println(occupancySensor.getHazardLevel());
        System.out.println(ruleBasedHazardEvaluator.evaluateHazardLevel());
        assertEquals(51, ruleBasedHazardEvaluator.evaluateHazardLevel());
        assertEquals("RuleBased", ruleBasedHazardEvaluator.toString());
    }

}