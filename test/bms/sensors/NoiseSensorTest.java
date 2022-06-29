package bms.sensors;

import org.junit.Test;

import static org.junit.Assert.*;

public class NoiseSensorTest {

    @Test
    public void getComfortLevel() {
        int[] readings2 = {67,45,50};
        NoiseSensor noise = new NoiseSensor(readings2,1);
        System.out.println(noise.calculateRelativeLoudness());
        System.out.println(noise.getComfortLevel());

        NoiseSensor noise2 = new NoiseSensor(new int[]{55,62,69,63},3);

        assertEquals("NoiseSensor:55,62,69,63:3",noise2.encode());
    }

}