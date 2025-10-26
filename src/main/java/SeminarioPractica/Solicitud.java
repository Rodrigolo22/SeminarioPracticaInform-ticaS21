package SeminarioPractica;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Solicitud {
    private int id;
    private Obra obra;
    private Usuario solicitante;
    private LocalDate fecha;
    private Prioridad prioridad;
    private Estado estado;
    private String descripcion;
    private final List<DetalleSolicitud> detalles = new ArrayList<>();

    // Constructor vacío requerido por controladores/FX
    public Solicitud() {}

    public Solicitud(int id, Obra obra, Usuario solicitante, LocalDate fecha,
                     Prioridad prioridad, Estado estado, String descripcion) {
        this.id = id;
        this.obra = obra;
        this.solicitante = solicitante;
        this.fecha = fecha;
        this.prioridad = prioridad;
        this.estado = estado;
        this.descripcion = descripcion;
    }

    // Getters/Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Obra getObra() { return obra; }
    public void setObra(Obra obra) { this.obra = obra; }

    public Usuario getSolicitante() { return solicitante; }
    public void setSolicitante(Usuario solicitante) { this.solicitante = solicitante; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Prioridad getPrioridad() { return prioridad; }
    public void setPrioridad(Prioridad prioridad) { this.prioridad = prioridad; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<DetalleSolicitud> getDetalles() { return detalles; }
}
