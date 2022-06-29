package bms.sensors;

import org.junit.Test;

import static org.junit.Assert.*;

public class TimedSensorTest {

    @Test
    public void equalsTest() {
        TimedSensor timedSensor1 = new CarbonDioxideSensor(new int[]{500,600,700},4,400,100);
        TimedSensor timedSensor2 = new CarbonDioxideSensor(new int[]{500,600,700},4,400,100);

        assertEquals(timedSensor1,timedSensor2);

        timedSensor1 = new CarbonDioxideSensor(new int[]{500,600,700},4,400,100);
        timedSensor2 = new CarbonDioxideSensor(new int[]{500,600,700},3,400,100);

        assertNotEquals(timedSensor1,timedSensor2);

        timedSensor1 = new CarbonDioxideSensor(new int[]{500,400,700},4,400,100);
        timedSensor2 = new CarbonDioxideSensor(new int[]{500,600,700},4,400,100);

        assertNotEquals(timedSensor1,timedSensor2);

        timedSensor1 = new CarbonDioxideSensor(new int[]{500,400,700},4,400,100);
        timedSensor2 = new CarbonDioxideSensor(new int[]{500,400,700,300},4,400,100);

        assertNotEquals(timedSensor1,timedSensor2);

        timedSensor1 = new CarbonDioxideSensor(new int[]{500,400,700},4,400,100);
        timedSensor2 = new NoiseSensor(new int[]{500,400,700},4);

        assertNotEquals(timedSensor1,timedSensor2);
    }
}