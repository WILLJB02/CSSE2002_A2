package bms.building;

import bms.exceptions.*;
import bms.floor.Floor;
import bms.hazardevaluation.RuleBasedHazardEvaluator;
import bms.hazardevaluation.WeightingBasedHazardEvaluator;
import bms.room.Room;
import bms.room.RoomType;
import bms.sensors.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Class which manages the initialisation and saving of buildings by reading
 * and writing data to a file.
 */
public class BuildingInitialiser {
    /**
     * Loads a list of buildings from a save file with the given filename.
     * Save files have the following structure. Square brackets indicate that
     * the data inside them is optional. See the demo save file for an example
     * (uqstlucia.txt).
     * <br>
     * <br>
     *  buildingName
     *  <p>
     *  numFloors
     *  <p>
     *  floorNumber:floorWidth:floorLength:numRooms[:rooms,in,maintenance,
     *          schedule]
     *  <p>
     *  roomNumber:ROOM_TYPE:roomArea:numSensors[:hazardEvalType]
     *  <p>
     *  sensorType:list,of,sensor,readings[:sensorAttributes...][@weighting]
     *  <p>
     *  ...       (more sensors)
     *  <p>
     *  ...     (more rooms)
     *  <p>
     *  ...   (more floors)
     *  <p>
     *  ... (more buildings)
     *  <p>
     *  <br>
     * A save file is invalid if any of the following conditions are true:
     *
     * <ul>
     *      <li> The number of floors specified for a building is not equal to
     *      the actual number of floors read from the file for that building.
     *      <li> The number of rooms specified for a floor is not equal to the
     *      actual number of rooms read from the file for that floor.
     *      <li> The number of sensors specified for a room is not equal to
     *      the number of sensors read from the file for that room.
     *      <li> A floor's maintenance schedule contains a room number that does
     *      not correspond to a room with the same number on that floor.
     *      <li> A floor's maintenance schedule is invalid according to
     *      Floor.createMaintenanceSchedule(List).
     *      <li> A building has two floors with the same floor number
     *      (a duplicate floor).
     *      <li> A floor's length or width is less than the minimum length or
     *      width, respectively, for a floor.
     *      <li> A floor has no floor below to support the floor.
     *      <li> A floor is too large to fit on top of the floor below.
     *      <li> A floor has two rooms with the same room number (a duplicate
     *      room).
     *      <li> A room cannot be added to its floor because there is
     *      insufficient unoccupied space on the floor.
     *      <li> A room's type is not one of the types listed in RoomType.
     *      Room types are case-sensitive.
     *      <li> A room's area is less than the minimum area for a room.
     *      <li> A room's hazard evaluator type is invalid.
     *      <li> A room's weighting-based hazard evaluator weightings are
     *      invalid according to WeightingBasedHazardEvaluator(Map).
     *      <li> A room has two sensors of the same type (a duplicate sensor).
     *      <li> A sensor's type does not match one of the concrete sensor
     *      types (e.g. NoiseSensor, OccupancySensor, ...).
     *      <li> A sensor's update frequency does not meet the restrictions
     *      outlined in TimedSensor(int[], int).
     *      <li> A carbon dioxide sensor's variation limit is greater than its
     *      ideal CO2 value.
     *      <li> Any numeric value that should be non-negative is less than
     *      zero. This includes:
     *      <ul>
     *          <li> the number of floors in a building
     *          <li> the number of rooms on a floor
     *          <li> the number of sensors in room
     *          <li> sensor readings
     *          <li> occupancy sensor capacity
     *          <li> carbon dioxide sensor ideal CO2 level
     *          <li> carbon dioxide sensor variation limit
     *      </ul>
     *      <li> Any numeric value that should be positive is less than or
     *      equal to zero. This includes:
     *      <ul>
     *         <li>floor numbers
     *      </ul>
     *      <li> floor numbers
     *      <li> The colon-delimited format is violated, i.e. there are
     *      more/fewer colons than expected.
     *      <li> Any numeric value fails to be parsed.
     *      <li> An empty line occurs where a non-empty line is expected.
     * </ul>
     *
     * @param filename path of the file from which to load a list of buildings
     * @return a list containing all the buildings loaded from the file
     * @throws IOException if an IOException is encountered when calling any
     *      IO methods
     * @throws FileFormatException  if the file format of the given file is
     *      invalid according to the rules above
     */
    public static List<Building> loadBuildings(String filename)
            throws IOException, FileFormatException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        List<Building> loadedBuildings = new ArrayList<>();
        try {
            boolean buildingsRemaining = true;
            String buildingName = reader.readLine();
            // loads 1 building per loop
            while (buildingsRemaining) {
                Building building = new Building(buildingName);
                int numberFloors = Integer.parseInt(reader.readLine());
                if (numberFloors < 0) {
                    throw new FileFormatException();
                }
                // adds 1 floor to building per loop
                for (int i = 0; i < numberFloors; i++) {
                    Floor floor = loadFloor(reader);
                    building.addFloor(floor);
                }
                loadedBuildings.add(building);
                buildingName = reader.readLine();
                // checks if another building is present in file
                if (buildingName == null) {
                    buildingsRemaining = false;
                }
            }
        } catch (IOException | NoFloorBelowException | DuplicateFloorException
                | FloorTooSmallException | InsufficientSpaceException |
                DuplicateRoomException | DuplicateSensorException |
                IllegalArgumentException e) {
            throw new FileFormatException();
        }
        return loadedBuildings;
    }

    /**
     * Method which converts string array to an integer array.
     * @param stringArray string array that is to be converted.
     * @return an array of integers.
     */
    private static int[] convertStringArray (String[] stringArray) {
        int[] arr = new int[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            arr[i] = Integer.parseInt(stringArray[i]);
        }
        return arr;
    }

    /**
     * Constructs a floor object from the provided encoded file.
     * @param reader Buffered reader that is being used to read provided file.
     * @return floor object corresponding to provided encoded information
     * @throws IOException if an IOException is encountered when calling any
     * IO methods
     * @throws FileFormatException if the file format of the given file is
     * invalid
     * @throws InsufficientSpaceException the file attempts to add a room when
     * there is not enough space on the floor
     * @throws DuplicateRoomException the file attempts to load a duplicate
     * floor
     * @throws DuplicateSensorException the file attempts to load a duplicate
     * sensor
     */
    private static Floor loadFloor(BufferedReader reader) throws
            IOException, FileFormatException, InsufficientSpaceException,
            DuplicateRoomException, DuplicateSensorException {
        // Initialising Floor
        String[] floorFileLine = reader.readLine().split(":", 5);
        int floorNumber = Integer.parseInt(floorFileLine[0]);
        double length = Double.parseDouble(floorFileLine[1]);
        double width = Double.parseDouble(floorFileLine[2]);
        Floor floor = new Floor(floorNumber, length, width);

        // checking for maintenance schedule
        List<Integer> maintenanceSchedule = null;
        if (floorFileLine.length == 5) {
            String[] maintenanceRooms = floorFileLine[4].split(",");
            maintenanceSchedule = new ArrayList<>();
            for (String room : maintenanceRooms) {
                maintenanceSchedule.add(Integer.parseInt(room));
            }
        }

        // adding floors to room from file
        int numberRooms = Integer.parseInt(floorFileLine[3]);
        if (numberRooms < 0) {
            throw new FileFormatException();
        }
        for (int j = 0; j < numberRooms; j++) {
            Room room = loadRoom(reader);
            floor.addRoom(room);
        }

        // adding maintenance schedule to floor if it exists
        if (maintenanceSchedule != null) {
            List<Room> maintenanceRooms = new ArrayList<>();
            for (Integer roomNumber : maintenanceSchedule) {
                if (floor.getRoomByNumber(roomNumber) == null) {
                    throw new FileFormatException();
                } else {
                    maintenanceRooms.add(floor.getRoomByNumber(
                            roomNumber));
                }
            }
            floor.createMaintenanceSchedule(maintenanceRooms);
        }
        return floor;
    }

    /**
     * Constructs a room object from the provided encoded file.
     * @param reader buffered reader that is being used to read provided file.
     * @return room object corresponding to provided encoded information
     * @throws IOException if an IOException is encountered when calling any
     * IO methods
     * @throws FileFormatException if the file format of the given file is
     * invalid
     * @throws DuplicateSensorException the file attempts to load a duplicate
     * sensor
     */
    private static Room loadRoom(BufferedReader reader) throws IOException,
            FileFormatException, DuplicateSensorException {

        // initialising Room
        String[] roomLine = reader.readLine().split(":",5);
        int roomNumber = Integer.parseInt(roomLine[0]);
        RoomType roomType = RoomType.valueOf(roomLine[1]);
        double roomArea = Double.parseDouble(roomLine[2]);
        Room room = new Room(roomNumber, roomType, roomArea);

        // checking for Hazard Evaluator
        String hazardEvalType = null;
        if (roomLine.length > 4) {
            hazardEvalType = roomLine[4];
            if (hazardEvalType.equals("")) {
                throw new FileFormatException();
            }
        }

        // adding sensors to room
        int numberSensors = Integer.parseInt(roomLine[3]);
        Map<HazardSensor,Integer> weightingBasedHazardEvaluator =
                new HashMap<>();
        List<HazardSensor> ruleBasedHazardEvaluator = new ArrayList<>();
        if (numberSensors < 0) {
            throw new FileFormatException();
        }
        for (int k = 0; k < numberSensors; k++) {
            Map<String, Object> sensorMap  = loadSensor(reader, hazardEvalType);
            TimedSensor timedSensor = (TimedSensor) sensorMap.get("Sensor");
            //generating hazard evaluator if it exists
            if (hazardEvalType != null &&
                    hazardEvalType.equals("WeightingBased")) {
                int weighting = (int) sensorMap.get("Weighting");
                weightingBasedHazardEvaluator.put((HazardSensor) timedSensor,
                        weighting);
            } else if (hazardEvalType != null &&
                    hazardEvalType.equals("RuleBased")) {
                ruleBasedHazardEvaluator.add((HazardSensor) timedSensor);
            } else if (hazardEvalType != null) {
                throw new FileFormatException();
            }
            room.addSensor(timedSensor);
        }

        // adding hazard evaluator to room if it exists
        if (hazardEvalType != null &&
                hazardEvalType.equals("WeightingBased")) {
            room.setHazardEvaluator(new WeightingBasedHazardEvaluator
                    (weightingBasedHazardEvaluator));
        } else if (hazardEvalType != null &&
                hazardEvalType.equals("RuleBased")) {
            room.setHazardEvaluator(new RuleBasedHazardEvaluator
                    (ruleBasedHazardEvaluator));
        }
        return room;
    }

    /**
     * Constructs a sensor object from the provided encoded file.
     * @param reader buffered reader that is being used to read provided file.
     * @param hazardEvalType string which denotes weather hazard evaluator
     *                       exists
     * @return sensor object corresponding to provided encoded information
     * @throws FileFormatException if the file format of the given file is
     * invalid
     * @throws IOException if an IOException is encountered when calling any
     * IO methods
     */
    private static Map<String, Object> loadSensor(BufferedReader reader,
                                                  String hazardEvalType)
            throws FileFormatException, IOException {

        TreeMap<String, Object> sensorOutput = new TreeMap<>();
        String sensorFileLine = reader.readLine();
        String sensorInformation;
        int weighting;

        // splitting file line if WeightingBased Evaluator exits
        if (hazardEvalType != null && hazardEvalType.equals("WeightingBased")) {
            sensorInformation = sensorFileLine.split("@")[0];
            weighting = Integer.parseInt(sensorFileLine.split("@")[1]);
            sensorOutput.put("Weighting", weighting);
        } else {
            sensorInformation = sensorFileLine;
        }

        // initialising sensor based upon sensor type
        String[] sensorValues = sensorInformation.split(":");
        TimedSensor timedSensor;
        int[] readings = convertStringArray(
                sensorValues[1].split(","));
        switch (sensorValues[0]) {
            case "CarbonDioxideSensor" -> {
                int varLimit = Integer.parseInt(sensorValues[4]);
                int idealValue = Integer.parseInt(sensorValues[3]);
                int frequency = Integer.parseInt(sensorValues[2]);
                timedSensor = new CarbonDioxideSensor(readings,
                        frequency, idealValue, varLimit);
                break;
            }
            case "NoiseSensor" -> {
                int frequency = Integer.parseInt(sensorValues[2]);
                timedSensor = new NoiseSensor(readings, frequency);
                break;
            }
            case "OccupancySensor" -> {
                int capacity = Integer.parseInt(sensorValues[3]);
                int frequency = Integer.parseInt(sensorValues[2]);
                timedSensor = new OccupancySensor(readings,
                        frequency, capacity);
                break;
            }
            case "TemperatureSensor" -> {
                timedSensor = new TemperatureSensor(readings);
                break;
            }
            default -> throw new FileFormatException();
        }
        sensorOutput.put("Sensor", timedSensor);
        return sensorOutput;
    }
}

