package bms.floor;

import bms.room.Room;
import bms.room.RoomState;
import bms.room.RoomType;
import bms.util.TimedItem;
import bms.util.Encodable;
import bms.util.TimedItemManager;
import java.util.List;
import java.util.TreeMap;

/**
 * Carries out maintenance on a list of rooms in a given floor.
 * The maintenance time for each room depends on the type of the room and its
 * area. Maintenance cannot progress whilst an evacuation is in progress.
 */
public class MaintenanceSchedule implements TimedItem, Encodable {

    /**
     * Order of rooms in which maintenance occurs on a floor
     */
    private final List<Room> roomOrder;

    /**
     * Map storing multiplier used to calculate the maintenance time of a room.
     */
    private static final TreeMap<RoomType, Double> ROOM_TYPE_MULTIPLIER =
            new TreeMap<>()
    {{
        put(RoomType.STUDY, 1.0);
        put(RoomType.OFFICE, 1.5);
        put(RoomType.LABORATORY, 2.0);
    }};

    /**
     * Room which is currently being maintained
     */
    private Room currentRoom;

    /**
     * index of the current room in the room order list
     */
    private int roomIndexCounter;

    /**
     * the amount of time the current room has been maintained for.
     */
    private int timeElapsedCurrentRoom;

    /**
     * Creates a new maintenance schedule for a floor's list of rooms.
     * In this constructor, the new maintenance schedule should be registered
     * as a timed item with the timed item manager.
     *
     * The first room in the given order should be set to "in maintenance",
     * see Room.setMaintenance(boolean).
     * @param roomOrder list of rooms on which to perform maintenance, in order
     */
    public MaintenanceSchedule(List<Room> roomOrder) {
        this.roomOrder = roomOrder;
        this.roomOrder.get(0).setMaintenance(true);
        currentRoom = roomOrder.get(0);
        roomIndexCounter = 0;
        timeElapsedCurrentRoom = 0;
        TimedItemManager.getInstance().registerTimedItem(this);
    }

    /**
     * Returns the room which is currently in the process of being maintained.
     * @return room currently in maintenance
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Returns the number of minutes that have elapsed while maintaining the
     * current room (getCurrentRoom()).
     * @return time elapsed maintaining current room
     */
    public int getTimeElapsedCurrentRoom() {
        return timeElapsedCurrentRoom;
    }

    /**
     * Returns the time taken to perform maintenance on the given room, in
     * minutes.
     * The maintenance time for a given room depends on its size (larger rooms
     * take longer to maintain) and its room type (rooms with more furniture
     * and equipment take take longer to maintain).
     *
     * The formula for maintenance time is calculated as the room's base
     * maintenance time multiplied by its room type multiplier, and finally
     * rounded to the nearest integer. Floating point operations should be used
     * during all steps of the calculation, until the final rounding to integer.
     *
     * Rooms with an area of Room.getMinArea() have a base maintenance time of
     * 5.0 minutes.
     *
     * Rooms with areas greater than Room.getMinArea() have a base maintenance
     * time of 5.0 minutes, plus 0.2 minutes for every square metre the room's
     * area is over Room.getMinArea().
     *
     * A room's room type multiplier is given in the table below.
     *
     * Room type multiplier table
     * Room Type	Room Type Multiplier
     * STUDY	            1
     * OFFICE	           1.5
     * LABORATORY	        2
     *
     * @param room room on which to perform maintenance
     * @return room's maintenance time in minutes
     */
    public int getMaintenanceTime(Room room) {
        float baseMaintenanceTime = (float) (5.0 + 0.2 * (room.getArea() -
                Room.getMinArea()));
        return (int) Math.round(baseMaintenanceTime *
                ROOM_TYPE_MULTIPLIER.get(room.getType()));
    }

    /**
     * Returns the machine-readable string representation of this maintenance
     * schedule.
     * The format of the string to return is:
     *
     *  roomNumber1,roomNumber2,...,roomNumberN
     *
     * where 'roomNumberX' is the room number of the Xth room in this
     * maintenance schedule's room order, from 1 to N where N is the number
     * of rooms in the maintenance order.
     * There should be no newline at the end of the string.
     *
     * See the demo save file for an example (uqstlucia.txt).
     * @return encoded string representation of this maintenance schedule
     */
    @Override
    public String encode() {
        StringBuilder encode = new StringBuilder();
        encode.append(roomOrder.get(0).getRoomNumber());
        for (int i = 1; i < roomOrder.size(); i++) {
            encode.append(",").append(roomOrder.get(i).getRoomNumber());
        }
        return encode.toString();
    }

    /**
     * Progresses the maintenance schedule by one minute.
     * If the room currently being maintained has a room state of EVACUATE,
     * then no action should occur.
     *
     * If enough time has elapsed such that the room currently being maintained
     * has completed its maintenance (according to getMaintenanceTime(Room)),
     * then:
     *
     * the current room should have its maintenance status set to false
     * (see Room.setMaintenance(boolean))
     * the next room in the list passed to the constructor should be set as the
     * new current room. If the end of the list has been reached, the new
     * current room should "wrap around" to the first room in the list.
     * the new current room should have its maintenance status set to true
     */
    public void elapseOneMinute() {
        if (!currentRoom.evaluateRoomState().equals(RoomState.EVACUATE)) {
            timeElapsedCurrentRoom += 1;
            if (timeElapsedCurrentRoom == getMaintenanceTime(currentRoom)) {
                currentRoom.setMaintenance(false);
                roomIndexCounter += 1;
                currentRoom = roomOrder.get(roomIndexCounter %
                        roomOrder.size());
                currentRoom.setMaintenance(true);
                timeElapsedCurrentRoom = 0;
            }
        }
    }

    /**
     * Stops the in-progress maintenance of the current room and progresses
     * to the next room.
     * The same steps should be undertaken as described in the dot point list
     * in elapseOneMinute().
     */
    public void skipCurrentMaintenance() {
        currentRoom.setMaintenance(false);
        roomIndexCounter += 1;
        currentRoom = roomOrder.get(roomIndexCounter % roomOrder.size());
        currentRoom.setMaintenance(true);
        timeElapsedCurrentRoom = 0;
    }

    /**
     * Returns the human-readable string representation of this maintenance
     * schedule.
     * The format of the string to return is
     *
     * MaintenanceSchedule: currentRoom=#currentRoomNumber,
     * currentElapsed=elapsed
     * where 'currentRoomNumber' is the room number of the room currently
     * being maintained, and 'elapsed' is the number of minutes that have
     * elapsed while maintaining the current room.
     * For example:
     *
     * MaintenanceSchedule: currentRoom=#108, currentElapsed=3
     * @return string representation of this maintenance schedule
     */
    public String toString() {
        return String.format("MaintenanceSchedule: currentRoom=#%s, " +
                "currentElapsed=%s", currentRoom.getRoomNumber(),
                getTimeElapsedCurrentRoom());
    }
}
