package bms.floor;
import bms.building.Building;
import bms.exceptions.*;
import bms.floor.MaintenanceSchedule;
import bms.room.Room;
import bms.room.RoomType;
import bms.sensors.*;
import bms.hazardevaluation.RuleBasedHazardEvaluator;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;


public class FloorTest {

    @Test
    public void floorEqual() throws InsufficientSpaceException, DuplicateRoomException, DuplicateSensorException {
        Floor floor1 = new Floor(1, 10.00102,20.00102);
        Floor floor2 = new Floor(1, 10.001,20.001);

        assertEquals(floor1, floor2);
        assertEquals(floor1.hashCode(), floor2.hashCode());


        Room room1a = new Room(1,RoomType.STUDY,20.01);
        Room room1b = new Room(1,RoomType.STUDY,20.01);
        Room room2a = new Room(2,RoomType.OFFICE,20.01);
        Room room2b = new Room(2,RoomType.OFFICE,20.01);
        Room room3a = new Room(3,RoomType.LABORATORY,20.01);
        Room room3b = new Room(3,RoomType.LABORATORY,20.01);

        floor1.addRoom(room1a);
        floor1.addRoom(room2a);
        floor1.addRoom(room3a);

        floor2.addRoom(room2b);
        floor2.addRoom(room3b);
        floor2.addRoom(room1b);

        assertEquals(floor1, floor2);
        assertEquals(floor1.hashCode(), floor2.hashCode());


        floor1 = new Floor(1, 10.00102,20.00102);
        floor2 = new Floor(1, 10.001,20.001);
        floor1.addRoom(room1a);
        floor1.addRoom(room2a);

        floor2.addRoom(room2b);
        floor2.addRoom(room3b);

        assertNotEquals(floor1, floor2);
        assertNotEquals(floor1.hashCode(), floor2.hashCode());


        floor1 = new Floor(1, 10.00102,20.00102);
        floor2 = new Floor(1, 10.001,20.001);
        floor1.addRoom(room1a);
        floor1.addRoom(room2a);
        floor1.addRoom(room3a);

        floor2.addRoom(room2b);
        floor2.addRoom(room3b);

        assertNotEquals(floor1, floor2);
        assertNotEquals(floor1.hashCode(), floor2.hashCode());


        floor1 = new Floor(1, 10.00102,20.00102);
        floor2 = new Floor(2, 10.001,20.001);
        assertNotEquals(floor1, floor2);
        assertNotEquals(floor1.hashCode(), floor2.hashCode());


        floor1 = new Floor(1, 10.004,20.00102);
        floor2 = new Floor(1, 9,20.001);
        assertNotEquals(floor1, floor2);
        assertNotEquals(floor1.hashCode(), floor2.hashCode());

        floor1 = new Floor(1, 10.00102,20.00102);
        floor2 = new Floor(1, 10.001,21);

        assertNotEquals(floor1, floor2);
        assertNotEquals(floor1.hashCode(), floor2.hashCode());

    }

    @Test
    public void createMaintenanceScheduleTest() throws InsufficientSpaceException, DuplicateRoomException, DuplicateFloorException, NoFloorBelowException, FloorTooSmallException {
        Floor floor = new Floor(1,20,20);
        Room gpsRoom101 = new Room(101, RoomType.STUDY, 20);
        Room gpsRoom102 = new Room(102, RoomType.STUDY, 20);
        Room gpsRoom103 = new Room(103, RoomType.STUDY, 15);
        Room gpsRoom104 = new Room(104, RoomType.LABORATORY, 45);
        floor.addRoom(gpsRoom101);
        floor.addRoom(gpsRoom102);
        floor.addRoom(gpsRoom103);
        floor.addRoom(gpsRoom104);
        ArrayList<Room> maintenance = new ArrayList<>();
        maintenance.add(gpsRoom101);
        maintenance.add(gpsRoom102);
        maintenance.add(gpsRoom103);
        maintenance.add(gpsRoom104);

        assertNull(floor.getMaintenanceSchedule());
        floor.createMaintenanceSchedule(maintenance);
        System.out.println(floor.getMaintenanceSchedule());

        ArrayList<Room> maintenance2 = new ArrayList<>();
        maintenance2.add(gpsRoom102);
        maintenance2.add(gpsRoom101);
        maintenance2.add(gpsRoom103);

        assertTrue(gpsRoom101.maintenanceOngoing());
        assertFalse(gpsRoom102.maintenanceOngoing());
        floor.createMaintenanceSchedule(maintenance2);
        System.out.println(floor.getMaintenanceSchedule());
        assertFalse(gpsRoom101.maintenanceOngoing());
        assertTrue(gpsRoom102.maintenanceOngoing());

    }

