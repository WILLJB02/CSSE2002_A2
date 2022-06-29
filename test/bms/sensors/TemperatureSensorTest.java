package bms.sensors;

import org.junit.Test;

import static org.junit.Assert.*;

public class TemperatureSensorTest {

    @Test
    public void getComfortLevel() {
        TemperatureSensor temperatureSensor = new TemperatureSensor(new int[]{20,25});
        assertEquals(100, temperatureSensor.getComfortLevel());
        temperatureSensor = new TemperatureSensor(new int[]{24,25});
        assertEquals(100, temperatureSensor.getComfortLevel());
        temperatureSensor = new TemperatureSensor(new int[]{26,25});
        assertEquals(100, temperatureSensor.getComfortLevel());
        temperatureSensor = new TemperatureSensor(new int[]{16,25});
        assertEquals(20, temperatureSensor.getComfortLevel());
        temperatureSensor = new TemperatureSensor(new int[]{30,25});
        assertEquals(20, temperatureSensor.getComfortLevel());
        temperatureSensor = new TemperatureSensor(new int[]{15,25});
        assertEquals(0, temperatureSensor.getComfortLevel());
        temperatureSensor = new TemperatureSensor(new int[]{31,25});
        assertEquals(0, temperatureSensor.getComfortLevel());
        temperatureSensor = new TemperatureSensor(new int[]{0,25});
        assertEquals(0, temperatureSensor.getComfortLevel());
        temperatureSensor = new TemperatureSensor(new int[]{40,25});
        assertEquals(0, temperatureSensor.getComfortLevel());
        temperatureSensor = new TemperatureSensor(new int[]{28,29,26,24,25,26});
        assertEquals("TemperatureSensor:28,29,26,24,25,26",temperatureSensor.encode());
    }

}