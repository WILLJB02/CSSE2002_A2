package bms.floor;

import bms.room.Room;
import bms.room.RoomType;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MaintenanceScheduleTest {

    MaintenanceSchedule maintenanceSchedule;
    Room room1, room2, room3, room4, room5, room6;

    @Before
    public void setUp() {
        room1 = new Room(100, RoomType.STUDY,5);
        room2 = new Room(101, RoomType.LABORATORY,5);
        room3 = new Room(102, RoomType.OFFICE,5);
        room4 = new Room(103, RoomType.STUDY,10.89);
        room5 = new Room(104, RoomType.LABORATORY,12.11);
        room6 = new Room(105, RoomType.OFFICE,13.72);
        List<Room> roomOrder = new ArrayList<>();
        roomOrder.add(room1);
        roomOrder.add(room2);
        roomOrder.add(room3);
        roomOrder.add(room4);
        roomOrder.add(room5);
        roomOrder.add(room6);
        maintenanceSchedule = new MaintenanceSchedule(roomOrder);
    }

    @Test
    public void constructor() {
        assertTrue(room1.maintenanceOngoing());
    }




    @Test
    public void getMaintenanceTimeStudyMinArea() {
        assertEquals(5,maintenanceSchedule.getMaintenanceTime(room1));
    }

    @Test
    public void getMaintenanceTimeOfficeMinArea() {
        assertEquals(8,maintenanceSchedule.getMaintenanceTime(room3));
    }

    @Test
    public void getMaintenanceTimeLabMinArea() {
        assertEquals(10,maintenanceSchedule.getMaintenanceTime(room2));
    }

    @Test
    public void getMaintenanceTimeStudy() {
        assertEquals(6,maintenanceSchedule.getMaintenanceTime(room4));
    }

    @Test
    public void getMaintenanceTimeOffice() {
        assertEquals(10,maintenanceSchedule.getMaintenanceTime(room6));
    }

    @Test
    public void getMaintenanceTimeLab() {
        assertEquals(13,maintenanceSchedule.getMaintenanceTime(room5));
    }




    @Test
    public void getCurrentRoomInitial() {
        assertEquals(room1, maintenanceSchedule.getCurrentRoom());
    }

    @Test
    public void getCurrentRoomElapsed1() {
        for (int i = 0; i < 4; i++) {
            maintenanceSchedule.elapseOneMinute();
        }
        assertEquals(room1, maintenanceSchedule.getCurrentRoom());
    }

    @Test
    public void getCurrentRoomElapsed2() {
        for (int i = 0; i < 5; i++) {
            maintenanceSchedule.elapseOneMinute();
        }
        assertEquals(room2, maintenanceSchedule.getCurrentRoom());
    }

    @Test
    public void getCurrentRoomElapsed3() {
        for (int i = 0; i < 51; i++) {
            maintenanceSchedule.elapseOneMinute();
        }
        assertEquals(room6, maintenanceSchedule.getCurrentRoom());
    }

    @Test
    public void getCurrentRoomElapsed4() {
        for (int i = 0; i < 52; i++) {
            maintenanceSchedule.elapseOneMinute();
        }
        assertEquals(room1, maintenanceSchedule.getCurrentRoom());
    }



    @Test
    public void getTimeElapsedCurrentRoomInitial() {
        assertEquals(0,maintenanceSchedule.getTimeElapsedCurrentRoom());
    }

    @Test
    public void getTimeElapsedCurrentRoomElapsed1() {
        for (int i = 0; i < 4; i++) {
            maintenanceSchedule.elapseOneMinute();
        }
        assertEquals(4,maintenanceSchedule.getTimeElapsedCurrentRoom());
    }

    @Test
    public void getTimeElapsedCurrentRoomElapsed2() {
        for (int i = 0; i < 5; i++) {
            maintenanceSchedule.elapseOneMinute();
        }
        assertEquals(0,maintenanceSchedule.getTimeElapsedCurrentRoom());
    }

    @Test
    public void getTimeElapsedCurrentRoomElapsed3() {
        for (int i = 0; i < 7; i++) {
            maintenanceSchedule.elapseOneMinute();
        }
        assertEquals(2,maintenanceSchedule.getTimeElapsedCurrentRoom());
    }


    @Test
    public void elapseOneMinute() {
        for (int i = 0; i < 3; i++) {
            maintenanceSchedule.elapseOneMinute();
        }
        assertEquals(3, maintenanceSchedule.getTimeElapsedCurrentRoom());
        assertEquals(room1, maintenanceSchedule.getCurrentRoom());
        assertTrue(room1.maintenanceOngoing());
    }

    @Test
    public void elapseOneMinuteEvacuate() {
        for (int i = 0; i < 2; i++) {
            maintenanceSchedule.elapseOneMinute();
        }
        room1.setFireDrill(true);
        for (int i = 0; i < 10; i++) {
            maintenanceSchedule.elapseOneMinute();
        }
        assertEquals(2,maintenanceSchedule.getTimeElapsedCurrentRoom());
        assertEquals(room1, maintenanceSchedule.getCurrentRoom());
        assertTrue(room1.maintenanceOngoing());
    }

    @Test
    public void elapseOneMinuteComplete1() {
        for (int i = 0; i < 4; i++) {
            maintenanceSchedule.elapseOneMinute();
        }
        assertEquals(4,maintenanceSchedule.getTimeElapsedCurrentRoom());
        assertEquals(room1, maintenanceSchedule.getCurrentRoom());
        assertTrue(room1.maintenanceOngoing());
        assertFalse(room2.maintenanceOngoing());

        maintenanceSchedule.elapseOneMinute();

        assertEquals(0,maintenanceSchedule.getTimeElapsedCurrentRoom());
        assertEquals(room2, maintenanceSchedule.getCurrentRoom());
        assertTrue(room2.maintenanceOngoing());
        assertFalse(room1.maintenanceOngoing());
    }

    @Test
    public void elapseOneMinuteComplete2() {
        for (int i = 0; i < 51; i++) {
            maintenanceSchedule.elapseOneMinute();
        }
        assertEquals(9,maintenanceSchedule.getTimeElapsedCurrentRoom());
        assertEquals(room6, maintenanceSchedule.getCurrentRoom());
        assertTrue(room6.maintenanceOngoing());
        assertFalse(room1.maintenanceOngoing());

        maintenanceSchedule.elapseOneMinute();

        assertEquals(0,maintenanceSchedule.getTimeElapsedCurrentRoom());
        assertEquals(room1, maintenanceSchedule.getCurrentRoom());
        assertTrue(room1.maintenanceOngoing());
        assertFalse(room6.maintenanceOngoing());
    }




    @Test
    public void skipCurrentMaintenance() {
        for (int i = 0; i < 2; i++) {
            maintenanceSchedule.elapseOneMinute();
        }
        assertEquals(2,maintenanceSchedule.getTimeElapsedCurrentRoom());
        assertEquals(room1, maintenanceSchedule.getCurrentRoom());
        assertTrue(room1.maintenanceOngoing());
        assertFalse(room2.maintenanceOngoing());

        maintenanceSchedule.skipCurrentMaintenance();

        assertEquals(0,maintenanceSchedule.getTimeElapsedCurrentRoom());
        assertEquals(room2, maintenanceSchedule.getCurrentRoom());
        assertTrue(room2.maintenanceOngoing());
        assertFalse(room1.maintenanceOngoing());

    }




    @Test
    public void ToString() {
        for (int i = 0; i < 51; i++) {
            maintenanceSchedule.elapseOneMinute();
        }

        assertEquals("MaintenanceSchedule: currentRoom=#105, currentElapsed=9", maintenanceSchedule.toString());
    }


    @Test
    public void encode() {
        assertEquals("100,101,102,103,104,105", maintenanceSchedule.encode());
    }
}