package bms.util;

import bms.building.Building;
import bms.floor.Floor;
import bms.room.Room;
import bms.room.RoomState;
import bms.room.RoomType;
import bms.sensors.ComfortSensor;
import bms.sensors.Sensor;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class that provides a recommendation for a study room in a
 * building.
 */
public class StudyRoomRecommender {
    /**
     * Returns a room in the given building that is most suitable for study
     * purposes.
     * Any given room's suitability for study is based on several criteria,
     * including:
     *      - the room's type - it must be a study room (see RoomType)
     *      - the room's status - it must be open, not being evacuated or in
     *      maintenance (see Room.evaluateRoomState())
     *      - the room's comfort level based on its available sensors
     *      (see ComfortSensor.getComfortLevel())
     *      - which floor the room is on - rooms on lower floors are better
     *
     * Since travelling up the floors of a building often requires walking up
     * stairs, the process for choosing a study room begins by looking for rooms
     * on the first floor, and only considers higher floors if doing so would
     * improve the comfort level of the room chosen. Similarly, once on a
     * floor, walking back down more than one floor to a previously considered
     * study room is also not optimal. If there are no rooms on the first floor
     * of a building that meet the basic criteria, then the algorithm should
     * recommend that the building be avoided entirely, even if there are
     * suitable rooms on higher floors.
     *
     * Based on these requirements, the algorithm for determining the most
     * suitable study room is as follows:
     *      1. If there are no rooms in the building, return null.
     *      2. Consider only rooms on the first floor.
     *      3. Eliminate any rooms that are not study rooms or are not open.
     *      If there are no remaining candidate rooms, return the room with the
     *      highest comfort level on the previous floor, or null if there is no
     *      previous floor.
     *      4. Calculate the comfort level of all remaining rooms on this floor,
     *      using the average of the comfort levels of each room's available
     *      comfort sensors. If a room has no comfort sensors, its comfort
     *      level should be treated as 0.
     *      5. Keep a reference to the room with the highest comfort level on
     *      this floor based on the calculation in the previous step. If there
     *      is a tie between two or more rooms, any of these may be chosen.
     *      6. If the highest comfort level of any room on this floor is less
     *      than or equal to the highest comfort level of any room on the
     *      previous floor, return the room on the previous floor with the
     *      highest comfort level.
     *      7.If this is the top floor of the building, return the room found
     *      in step 5. Otherwise, repeat steps 2-7 for the next floor up.
     *
     * @param building building in which to search for a study room
     * @return the most suitable study room in the building; null if there are
     * none
     */
    public static Room recommendStudyRoom(Building building) {
        List<Floor> floors = building.getFloors();
        Room currentRoom = null;
        double currentComfortLevel;
        List<Room> candidateRooms = new ArrayList<>();
        for (Floor f : floors)  {
            List<Room> rooms = f.getRooms();
            // determining all candidate rooms on a given floor
            for (Room r: rooms) {
                if (r.getType().equals(RoomType.STUDY) &&
                        r.evaluateRoomState().equals(RoomState.OPEN)) {
                    candidateRooms.add(r);
                }
            }
            // determining most appropriate room out of candidate rooms
            if (candidateRooms.size() == 0) {
                return currentRoom;
            } else {
                if (currentRoom == null) {
                    currentRoom = candidateRooms.get(0);
                }
                Room previousRoom = currentRoom;
                currentComfortLevel = calculateComfortLevel(currentRoom);
                for (Room r : candidateRooms) {
                    if (calculateComfortLevel(r) > currentComfortLevel) {
                        currentRoom = r;
                        currentComfortLevel = calculateComfortLevel(r);
                    }
                }
                if (previousRoom.equals(currentRoom)) {
                    return currentRoom;
                }
            }
        }
        return currentRoom;
    }

    /**
     * Determines the average of comfort level values for sensors in a given
     * room.
     * @param room room for average comfort level to be calculated
     * @return double which represents the average comfort level value
     */
    private static double calculateComfortLevel(Room room) {
        double total = 0;
        for (Sensor s : room.getSensors()) {
            ComfortSensor comfortSensor = (ComfortSensor) s;
            total += comfortSensor.getComfortLevel();
        }
        return total / room.getSensors().size();
    }
}
