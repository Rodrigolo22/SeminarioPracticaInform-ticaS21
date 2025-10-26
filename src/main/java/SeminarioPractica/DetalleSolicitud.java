package SeminarioPractica;

public class DetalleSolicitud {
    private int id;
    private int idSolicitud;      // se completa luego del insert
    private String tipoRecurso;   // "MATERIAL" o "HERRAMIENTA"
    private String descripcion;
    private double cantidad;
    private String unidad;

    public DetalleSolicitud(String tipoRecurso, String descripcion, double cantidad, String unidad) {
        this.tipoRecurso = tipoRecurso;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.unidad = unidad;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdSolicitud() { return idSolicitud; }
    public void setIdSolicitud(int idSolicitud) { this.idSolicitud = idSolicitud; }
    public String getTipoRecurso() { return tipoRecurso; }
    public String getDescripcion() { return descripcion; }
    public double getCantidad() { return cantidad; }
    public String getUnidad() { return unidad; }
}
