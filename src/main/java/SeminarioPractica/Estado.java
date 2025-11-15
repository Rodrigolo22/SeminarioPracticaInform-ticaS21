package SeminarioPractica;

/**
 * Estados posibles de una solicitud.
 * El valor que se guarda en BD es el name() (PENDIENTE, APROBADA, etc.).
 */
public enum Estado {

    PENDIENTE("Pendiente"),
    APROBADA("Aprobada"),
    RECHAZADA("Rechazada"),
    EN_PROCESO("En proceso"),
    EN_CAMINO_OBRA("En camino a obra"),
    ENTREGADO("Entregado");

    private final String descripcion;

    Estado(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
