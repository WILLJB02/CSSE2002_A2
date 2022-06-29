import bms.building.Building;
import bms.building.BuildingInitialiser;
import bms.exceptions.DuplicateSensorException;
import bms.exceptions.FileFormatException;
import bms.floor.Floor;
import bms.floor.MaintenanceSchedule;
import bms.hazardevaluation.WeightingBasedHazardEvaluator;
import bms.room.Room;
import bms.room.RoomType;
import bms.sensors.*;
import bms.hazardevaluation.RuleBasedHazardEvaluator;
import org.junit.Test;

import java.awt.*;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class testing {
    @Test
    public void Test() throws DuplicateSensorException {
        int[] readings = {1000, 75, 82};
        int[] readings2 = {2000, 75, 82};
        int[] readings3 = {100, 75, 82};
        CarbonDioxideSensor test = new CarbonDioxideSensor(readings,2, 800,100);
        CarbonDioxideSensor test2 = new CarbonDioxideSensor(readings2,2, 800,100);
        OccupancySensor test3 = new OccupancySensor(readings3, 2, 500);
        System.out.println(test.equals(test2));
        System.out.println(test.encode());
        System.out.println(test.getHazardLevel());
        System.out.println(test2.getHazardLevel());
        System.out.println(test3.getHazardLevel());

        ArrayList<HazardSensor> b = new ArrayList<>();
        b.add(test);
        b.add(test3);



        RuleBasedHazardEvaluator a = new RuleBasedHazardEvaluator(b);
        System.out.println(a.evaluateHazardLevel());

        Room room = new Room(1, RoomType.STUDY,25.1234);


        room.addSensor(test);
        room.addSensor(test3);
        room.setHazardEvaluator(a);
        System.out.println(room.getHazardEvaluator());
        System.out.println(room.encode());
    }

    @Test
    public void WeightingBasedEvlautor() throws DuplicateSensorException, IOException, FileFormatException {
        int[] readings1 = {40,45,50};
        HazardSensor occupancySensor = new OccupancySensor(readings1,1,100);
        int[] readings2 = {64,45,50};
        HazardSensor noise = new NoiseSensor(readings2,1);
        int[] readings3 = {30,45,50};
        HazardSensor temp = new TemperatureSensor(readings3);

        System.out.println(occupancySensor.getHazardLevel());
        System.out.println(noise.getHazardLevel());
        System.out.println(temp.getHazardLevel());

        Map<HazardSensor,Integer> weightings = new HashMap<>();
        weightings.put(occupancySensor, 20);
        weightings.put(noise,30);
        weightings.put(temp,50);

        WeightingBasedHazardEvaluator hazard = new WeightingBasedHazardEvaluator(weightings);
        System.out.println(hazard.evaluateHazardLevel());
    }

    @Test
    public void Test2() throws DuplicateSensorException, IOException, FileFormatException {
        BuildingInitialiser buildingInitialiser = new BuildingInitialiser();
        List<Building> buildings = BuildingInitialiser.loadBuildings("uqstlucia.txt");
        for (Building building: buildings) {
            System.out.println(building.toString());
            for (Floor floor : building.getFloors()) {
                System.out.println("\t" + floor.toString());
                for (Room room : floor.getRooms()) {
                    System.out.println("\t\t" + room.toString());
                    for (Sensor sensor : room.getSensors()) {
                        System.out.println("\t\t\t" + sensor.toString());
                    }
                }
            }
        }

        WeightingBasedHazardEvaluator w = (WeightingBasedHazardEvaluator) buildings.get(0).getFloors().get(4).getRooms().get(0).getHazardEvaluator();
        System.out.println(w.getWeightings());



        Room room1 = new Room(1,RoomType.STUDY, 20.1);
        Room room2 = new Room(2,RoomType.OFFICE, 25.6);
        Room room3 = new Room(3,RoomType.LABORATORY, 18.1);
        ArrayList<Room> roomOrder = new ArrayList<Room>();
        roomOrder.add(room1);
        roomOrder.add(room2);
        roomOrder.add(room3);
        MaintenanceSchedule maintenanceSchedule =  new MaintenanceSchedule(roomOrder);

        System.out.println(maintenanceSchedule.getMaintenanceTime(room1));
        System.out.println(maintenanceSchedule.getMaintenanceTime(room2));
        System.out.println(maintenanceSchedule.getMaintenanceTime(room3));

        System.out.println(room1.maintenanceOngoing());
        System.out.println(room2.maintenanceOngoing());
        System.out.println(room3.maintenanceOngoing());

        System.out.println(maintenanceSchedule.getCurrentRoom());
        System.out.println(maintenanceSchedule.getTimeElapsedCurrentRoom());
        for (int i = 0;  i < 7; i++) {
            maintenanceSchedule.elapseOneMinute();
            }
        for (int i = 0;  i < 2; i++) {
                maintenanceSchedule.elapseOneMinute();
            }

        System.out.println(maintenanceSchedule.getCurrentRoom());
        System.out.println(maintenanceSchedule.getTimeElapsedCurrentRoom());

        System.out.println(room1.maintenanceOngoing());
        System.out.println(room2.maintenanceOngoing());
        System.out.println(room3.maintenanceOngoing());
        for (int i = 0;  i < 28; i++) {
            maintenanceSchedule.elapseOneMinute();
        }
        System.out.println(maintenanceSchedule.getCurrentRoom());
        System.out.println(maintenanceSchedule.getTimeElapsedCurrentRoom());
        System.out.println(room1.maintenanceOngoing());
        System.out.println(room2.maintenanceOngoing());
        System.out.println(room3.maintenanceOngoing());
    }

    @Test
    public void Test3() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/quicksave.txt").get(0).encode();
    }
}
