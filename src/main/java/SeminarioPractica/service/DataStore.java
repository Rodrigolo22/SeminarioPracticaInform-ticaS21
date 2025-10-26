package SeminarioPractica.service;

import SeminarioPractica.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DataStore {

    //almacenamiento en memoria
    private static final List<Usuario>    USUARIOS    = new ArrayList<>();
    private static final List<Obra>       OBRAS       = new ArrayList<>();
    private static final List<Solicitud>  SOLICITUDES = new ArrayList<>();

    // secuencias
    private static final AtomicInteger SEQ_USUARIO   = new AtomicInteger(3);
    private static final AtomicInteger SEQ_OBRA      = new AtomicInteger(2);
    private static final AtomicInteger SEQ_SOLICITUD = new AtomicInteger(0);

    static {
        // Usuarios demo (activos)
        USUARIOS.add(new Usuario(1, "Carlos", "Encargado", "encargado@andina.com", "demo", Rol.ENCARGADO, true));
        USUARIOS.add(new Usuario(2, "Laura",  "Logística", "logistica@andina.com", "demo", Rol.LOGISTICA, true));
        USUARIOS.add(new Usuario(3, "Ana",    "Admin",     "admin@andina.com",     "demo", Rol.ADMIN,     true));

        // Obras
        OBRAS.add(new Obra(1, "Adoquinado Pozo Colorado", "Tilcara", true));
        OBRAS.add(new Obra(2, "Pista de Atletismo",       "Alto Comedero", true));

        // Solicitud
        Solicitud s = new Solicitud();
        s.setId(SEQ_SOLICITUD.incrementAndGet());
        s.setObra(OBRAS.get(0));
        s.setSolicitante(USUARIOS.get(0)); // Carlos Encargado
        s.setFecha(LocalDate.now());
        s.setPrioridad(Prioridad.MEDIA);
        s.setEstado(Estado.PENDIENTE);
        s.setDescripcion("EPP - Cascos x 10 (rojos)");
        SOLICITUDES.add(s);
    }

    // LOGIN
    public static Usuario login(String email, String password) {
        for (Usuario u : USUARIOS) {
            if (u.getEmail().equalsIgnoreCase(email)
                    && u.getPassword().equals(password)
                    && u.isActivo()) {
                return u;
            }
        }
        return null;
    }

    // OBRAS
    public static List<Obra> getObras() { return OBRAS; }

    // SOLICITUDES
    public static void addSolicitud(Solicitud s) {
        if (s.getId() == 0) s.setId(SEQ_SOLICITUD.incrementAndGet());
        SOLICITUDES.add(s);
    }

    public static List<Solicitud> getSolicitudesDe(Usuario u) {
        List<Solicitud> out = new ArrayList<>();
        if (u == null) return out;
        for (Solicitud s : SOLICITUDES) {
            if (s.getSolicitante() != null && s.getSolicitante().getId() == u.getId()) {
                out.add(s);
            }
        }
        return out;
    }

    // Soporte para Logística
    public static List<Solicitud> getSolicitudesPendientes() {
        List<Solicitud> out = new ArrayList<>();
        for (Solicitud s : SOLICITUDES) {
            if (s.getEstado() == Estado.PENDIENTE) out.add(s);
        }
        return out;
    }

    public static void cambiarEstadoSolicitud(int id, Estado nuevo) {
        for (int i = 0; i < SOLICITUDES.size(); i++) {
            Solicitud s = SOLICITUDES.get(i);
            if (s.getId() == id) {
                s.setEstado(nuevo);
                SOLICITUDES.set(i, s); // “persistimos” el cambio en memoria
                return;
            }
        }
    }

    // helpers para futuras altas de usuarios/obras
    public static int nextUsuarioId()   { return SEQ_USUARIO.incrementAndGet(); }
    public static int nextObraId()      { return SEQ_OBRA.incrementAndGet(); }
    public static int nextSolicitudId() { return SEQ_SOLICITUD.incrementAndGet(); }
}
