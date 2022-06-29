package bms.sensors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CarbonDioxideSensorTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getComfortLevel() {
        CarbonDioxideSensor carbonDioxideSensor1 = new CarbonDioxideSensor(new int[]{640, 100, 200},2,600,200);
        assertEquals(80, carbonDioxideSensor1.getComfortLevel());

        CarbonDioxideSensor carbonDioxideSensor2 = new CarbonDioxideSensor(new int[]{420, 100, 200},2,500,100);
        assertEquals(20, carbonDioxideSensor2.getComfortLevel());

        CarbonDioxideSensor carbonDioxideSensor3 = new CarbonDioxideSensor(new int[]{1000, 100, 200},2,800,100);
        assertEquals(0, carbonDioxideSensor3.getComfortLevel());

        CarbonDioxideSensor carbonDioxideSensor4 = new CarbonDioxideSensor(new int[]{565, 100, 200},2,800,300);
        assertEquals(22, carbonDioxideSensor4.getComfortLevel());
    }

    @Test
    public void equals() {
        CarbonDioxideSensor carbonDioxideSensor1 = new CarbonDioxideSensor(new int[]{640, 100, 200},2,600,200);
        CarbonDioxideSensor carbonDioxideSensor2 = new CarbonDioxideSensor(new int[]{640, 100, 200},2,600,200);
        assertEquals(carbonDioxideSensor1, carbonDioxideSensor2);
        carbonDioxideSensor1 = new CarbonDioxideSensor(new int[]{640, 101, 200},2,600,200);
        carbonDioxideSensor2 = new CarbonDioxideSensor(new int[]{640, 100, 200},2,600,200);
        assertNotEquals(carbonDioxideSensor1, carbonDioxideSensor2);
        carbonDioxideSensor1 = new CarbonDioxideSensor(new int[]{640, 200, 100},2,600,200);
        carbonDioxideSensor2 = new CarbonDioxideSensor(new int[]{640, 100, 200},2,600,200);
        assertNotEquals(carbonDioxideSensor1, carbonDioxideSensor2);
        carbonDioxideSensor1 = new CarbonDioxideSensor(new int[]{640, 200, 100},1,600,200);
        carbonDioxideSensor2 = new CarbonDioxideSensor(new int[]{640, 100, 200},2,600,200);
        assertNotEquals(carbonDioxideSensor1, carbonDioxideSensor2);
        carbonDioxideSensor1 = new CarbonDioxideSensor(new int[]{640, 200, 100},2,600,200);
        carbonDioxideSensor2 = new CarbonDioxideSensor(new int[]{640, 100, 200},2,500,200);
        assertNotEquals(carbonDioxideSensor1, carbonDioxideSensor2);
        carbonDioxideSensor1 = new CarbonDioxideSensor(new int[]{640, 200, 100},2,600,200);
        carbonDioxideSensor2 = new CarbonDioxideSensor(new int[]{640, 100, 200},2,500,100);
        assertNotEquals(carbonDioxideSensor1, carbonDioxideSensor2);
    }

    @Test
    public void hashCodeTest() {
        CarbonDioxideSensor carbonDioxideSensor1 = new CarbonDioxideSensor(new int[]{640, 100, 200},2,600,200);
        CarbonDioxideSensor carbonDioxideSensor2 = new CarbonDioxideSensor(new int[]{640, 100, 200},2,600,200);
        assertEquals(carbonDioxideSensor1.hashCode(), carbonDioxideSensor2.hashCode());
        carbonDioxideSensor1 = new CarbonDioxideSensor(new int[]{640, 100, 200},1,600,200);
        carbonDioxideSensor2 = new CarbonDioxideSensor(new int[]{640, 100, 200},2,600,200);
        assertNotEquals(carbonDioxideSensor1.hashCode(), carbonDioxideSensor2.hashCode());
    }

    @Test
    public void encode() {
        CarbonDioxideSensor sensor = new CarbonDioxideSensor(new int[]{690, 740}, 5, 700, 150);
        assertEquals("CarbonDioxideSensor:690,740:5:700:150",sensor.encode());
    }


}