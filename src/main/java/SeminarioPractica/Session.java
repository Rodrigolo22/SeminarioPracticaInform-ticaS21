package SeminarioPractica;

/**
 Clase estática que gestiona la sesión del usuario actualmente logueado.
 Permite guardar, obtener y limpiar el usuario activo durante
 la ejecución de la aplicación.
 Funciona como una sesión global
 mientras la aplicación esté abierta.
 */
public class Session {

    // Usuario actualmente autenticado en el sistema
    private static Usuario usuarioActual;


    //Guarda el usuario que inició sesión.

    public static void setUsuario(Usuario u) {
        usuarioActual = u;
    }


    //Devuelve el usuario que está actualmente en sesión.

    public static Usuario getUsuario() {
        return usuarioActual;
    }


     //Cierra la sesión eliminando el usuario actual.

    public static void clear() {
        usuarioActual = null;
    }

    /**
     Indica si hay un usuario logueado en este momento.
     Devuelve true si hay sesión activa, false si no.
     */
    public static boolean isLogged() {
        return usuarioActual != null;
    }
}
