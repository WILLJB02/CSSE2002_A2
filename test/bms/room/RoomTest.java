package bms.room;
import bms.exceptions.DuplicateSensorException;
import bms.floor.MaintenanceSchedule;
import bms.hazardevaluation.HazardEvaluator;
import bms.room.Room;
import bms.room.RoomType;
import bms.sensors.*;
import bms.hazardevaluation.RuleBasedHazardEvaluator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class RoomTest {

    @Test
    public void roomEqual() throws DuplicateSensorException {

        //no rooms
        Room room1 = new Room(1,RoomType.STUDY,20.01);
        Room room2 = new Room(1,RoomType.STUDY,20.01);
        assertEquals(room1, room2);

        //area slightly different
        room1 = new Room(1,RoomType.STUDY,20.010002);
        room2 = new Room(1,RoomType.STUDY,20.010001);
        assertEquals(room1, room2);


        //samerooms
        Room room3 = new Room(1,RoomType.STUDY,20.01);
        Room room4 = new Room(1,RoomType.STUDY,20.01);
        Sensor sensor1 = new OccupancySensor(new int[]{13, 24, 28, 15, 6}, 4, 30);
        Sensor sensor2 = new CarbonDioxideSensor(new int[]{690, 740}, 5, 700, 150);
        Sensor sensor3 = new NoiseSensor(new int[]{55, 62, 69, 63}, 3);
        room3.addSensor(sensor1);
        room3.addSensor(sensor2);
        room3.addSensor(sensor3);
        room4.addSensor(sensor1);
        room4.addSensor(sensor3);
        room4.addSensor(sensor2);
        assertEquals(room3, room4);


        //different Rooms
        room3 = new Room(1,RoomType.STUDY,20.01);
        room4 = new Room(1,RoomType.STUDY,20.01);
        room3.addSensor(sensor1);
        room3.addSensor(sensor3);
        room4.addSensor(sensor1);
        room4.addSensor(sensor2);
        assertNotEquals(room3, room4);

        room3 = new Room(1,RoomType.STUDY,20.01);
        room4 = new Room(1,RoomType.STUDY,20.01);
        room3.addSensor(sensor1);
        room3.addSensor(sensor3);
        room4.addSensor(sensor1);
        room4.addSensor(sensor2);
        room4.addSensor(sensor3);
        assertNotEquals(room3, room4);

        room1 = new Room(1,RoomType.STUDY,20.01);
        room2 = new Room(2,RoomType.STUDY,20.01);
        assertNotEquals(room1, room2);

        room1 = new Room(1,RoomType.STUDY,20.01);
        room2 = new Room(1,RoomType.LABORATORY,20.01);
        assertNotEquals(room1, room2);

        room1 = new Room(1,RoomType.STUDY,20.01);
        room2 = new Room(1,RoomType.STUDY,22.01);
        assertNotEquals(room1, room2);


    }



    @Test
    public void maintenanceOngoing() {
        Room room = new Room(1,RoomType.STUDY,12);
        assertFalse(room.maintenanceOngoing());
        room.setMaintenance(true);
        assertTrue(room.maintenanceOngoing());
        room.setMaintenance(false);
        assertFalse(room.maintenanceOngoing());
    }

    @Test
    public void getHazardEvaluator() {
        Room room = new Room(1,RoomType.STUDY,12);
        assertNull(room.getHazardEvaluator());
        List<HazardSensor> sensors = new ArrayList<>();
        RuleBasedHazardEvaluator ruleBasedHazardEvaluator = new RuleBasedHazardEvaluator(sensors);
        room.setHazardEvaluator(ruleBasedHazardEvaluator);
        assertEquals(ruleBasedHazardEvaluator, room.getHazardEvaluator());
    }

    @Test
    public void addSensor() throws DuplicateSensorException {
        Room room = new Room(1,RoomType.STUDY,20.01);
        Sensor sensor = new OccupancySensor(new int[]{13, 24, 28, 15, 6}, 4, 30);
        List<HazardSensor> sensors = new ArrayList<>();
        RuleBasedHazardEvaluator ruleBasedHazardEvaluator = new RuleBasedHazardEvaluator(sensors);
        room.setHazardEvaluator(ruleBasedHazardEvaluator);
        assertEquals(ruleBasedHazardEvaluator, room.getHazardEvaluator());
        room.addSensor(sensor);
        assertNull(room.getHazardEvaluator());
    }

    @Test
    public void evaluateRoomState() throws DuplicateSensorException {
        Room room = new Room(1,RoomType.STUDY,20.01);
        TemperatureSensor temperatureSensor = new TemperatureSensor(new int[] {100,4,2});
        room.addSensor(temperatureSensor);
        assertEquals(RoomState.EVACUATE,room.evaluateRoomState());

        room = new Room(1,RoomType.STUDY,20.01);
        room.setFireDrill(true);
        assertEquals(RoomState.EVACUATE,room.evaluateRoomState());

        room = new Room(1,RoomType.STUDY,20.01);
        room.setMaintenance(true);
        assertEquals(RoomState.MAINTENANCE,room.evaluateRoomState());
        room = new Room(1,RoomType.STUDY,20.01);
        assertEquals(RoomState.OPEN,room.evaluateRoomState());
    }

    @Test
    public void encode() throws DuplicateSensorException {
        Room room3 = new Room(1,RoomType.STUDY,20.01);
        Sensor sensor1 = new OccupancySensor(new int[]{13, 24, 28, 15, 6}, 4, 30);
        Sensor sensor2 = new CarbonDioxideSensor(new int[]{690, 740}, 5, 700, 150);
        Sensor sensor3 = new NoiseSensor(new int[]{55, 62, 69, 63}, 3);
        room3.addSensor(sensor1);
        room3.addSensor(sensor2);
        room3.addSensor(sensor3);
    }




}