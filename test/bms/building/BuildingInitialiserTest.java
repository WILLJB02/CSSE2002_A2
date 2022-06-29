package bms.building;

import bms.exceptions.*;
import bms.floor.Floor;
import bms.hazardevaluation.WeightingBasedHazardEvaluator;
import bms.room.Room;
import bms.room.RoomType;
import bms.sensors.*;
import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BuildingInitialiserTest {

    @Test
    public void loadFileTest() throws
            DuplicateFloorException, NoFloorBelowException,
            FloorTooSmallException, InsufficientSpaceException,
            DuplicateRoomException, IOException, FileFormatException,
            DuplicateSensorException {


        // got to finish off
        List<Building> loadedBuildings = BuildingInitialiser.loadBuildings("saves/uqstlucia.txt");
        Building gps = loadedBuildings.get(0);
        Building fsb = loadedBuildings.get(1);
        Building alb = loadedBuildings.get(2);


        Building generalPurposeSouth = new Building("General Purpose South");

        Floor gps1 = new Floor(1, 10, 10);
        Floor gps2 = new Floor(2, 10, 10);
        Floor gps3 = new Floor(3, 10, 8);
        Floor gps4 = new Floor(4, 10, 5);
        Floor gps5 = new Floor(5, 8, 5);

        Room gpsRoom101 = new Room(101, RoomType.STUDY, 20);
        Room gpsRoom102 = new Room(102, RoomType.STUDY, 20);
        Room gpsRoom103 = new Room(103, RoomType.STUDY, 15);
        Room gpsRoom104 = new Room(104, RoomType.LABORATORY, 45);
        Room gpsRoom201 = new Room(201, RoomType.OFFICE, 50);
        Room gpsRoom202 = new Room(202, RoomType.OFFICE, 30);
        Room gpsRoom203 = new Room(203, RoomType.STUDY, 10);
        Room gpsRoom301 = new Room(301, RoomType.STUDY, 30);
        Room gpsRoom302 = new Room(302, RoomType.LABORATORY, 25);
        Room gpsRoom303 = new Room(303, RoomType.LABORATORY, 25);
        Room gpsRoom401 = new Room(401, RoomType.OFFICE, 20);
        Room gpsRoom402 = new Room(402, RoomType.OFFICE, 10);
        Room gpsRoom403 = new Room(403, RoomType.OFFICE, 10);
        Room gpsRoom501 = new Room(501, RoomType.LABORATORY, 30);


        Sensor occupancy102 = new OccupancySensor(new int[]{13, 24, 28, 15, 6}, 4, 30);
        Sensor co2104 = new CarbonDioxideSensor(new int[]{690, 740}, 5, 700, 150);
        Sensor noise201 = new NoiseSensor(new int[]{55, 62, 69, 63}, 3);
        Sensor occupancy201 = new OccupancySensor(new int[]{32, 35, 26, 4, 3, 2, 6, 16, 17, 22, 28, 29}, 2, 40);
        Sensor temp203 = new TemperatureSensor(new int[]{28, 29, 26, 24, 25, 26});
        Sensor occupancy301 = new OccupancySensor(new int[]{15, 17, 12, 8, 11}, 4, 30);
        Sensor temp302 = new TemperatureSensor(new int[]{25, 26, 24});
        Sensor temp303 = new TemperatureSensor(new int[]{24, 21});
        Sensor occupancy501 = new OccupancySensor(new int[]{15, 12, 2, 0}, 1, 20);
        Sensor temp501 = new TemperatureSensor(new int[]{25, 34, 61, 85});

        generalPurposeSouth.addFloor(gps1);
        generalPurposeSouth.addFloor(gps2);
        generalPurposeSouth.addFloor(gps3);
        generalPurposeSouth.addFloor(gps4);
        generalPurposeSouth.addFloor(gps5);

        gps1.addRoom(gpsRoom101);
        gps1.addRoom(gpsRoom102);
        gps1.addRoom(gpsRoom103);
        gps1.addRoom(gpsRoom104);
        gps2.addRoom(gpsRoom201);
        gps2.addRoom(gpsRoom202);
        gps2.addRoom(gpsRoom203);
        gps3.addRoom(gpsRoom301);
        gps3.addRoom(gpsRoom302);
        gps3.addRoom(gpsRoom303);
        gps4.addRoom(gpsRoom401);
        gps4.addRoom(gpsRoom402);
        gps4.addRoom(gpsRoom403);
        gps5.addRoom(gpsRoom501);

        gpsRoom102.addSensor(occupancy102);
        gpsRoom104.addSensor(co2104);
        gpsRoom201.addSensor(noise201);
        gpsRoom201.addSensor(occupancy201);
        gpsRoom203.addSensor(temp203);
        gpsRoom301.addSensor(occupancy301);
        gpsRoom302.addSensor(temp302);
        gpsRoom303.addSensor(temp303);
        gpsRoom501.addSensor(temp501);
        gpsRoom501.addSensor(occupancy501);


        Building forganSmithBuilding = new Building("Forgan Smith Building");

        Floor fsb1 = new Floor(1, 8.5, 40);

        Room fsbRoom1 = new Room(101, RoomType.STUDY, 23.8);
        Room fsbRoom2 = new Room(102, RoomType.STUDY, 20);
        Room fsbRoom3 = new Room(103, RoomType.STUDY, 28.5);
        Room fsbRoom4 = new Room(104, RoomType.OFFICE, 35);
        Room fsbRoom5 = new Room(105, RoomType.STUDY, 20);
        Room fsbRoom6 = new Room(106, RoomType.STUDY, 25.5);
        Room fsbRoom7 = new Room(107, RoomType.OFFICE, 40);
        Room fsbRoom8 = new Room(108, RoomType.STUDY, 20);
        Room fsbRoom9 = new Room(109, RoomType.STUDY, 21.2);
        Room fsbRoom10 = new Room(110, RoomType.STUDY, 20);

        Sensor noise103 = new NoiseSensor(new int[]{52, 42, 53, 56}, 2);
        Sensor c02107 = new CarbonDioxideSensor(new int[]{745, 1320, 2782, 3216, 5043, 3528, 1970}, 3, 700, 300);
        Sensor occupancy107 = new OccupancySensor(new int[]{11, 13, 13, 13, 10}, 3, 20);

        forganSmithBuilding.addFloor(fsb1);

        fsb1.addRoom(fsbRoom1);
        fsb1.addRoom(fsbRoom2);
        fsb1.addRoom(fsbRoom3);
        fsb1.addRoom(fsbRoom4);
        fsb1.addRoom(fsbRoom5);
        fsb1.addRoom(fsbRoom6);
        fsb1.addRoom(fsbRoom7);
        fsb1.addRoom(fsbRoom8);
        fsb1.addRoom(fsbRoom9);
        fsb1.addRoom(fsbRoom10);

        fsbRoom3.addSensor(noise103);
        fsbRoom7.addSensor(c02107);
        fsbRoom7.addSensor(occupancy107);


        Building andrewLiverisBuilding = new Building("Andrew N. Liveris Building");

        Floor alb1 = new Floor(1, 15, 30);

        andrewLiverisBuilding.addFloor(alb1);
        assertEquals(generalPurposeSouth, gps);
        assertEquals(forganSmithBuilding, fsb);
        assertEquals(andrewLiverisBuilding, alb);


        //checking maintenance schedules
        assertEquals("101,103,102,104", gps.getFloorByNumber(1).getMaintenanceSchedule().encode());

        assertEquals("403,402,401", gps.getFloorByNumber(4).getMaintenanceSchedule().encode());
        assertEquals("104,107,109,107", fsb.getFloorByNumber(1).getMaintenanceSchedule().encode());

        //checking checking hazard evaluators TODO finish off
        assertEquals("RuleBased", gps.getFloorByNumber(2).getRoomByNumber(201).getHazardEvaluator().toString());
        assertEquals("RuleBased", gps.getFloorByNumber(3).getRoomByNumber(302).getHazardEvaluator().toString());
        assertEquals("RuleBased", fsb.getFloorByNumber(1).getRoomByNumber(107).getHazardEvaluator().toString());
        assertEquals("WeightingBased", gps.getFloorByNumber(5).getRoomByNumber(501).getHazardEvaluator().toString());
        ArrayList<Integer> weightings = new ArrayList<>();
        weightings.add(75);
        weightings.add(25);
        WeightingBasedHazardEvaluator weightingBasedHazardEvaluator = (WeightingBasedHazardEvaluator) gps.getFloorByNumber(5).getRoomByNumber(501).getHazardEvaluator();
        assertEquals(weightings, weightingBasedHazardEvaluator.getWeightings());
    }

    @Test(expected = FileFormatException.class)
    public void C02IdealValue() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/C02IdealValue");
    }

    @Test(expected = FileFormatException.class)
    public void C02VarLimit() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/C02VarLimit");
    }

    @Test(expected = FileFormatException.class)
    public void CO2Error() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/CO2Error");
    }

    @Test(expected = FileFormatException.class)
    public void duplicateFloor() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/duplicateFloor");
    }

    @Test(expected = FileFormatException.class)
    public void DuplicateRoom() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/DuplicateRoom");
    }

    @Test(expected = FileFormatException.class)
    public void duplicateSensor() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/duplicateSensor");
    }

    @Test(expected = FileFormatException.class)
    public void emptyLine() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/emptyLine");
    }

    @Test(expected = FileFormatException.class)
    public void extraFloor() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/extraFloor");
    }

    @Test(expected = FileFormatException.class)
    public void extraRoom() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/extraRoom");
    }

    @Test(expected = FileFormatException.class)
    public void extraSensor() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/extraSensor");
    }

    @Test(expected = FileFormatException.class)
    public void floorLengthMin() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/floorLengthMin");
    }

    @Test(expected = FileFormatException.class)
    public void floorNumberZero() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/floorNumberZero");
    }

    @Test(expected = FileFormatException.class)
    public void floorToLarge() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/floorToLarge");
    }

    @Test(expected = FileFormatException.class)
    public void floorWidthmin() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/floorWidthmin");
    }

    @Test(expected = FileFormatException.class)
    public void insufficientFloorSpace() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/insufficientFloorSpace");
    }

    @Test(expected = FileFormatException.class)
    public void invalidHazardEval() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/invalidHazardEval");
    }

    @Test(expected = FileFormatException.class)
    public void invalidHazardWeightings() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/invalidHazardWeightings");
    }

    @Test(expected = FileFormatException.class)
    public void invalidRoomType() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/invalidRoomType");
    }

    @Test(expected = FileFormatException.class)
    public void invalidSensorType() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/invalidSensorType");
    }

    @Test(expected = FileFormatException.class)
    public void lessColons() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/lessColons");
    }

    @Test(expected = FileFormatException.class)
    public void maintenanceTwice() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/maintenanceTwice");
    }

    @Test(expected = FileFormatException.class)
    public void minArea() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/minArea");
    }

    @Test(expected = FileFormatException.class)
    public void moreColons() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/moreColons");
    }

    @Test(expected = FileFormatException.class)
    public void noFloorBelow() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/noFloorBelow");
    }

    @Test(expected = FileFormatException.class)
    public void notEnoughFloors() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/notEnoughFloors");
    }

    @Test(expected = FileFormatException.class)
    public void notEnoughRooms() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/notEnoughRooms");
    }

    @Test(expected = FileFormatException.class)
    public void notEnoughSensors() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/notEnoughSensors");
    }

    @Test(expected = FileFormatException.class)
    public void numFloorsLessZero() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/numFloorsLessZero");
    }

    @Test(expected = FileFormatException.class)
    public void numRoomsLessZero() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/numRoomsLessZero");
    }

    @Test(expected = FileFormatException.class)
    public void numSensorsLessZero() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/numSensorsLessZero");
    }

    @Test(expected = FileFormatException.class)
    public void OccupancyCapcity() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/OccupancyCapcity");
    }

    @Test(expected = FileFormatException.class)
    public void parseError() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/parseError");
    }

    @Test(expected = FileFormatException.class)
    public void roomNotInMaintenance() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/roomNotInMaintenance");
    }

    @Test(expected = FileFormatException.class)
    public void sensorReadingsLessZero() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/sensorReadingsLessZero");
    }

    @Test(expected = FileFormatException.class)
    public void updateFreqError() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/updateFreqError");
    }



}













