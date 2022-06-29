package bms.util;

import bms.building.Building;
import bms.exceptions.DuplicateRoomException;
import bms.exceptions.DuplicateSensorException;
import bms.exceptions.InsufficientSpaceException;
import bms.floor.Floor;
import bms.room.Room;
import bms.room.RoomType;
import bms.sensors.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.*;

public class StudyRoomRecommenderTest {

    private Building building;
    private Floor floor1, floor2, floor3;
    private Room room1, room2, room3, room4, room5;

    @Before
    public void setUp() throws Exception {
        building  = new Building("Test Building");
        floor1 = new Floor(1,25.12, 30.10);
        floor2 = new Floor(2,24, 28);
        floor3 = new Floor(3,20, 25);

        room1 = new Room(1, RoomType.LABORATORY, 5);
        room2 = new Room(2, RoomType.OFFICE, 5);
        room3 = new Room(3, RoomType.LABORATORY, 5);
        room4 = new Room(4, RoomType.STUDY, 5);
        room5 = new Room(5, RoomType.STUDY, 5);


        building.addFloor(floor1);
        building.addFloor(floor2);
        building.addFloor(floor3);
    }

    @Test
    public void noRooms() {
        assertEquals(null, StudyRoomRecommender.recommendStudyRoom(building));
    }

    @Test
    public void noStudyRooms1stFloor() throws InsufficientSpaceException, DuplicateRoomException {
        floor1.addRoom(room1);
        floor1.addRoom(room2);
        floor1.addRoom(room3);
        assertEquals(null, StudyRoomRecommender.recommendStudyRoom(building));
    }

    @Test
    public void noOpenRooms1stFloor() throws InsufficientSpaceException, DuplicateRoomException {
        floor1.addRoom(room1);
        floor1.addRoom(room4);
        floor1.addRoom(room5);
        room1.setMaintenance(true);
        room2.setMaintenance(true);
        room3.setMaintenance(true);
        room4.setMaintenance(true);
        room5.setMaintenance(true);
        assertEquals(null, StudyRoomRecommender.recommendStudyRoom(building));
    }

    @Test
    public void noComfortSensors() throws InsufficientSpaceException, DuplicateRoomException {
        floor1.addRoom(room1);
        floor1.addRoom(room2);
        floor1.addRoom(room4);
        assertEquals(room4, StudyRoomRecommender.recommendStudyRoom(building));
    }

    @Test
    public void multipleNoComfortSensors() throws InsufficientSpaceException, DuplicateRoomException {
        floor1.addRoom(room1);
        floor1.addRoom(room2);
        floor1.addRoom(room4);
        floor1.addRoom(room5);
        assertTrue(Objects.equals(StudyRoomRecommender.recommendStudyRoom(building), room4) ||
                Objects.equals(StudyRoomRecommender.recommendStudyRoom(building), room5));
    }

    @Test
    public void roomWithComfortSensor() throws InsufficientSpaceException, DuplicateRoomException, DuplicateSensorException {
        floor1.addRoom(room1);
        floor1.addRoom(room4);
        floor1.addRoom(room2);
        int[] readings = {30,45,50};
        ComfortSensor testSensor = new TemperatureSensor(readings);
        room4.addSensor((Sensor) testSensor);
        assertEquals(room4, StudyRoomRecommender.recommendStudyRoom(building));
    }

    @Test
    public void SecondFloor() throws InsufficientSpaceException, DuplicateRoomException, DuplicateSensorException {

        Room roomA = new Room(1, RoomType.LABORATORY, 5);
        Room roomB = new Room(2, RoomType.STUDY, 5);
        Room roomC = new Room(3, RoomType.LABORATORY, 5);
        Room roomD = new Room(4, RoomType.STUDY, 5);
        Room roomE = new Room(5, RoomType.STUDY, 5);

        Room roomF = new Room(6, RoomType.LABORATORY, 5);
        Room roomG = new Room(7, RoomType.STUDY, 5);
        Room roomH = new Room(8, RoomType.LABORATORY, 5);
        Room roomI = new Room(9, RoomType.STUDY, 5);
        Room roomJ = new Room(10, RoomType.STUDY, 5);

        Room roomK = new Room(11, RoomType.LABORATORY, 5);
        Room roomL = new Room(12, RoomType.STUDY, 5);
        Room roomM = new Room(13, RoomType.LABORATORY, 5);
        Room roomN = new Room(14, RoomType.STUDY, 5);
        Room roomO = new Room(15, RoomType.STUDY, 5);

        CarbonDioxideSensor sensor1 = new CarbonDioxideSensor(new int[] {278,1000},1,400,200);
        CarbonDioxideSensor sensor2 = new CarbonDioxideSensor(new int[] {300,1000},1,400,200);
        NoiseSensor sensor3 = new NoiseSensor(new int[] {60,1000},1);
        TemperatureSensor sensor4 = new TemperatureSensor(new int[] {29,1000});
        TemperatureSensor sensor5 = new TemperatureSensor(new int[] {25,1000});

        System.out.println(sensor1.getComfortLevel());
        System.out.println(sensor2.getComfortLevel());
        System.out.println(sensor3.getComfortLevel());
        System.out.println(sensor4.getComfortLevel());
        System.out.println(sensor5.getComfortLevel());

        roomB.addSensor(sensor1);
        roomB.addSensor(sensor3);

        roomD.addSensor(sensor1);
        roomD.addSensor(sensor3);

        roomE.addSensor(sensor3);
        roomE.addSensor(sensor4);

        floor1.addRoom(roomA);
        floor1.addRoom(roomB);
        floor1.addRoom(roomC);
        floor1.addRoom(roomD);
        floor1.addRoom(roomE);


        roomG.addSensor(sensor1);
        roomG.addSensor(sensor3);

        roomI.addSensor(sensor2);
        roomI.addSensor(sensor3);

        roomJ.addSensor(sensor3);
        roomJ.addSensor(sensor4);


        floor2.addRoom(roomF);
        floor2.addRoom(roomG);
        floor2.addRoom(roomH);
        floor2.addRoom(roomI);
        floor2.addRoom(roomJ);

        roomL.addSensor(sensor1);
        roomL.addSensor(sensor3);

        roomN.addSensor(sensor2);
        roomN.addSensor(sensor3);

        roomO.addSensor(sensor3);
        roomO.addSensor(sensor5);

        floor3.addRoom(roomK);
        floor3.addRoom(roomL);
        floor3.addRoom(roomM);
        floor3.addRoom(roomN);
        floor3.addRoom(roomO);

         

        System.out.println(StudyRoomRecommender.recommendStudyRoom(building));
    }



}