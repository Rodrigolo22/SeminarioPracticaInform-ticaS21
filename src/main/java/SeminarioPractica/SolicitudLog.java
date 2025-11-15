package SeminarioPractica;

import java.time.LocalDateTime;

public class SolicitudLog {

    private int id;
    private int solicitudId;
    private LocalDateTime fecha;
    private String usuarioNombre;
    private Estado estadoAnterior;
    private Estado estadoNuevo;
    private String comentario;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSolicitudId() { return solicitudId; }
    public void setSolicitudId(int solicitudId) { this.solicitudId = solicitudId; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }

    public Estado getEstadoAnterior() { return estadoAnterior; }
    public void setEstadoAnterior(Estado estadoAnterior) { this.estadoAnterior = estadoAnterior; }

    public Estado getEstadoNuevo() { return estadoNuevo; }
    public void setEstadoNuevo(Estado estadoNuevo) { this.estadoNuevo = estadoNuevo; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}
