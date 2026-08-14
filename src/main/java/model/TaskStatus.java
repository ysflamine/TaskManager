package model;

public enum TaskStatus {
    PENDIENTE,
    EN_PROGRESO,
    COMPLETADA;

    // metodo para obtener el estado a partir de un String
    public static TaskStatus fromString(String text) {
        for (TaskStatus status : TaskStatus.values()) {
            if (status.name().equalsIgnoreCase(text)) {
                return status;
            }
        }
        return PENDIENTE; // valor por defecto
    }
}