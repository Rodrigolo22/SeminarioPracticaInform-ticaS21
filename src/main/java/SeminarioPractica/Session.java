package SeminarioPractica;

public class Session {

    private static Usuario usuario;
    private static Solicitud solicitudSeleccionada;

    private Session() {
    }

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario u) {
        usuario = u;
    }

    public static void clear() {
        usuario = null;
        solicitudSeleccionada = null;
    }

    public static Solicitud getSolicitudSeleccionada() {
        return solicitudSeleccionada;
    }

    public static void setSolicitudSeleccionada(Solicitud s) {
        solicitudSeleccionada = s;
    }
}
