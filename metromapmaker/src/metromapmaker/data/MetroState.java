package metromapmaker.data;

/**
 * This enum has the various possible states of the Map maker app
 * during the editing process which helps us determine which controls
 * are usable or not and what specific user actions should affect.
 * 
 * @author Qi Yuan Fang
 * @version 1.0
 */
public enum MetroState {
    SELECTING_STATION,
    SELECTING_ENDLINE,
    SELECTING_IMAGE,
    SELECTING_LABEL,
    DO_NOTHING,
    ADDING_STATION,
    DELETING_STATION,
    INIT_D
}
