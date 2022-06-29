package bms.room;

/**
 * Enum to represent the possible room state.
 */
public enum RoomState {
    /** Room that is open */
    OPEN,
    /** Room that needs to be evacuated */
    EVACUATE,
    /** Room that is in maintenance */
    MAINTENANCE,
    /** An error has occurred */
    ERROR
}
