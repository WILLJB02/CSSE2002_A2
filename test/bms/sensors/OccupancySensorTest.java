package bms.sensors;

import org.junit.Test;

import static org.junit.Assert.*;

public class OccupancySensorTest {

    @Test
    public void getComfortLevel() {
        OccupancySensor occupancySensor = new OccupancySensor(new int[]{14,20},1,20);
        assertEquals(30, occupancySensor.getComfortLevel());
        occupancySensor = new OccupancySensor(new int[]{17,20},1,15);
        assertEquals(0, occupancySensor.getComfortLevel());
    }

    @Test
    public void equals() {
        OccupancySensor occupancySensor = new OccupancySensor(new int[]{14,20},1,20);
        OccupancySensor occupancySensor2 = new OccupancySensor(new int[]{14,20},1,20);
        assertEquals(occupancySensor, occupancySensor2);
        assertEquals(occupancySensor.hashCode(), occupancySensor2.hashCode());


        occupancySensor = new OccupancySensor(new int[]{14,20},1,20);
        occupancySensor2 = new OccupancySensor(new int[]{20,14},1,20);
        assertNotEquals(occupancySensor, occupancySensor2);
        assertEquals(occupancySensor.hashCode(), occupancySensor2.hashCode());

        occupancySensor = new OccupancySensor(new int[]{14,20},2,20);
        occupancySensor2 = new OccupancySensor(new int[]{14,20},1,20);
        assertNotEquals(occupancySensor, occupancySensor2);
        assertNotEquals(occupancySensor.hashCode(), occupancySensor2.hashCode());

        occupancySensor = new OccupancySensor(new int[]{14,20},2,20);
        occupancySensor2 = new OccupancySensor(new int[]{14,20},2,21);
        assertNotEquals(occupancySensor, occupancySensor2);
        assertNotEquals(occupancySensor.hashCode(), occupancySensor2.hashCode());
        occupancySensor = new OccupancySensor(new int[]{32,35,26,4,3,2,6,16,17,22,28,29},2,40);
        assertEquals("OccupancySensor:32,35,26,4,3,2,6,16,17,22,28,29:2:40",occupancySensor.encode());
    }


}