    @Test (expected = IllegalArgumentException.class)
    public void createMaintenanceScheduleError1() {
        Floor floor = new Floor(1,20,20);
        ArrayList<Room> maintenance2 = null;
        floor.createMaintenanceSchedule(maintenance2);
    }

    @Test (expected = IllegalArgumentException.class)
    public void createMaintenanceScheduleError2() {
        Floor floor = new Floor(1,20,20);
        ArrayList<Room> maintenance2 = null;
        floor.createMaintenanceSchedule(maintenance2);
    }

    @Test (expected = IllegalArgumentException.class)
    public void createMaintenanceScheduleError3() throws InsufficientSpaceException, DuplicateRoomException {
        Floor floor = new Floor(1,20,20);
        Room gpsRoom101 = new Room(101, RoomType.STUDY, 20);
        Room gpsRoom102 = new Room(102, RoomType.STUDY, 20);
        Room gpsRoom103 = new Room(103, RoomType.STUDY, 15);
        Room gpsRoom104 = new Room(104, RoomType.LABORATORY, 45);
        floor.addRoom(gpsRoom101);
        floor.addRoom(gpsRoom102);
        floor.addRoom(gpsRoom103);
        ArrayList<Room> maintenance = new ArrayList<>();
        maintenance.add(gpsRoom101);
        maintenance.add(gpsRoom102);
        maintenance.add(gpsRoom103);
        maintenance.add(gpsRoom104);
        floor.createMaintenanceSchedule(maintenance);
    }

    @Test (expected = IllegalArgumentException.class)
    public void createMaintenanceScheduleError4() throws InsufficientSpaceException, DuplicateRoomException {
        Floor floor = new Floor(1,20,20);
        Room gpsRoom101 = new Room(101, RoomType.STUDY, 20);
        Room gpsRoom102 = new Room(102, RoomType.STUDY, 20);
        Room gpsRoom103 = new Room(103, RoomType.STUDY, 15);
        Room gpsRoom104 = new Room(104, RoomType.LABORATORY, 45);
        floor.addRoom(gpsRoom101);
        floor.addRoom(gpsRoom102);
        floor.addRoom(gpsRoom103);
        ArrayList<Room> maintenance = new ArrayList<>();
        maintenance.add(gpsRoom101);
        maintenance.add(gpsRoom101);
        maintenance.add(gpsRoom103);
        maintenance.add(gpsRoom104);
        floor.createMaintenanceSchedule(maintenance);
    }

    @Test (expected = IllegalArgumentException.class)
    public void createMaintenanceScheduleError5() throws InsufficientSpaceException, DuplicateRoomException {
        Floor floor = new Floor(1,20,20);
        Room gpsRoom101 = new Room(101, RoomType.STUDY, 20);
        Room gpsRoom102 = new Room(102, RoomType.STUDY, 20);
        Room gpsRoom103 = new Room(103, RoomType.STUDY, 15);
        Room gpsRoom104 = new Room(104, RoomType.LABORATORY, 45);
        floor.addRoom(gpsRoom101);
        floor.addRoom(gpsRoom102);
        floor.addRoom(gpsRoom103);
        ArrayList<Room> maintenance = new ArrayList<>();
        maintenance.add(gpsRoom101);
        maintenance.add(gpsRoom102);
        maintenance.add(gpsRoom103);
        maintenance.add(gpsRoom101);
        floor.createMaintenanceSchedule(maintenance);
    }


    @Test
    public void changeDimensions() throws FloorTooSmallException {
         Floor floor = new Floor(1,20,20);
         floor.changeDimensions(10,11);
         assertEquals(10, floor.getWidth(),0.01);
         assertEquals(11, floor.getLength(),0.01);
    }

    @Test (expected = FloorTooSmallException.class)
    public void changeDimensionsError1() throws FloorTooSmallException, InsufficientSpaceException, DuplicateRoomException {
        Floor floor3 = new Floor(3,8,8);

        Room room = new Room(1, RoomType.STUDY,40);

        floor3.addRoom(room);

        floor3.changeDimensions(6,6);
    }

    @Test (expected = IllegalArgumentException.class)
    public void changeDimensionsError2() throws FloorTooSmallException, InsufficientSpaceException, DuplicateRoomException {
        Floor floor3 = new Floor(3,8,8);

        Room room = new Room(1, RoomType.STUDY,40);

        floor3.addRoom(room);

        floor3.changeDimensions(3,6);
    }

    @Test (expected = IllegalArgumentException.class)
    public void changeDimensionsError3() throws FloorTooSmallException, InsufficientSpaceException, DuplicateRoomException {
        Floor floor3 = new Floor(3,8,8);

        Room room = new Room(1, RoomType.STUDY,40);

        floor3.addRoom(room);

        floor3.changeDimensions(6,3);
    }


}