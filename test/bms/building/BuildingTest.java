package bms.building;

import bms.exceptions.*;
import bms.floor.Floor;
import bms.room.Room;
import bms.room.RoomType;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class BuildingTest {

    @Test
    public void equalsandHashCode() throws DuplicateFloorException, NoFloorBelowException, FloorTooSmallException {
        Building building1 = new Building("test");
        Building building2 = new Building("test");
        String test = "test";

        assertEquals(building1,building1);
        assertNotEquals(building1,test);

        assertEquals(building1,building2);
        assertEquals(building1.hashCode(),building2.hashCode());

        Floor floor1 = new Floor(1,10,10);
        Floor floor2 = new Floor(2,10,10);
        Floor floor3 = new Floor(3,10,10);
        Floor floor2New = new Floor(2,8,10);

        building1.addFloor(floor1);
        building1.addFloor(floor2);
        building1.addFloor(floor3);

        building2.addFloor(floor1);
        building2.addFloor(floor2);
        building2.addFloor(floor3);

        assertEquals(building1,building2);
        assertEquals(building1.hashCode(),building2.hashCode());

        building1 = new Building("test");
        building2 = new Building("test");

        building1.addFloor(floor1);
        building1.addFloor(floor2);
        building1.addFloor(floor3);

        building2.addFloor(floor1);
        building2.addFloor(floor2);

        assertNotEquals(building1,building2);
        assertNotEquals(building1.hashCode(),building2.hashCode());

        building1 = new Building("test");
        building2 = new Building("test");

        building1.addFloor(floor1);
        building1.addFloor(floor2New);

        building2.addFloor(floor1);
        building2.addFloor(floor2);

        assertNotEquals(building1,building2);
        assertNotEquals(building1.hashCode(),building2.hashCode());
    }

    @Test
    public void encodeTest() throws IOException, FileFormatException {
        List<Building> loadedBuildings = BuildingInitialiser.loadBuildings("saves/uqstlucia.txt");
        Building gps = loadedBuildings.get(0);
        assertEquals("General Purpose South" + System.lineSeparator() +
                "5" + System.lineSeparator() +
                "1:10.00:10.00:4:101,103,102,104" + System.lineSeparator() +
                "101:STUDY:20.00:0" + System.lineSeparator() +
                "102:STUDY:20.00:1" + System.lineSeparator() +
                "OccupancySensor:13,24,28,15,6:4:30" + System.lineSeparator() +
                "103:STUDY:15.00:0" + System.lineSeparator() +
                "104:LABORATORY:45.00:1" + System.lineSeparator() +
                "CarbonDioxideSensor:690,740:5:700:150" + System.lineSeparator() +
                "2:10.00:10.00:3" + System.lineSeparator() +
                "201:OFFICE:50.00:2:RuleBased" +  System.lineSeparator() +
                "NoiseSensor:55,62,69,63:3" + System.lineSeparator() +
                "OccupancySensor:32,35,26,4,3,2,6,16,17,22,28,29:2:40" + System.lineSeparator() +
                "202:OFFICE:30.00:0" + System.lineSeparator() +
                "203:STUDY:10.00:1" + System.lineSeparator() +
                "TemperatureSensor:28,29,26,24,25,26" + System.lineSeparator() +
                "3:10.00:8.00:3" + System.lineSeparator() +
                "301:STUDY:30.00:1" + System.lineSeparator() +
                "OccupancySensor:15,17,12,8,11:4:30" + System.lineSeparator() +
                "302:LABORATORY:25.00:1:RuleBased" + System.lineSeparator() +
                "TemperatureSensor:25,26,24" + System.lineSeparator() +
                "303:LABORATORY:25.00:1" + System.lineSeparator() +
                "TemperatureSensor:24,21" + System.lineSeparator() +
                "4:10.00:5.00:3:403,402,401" + System.lineSeparator() +
                "401:OFFICE:20.00:0" + System.lineSeparator() +
                "402:OFFICE:10.00:0" + System.lineSeparator() +
                "403:OFFICE:10.00:0" + System.lineSeparator() +
                "5:8.00:5.00:1" + System.lineSeparator() +
                "501:LABORATORY:30.00:2:WeightingBased" + System.lineSeparator() +
                "OccupancySensor:15,12,2,0:1:20@75" + System.lineSeparator() +
                "TemperatureSensor:25,34,61,85@25"
                , gps.encode());
    }


    @Test
    public void renovateFLoor() throws DuplicateFloorException, NoFloorBelowException, FloorTooSmallException {
        Building building1 = new Building("test");

        Floor floor1 = new Floor(1,10,10);
        Floor floor2 = new Floor(2,10,10);
        Floor floor3 = new Floor(3,6,6);

        building1.addFloor(floor1);
        building1.addFloor(floor2);
        building1.addFloor(floor3);

        building1.renovateFloor(2,8,7);
        assertEquals(8,floor2.getWidth(),0.01);
        assertEquals(7,floor2.getLength(),0.01);
    }

    @Test(expected = FloorTooSmallException.class)
    public void belowwidth() throws FloorTooSmallException, NoFloorBelowException, DuplicateFloorException {
        Building building1 = new Building("test");

        Floor floor1 = new Floor(1,10,10);
        Floor floor2 = new Floor(2,10,10);
        Floor floor3 = new Floor(3,6,6);

        building1.addFloor(floor1);
        building1.addFloor(floor2);
        building1.addFloor(floor3);

        building1.renovateFloor(2,11,7);
    }

    @Test(expected = FloorTooSmallException.class)
    public void belowlenthh() throws FloorTooSmallException, NoFloorBelowException, DuplicateFloorException {
        Building building1 = new Building("test");

        Floor floor1 = new Floor(1,10,10);
        Floor floor2 = new Floor(2,10,10);
        Floor floor3 = new Floor(3,6,6);

        building1.addFloor(floor1);
        building1.addFloor(floor2);
        building1.addFloor(floor3);

        building1.renovateFloor(2,8,11);
    }

    @Test(expected = FloorTooSmallException.class)
    public void aboveWidth() throws FloorTooSmallException, NoFloorBelowException, DuplicateFloorException {
        Building building1 = new Building("test");

        Floor floor1 = new Floor(1,10,10);
        Floor floor2 = new Floor(2,10,10);
        Floor floor3 = new Floor(3,6.5,6.5);

        building1.addFloor(floor1);
        building1.addFloor(floor2);
        building1.addFloor(floor3);

        building1.renovateFloor(2,6,8);
    }

    @Test(expected = FloorTooSmallException.class)
    public void aboveLength() throws FloorTooSmallException, NoFloorBelowException, DuplicateFloorException {
        Building building1 = new Building("test");

        Floor floor1 = new Floor(1,10,10);
        Floor floor2 = new Floor(2,10,10);
        Floor floor3 = new Floor(3,6.5,6.5);

        building1.addFloor(floor1);
        building1.addFloor(floor2);
        building1.addFloor(floor3);

        building1.renovateFloor(2,8,6);
    }

    @Test(expected = FloorTooSmallException.class)
    public void roomArea() throws FloorTooSmallException, NoFloorBelowException, DuplicateFloorException, InsufficientSpaceException, DuplicateRoomException {
        Building building1 = new Building("test");

        Floor floor1 = new Floor(1,10,10);
        Floor floor2 = new Floor(2,10,10);
        Floor floor3 = new Floor(3,8,8);

        Room room = new Room(1, RoomType.STUDY,40);

        floor3.addRoom(room);

        building1.addFloor(floor1);
        building1.addFloor(floor2);
        building1.addFloor(floor3);

        building1.renovateFloor(3,6,6);

    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalArgument1() throws DuplicateFloorException, NoFloorBelowException, FloorTooSmallException {
        Building building1 = new Building("test");
        Floor floor1 = new Floor(1,10,10);
        building1.addFloor(floor1);
        building1.renovateFloor(2,6,6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalArgument2() throws DuplicateFloorException, NoFloorBelowException, FloorTooSmallException {
        Building building1 = new Building("test");
        Floor floor1 = new Floor(1,10,10);
        building1.addFloor(floor1);
        building1.renovateFloor(1,2,6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalArgument3() throws DuplicateFloorException, NoFloorBelowException, FloorTooSmallException {
        Building building1 = new Building("test");
        Floor floor1 = new Floor(1,10,10);
        building1.addFloor(floor1);
        building1.renovateFloor(1,6,2);
    }




}