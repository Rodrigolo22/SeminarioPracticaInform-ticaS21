package SeminarioPractica.service;

import SeminarioPractica.*;
import SeminarioPractica.db.DatabaseConfig;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class DataStore {

    private DataStore() {
        // Utilidad estática
    }


    // USUARIOS

    public static Usuario login(String email, String password) {
        String sql = "SELECT id, nombre, apellido, email, password, rol, activo " +
                "FROM usuario WHERE email = ? AND password = ? AND activo = 1";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password); // aquí podrías aplicar hash

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setNombre(rs.getString("nombre"));
                    u.setApellido(rs.getString("apellido"));
                    u.setEmail(rs.getString("email"));
                    u.setPassword(rs.getString("password"));
                    u.setRol(Rol.valueOf(rs.getString("rol")));
                    u.setActivo(rs.getBoolean("activo"));
                    return u;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en login()");
            e.printStackTrace();
        }
        return null;
    }


    // OBRAS

    public static List<Obra> getObras() {
        List<Obra> obras = new ArrayList<>();
        String sql = "SELECT id, nombre, ubicacion, activa FROM obra WHERE activa = 1 ORDER BY nombre";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Obra o = new Obra(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("ubicacion"),
                        rs.getBoolean("activa")
                );
                obras.add(o);
            }
        } catch (SQLException e) {
            System.err.println("Error en getObras()");
            e.printStackTrace();
        }
        return obras;
    }


    public static void addSolicitud(Solicitud s) {
        String insertSol = "INSERT INTO solicitud (usuario_id, obra_id, fecha_creacion, prioridad, estado, observaciones) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        String insertDet = "INSERT INTO detalle_solicitud (solicitud_id, descripcion_recurso, unidad, cantidad) " +
                "VALUES (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement psSol = null;
        PreparedStatement psDet = null;

        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);

            psSol = conn.prepareStatement(insertSol, Statement.RETURN_GENERATED_KEYS);
            psSol.setInt(1, s.getSolicitante().getId());
            psSol.setInt(2, s.getObra().getId());

            LocalDate fecha = s.getFecha() != null ? s.getFecha() : LocalDate.now();
            psSol.setTimestamp(3, Timestamp.valueOf(fecha.atStartOfDay()));

            psSol.setString(4, s.getPrioridad().name());
            psSol.setString(5, s.getEstado().name());
            psSol.setString(6, s.getDescripcion());

            psSol.executeUpdate();

            int idGenerado;
            try (ResultSet keys = psSol.getGeneratedKeys()) {
                if (keys.next()) {
                    idGenerado = keys.getInt(1);
                } else {
                    throw new SQLException("No se obtuvo ID generado para solicitud");
                }
            }
            s.setId(idGenerado);

            if (s.getDetalles() != null && !s.getDetalles().isEmpty()) {
                psDet = conn.prepareStatement(insertDet);
                for (DetalleSolicitud d : s.getDetalles()) {
                    psDet.setInt(1, idGenerado);
                    psDet.setString(2, d.getDescripcion());
                    psDet.setString(3, d.getUnidad());
                    psDet.setDouble(4, d.getCantidad());
                    psDet.addBatch();
                }
                psDet.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            System.err.println("Error en addSolicitud()");
            e.printStackTrace();
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
        } finally {
            if (psDet != null) try { psDet.close(); } catch (SQLException ignored) {}
            if (psSol != null) try { psSol.close(); } catch (SQLException ignored) {}
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {}
            }
        }
    }

    public static List<Solicitud> getSolicitudesDe(Usuario u) {
        List<Solicitud> lista = new ArrayList<>();
        if (u == null) return lista;

        String sql = "SELECT s.id, s.fecha_creacion, s.prioridad, s.estado, s.observaciones, " +
                "o.id AS obra_id, o.nombre AS obra_nombre, o.ubicacion AS obra_ubicacion " +
                "FROM solicitud s " +
                "JOIN obra o ON s.obra_id = o.id " +
                "WHERE s.usuario_id = ? " +
                "ORDER BY s.fecha_creacion DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, u.getId());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Solicitud s = mapSolicitudBasica(rs, u);
                    lista.add(s);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en getSolicitudesDe()");
            e.printStackTrace();
        }
        return lista;
    }


    // Solicitudes que ve Logística
    public static List<Solicitud> getSolicitudesPendientes() {
        List<Solicitud> lista = new ArrayList<>();

        String sql = "SELECT " +
                "s.id, " +
                "s.fecha_creacion, " +
                "s.observaciones, " +
                "s.estado, " +
                "s.prioridad, " +
                "o.id   AS obra_id, " +
                "o.nombre AS obra_nombre, " +
                "u.id   AS usuario_id, " +
                "u.nombre  AS usuario_nombre, " +
                "u.apellido AS usuario_apellido, " +
                "u.email    AS usuario_email " +
                "FROM solicitud s " +
                "JOIN obra o    ON s.obra_id    = o.id " +
                "JOIN usuario u ON s.usuario_id = u.id " +
                "ORDER BY s.fecha_creacion DESC, s.id DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Obra obra = new Obra(
                        rs.getInt("obra_id"),
                        rs.getString("obra_nombre")
                );

                Usuario usu = new Usuario(
                        rs.getInt("usuario_id"),
                        rs.getString("usuario_nombre"),
                        rs.getString("usuario_apellido"),
                        rs.getString("usuario_email"),
                        "",                 // password no hace falta acá
                        Rol.ENCARGADO,
                        true
                );

                Solicitud s = new Solicitud();
                s.setId(rs.getInt("id"));

                // fecha_creacion es DATETIME → la pasamos a LocalDate
                s.setFecha(rs.getTimestamp("fecha_creacion")
                        .toLocalDateTime()
                        .toLocalDate());

                s.setDescripcion(rs.getString("observaciones"));
                s.setEstado(Estado.valueOf(rs.getString("estado")));
                s.setPrioridad(Prioridad.valueOf(rs.getString("prioridad")));
                s.setObra(obra);
                s.setSolicitante(usu);

                lista.add(s);
            }

        } catch (SQLException e) {
            System.err.println("Error en getSolicitudesPendientes() (Logística)");
            e.printStackTrace();
        }

        return lista;
    }





    public static void cambiarEstadoSolicitud(int idSolicitud, Estado nuevoEstado) {
        String sql = "UPDATE solicitud SET estado = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado.name());
            ps.setInt(2, idSolicitud);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error en cambiarEstadoSolicitud()");
            e.printStackTrace();
        }
    }


    // HELPER

    private static Solicitud mapSolicitudBasica(ResultSet rs, Usuario solicitante) throws SQLException {
        Solicitud s = new Solicitud();
        s.setId(rs.getInt("id"));

        Timestamp ts = rs.getTimestamp("fecha_creacion");
        LocalDate fecha = ts != null ? ts.toLocalDateTime().toLocalDate() : LocalDate.now();
        s.setFecha(fecha);

        s.setPrioridad(Prioridad.valueOf(rs.getString("prioridad")));
        s.setEstado(Estado.valueOf(rs.getString("estado")));
        s.setDescripcion(rs.getString("observaciones"));

        Obra obra = new Obra(
                rs.getInt("obra_id"),
                rs.getString("obra_nombre"),
                rs.getString("obra_ubicacion"),
                true
        );
        s.setObra(obra);
        s.setSolicitante(solicitante);

        return s;
    }

    // DETALLE DE SOLICITUD

    public static List<DetalleSolicitud> getDetalleDeSolicitud(int idSolicitud) {
        List<DetalleSolicitud> lista = new ArrayList<>();

        String sql = "SELECT id, solicitud_id, descripcion_recurso, unidad, cantidad " +
                "FROM detalle_solicitud WHERE solicitud_id = ? ORDER BY id";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idSolicitud);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetalleSolicitud d = new DetalleSolicitud(
                            "ITEM", // tipoRecurso, no lo estamos guardando en BD, no es crítico
                            rs.getString("descripcion_recurso"),
                            rs.getDouble("cantidad"),
                            rs.getString("unidad")
                    );
                    d.setId(rs.getInt("id"));
                    d.setIdSolicitud(rs.getInt("solicitud_id"));
                    lista.add(d);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en getDetalleDeSolicitud()");
            e.printStackTrace();
        }
        return lista;
    }
    // ABM OBRAS

    /**
     * Lista TODAS las obras (activas e inactivas) para el ABM del ADMIN.
     */
    public static List<Obra> getTodasLasObras() {
        List<Obra> lista = new ArrayList<>();

        String sql = "SELECT id, nombre, ubicacion, activa " +
                "FROM obra " +
                "ORDER BY nombre";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Obra o = new Obra(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("ubicacion"),
                        rs.getBoolean("activa")
                );
                lista.add(o);
            }

        } catch (SQLException e) {
            System.err.println("Error en getTodasLasObras()");
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Inserta una nueva obra.
     */
    public static void insertarObra(String nombre, String ubicacion, boolean activa) {
        String sql = "INSERT INTO obra (nombre, ubicacion, activa) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, ubicacion);
            ps.setBoolean(3, activa);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error en insertarObra()");
            e.printStackTrace();
        }
    }

    /**
     * Actualiza una obra existente.
     */
    public static void actualizarObra(int id, String nombre, String ubicacion, boolean activa) {
        String sql = "UPDATE obra SET nombre = ?, ubicacion = ?, activa = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, ubicacion);
            ps.setBoolean(3, activa);
            ps.setInt(4, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error en actualizarObra()");
            e.printStackTrace();
        }
    }

    /**
     * Baja lógica: marca la obra como inactiva.
     */
    public static void desactivarObra(int id) {
        String sql = "UPDATE obra SET activa = 0 WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error en desactivarObra()");
            e.printStackTrace();
        }
    }
// ABM ENCARGADOS (ADMIN)

    /**
     * Lista todos los usuarios con rol ENCARGADO.
     */
    public static List<Usuario> getEncargados() {
        List<Usuario> lista = new ArrayList<>();

        String sql = "SELECT id, nombre, apellido, email, password, rol, activo " +
                "FROM usuario " +
                "WHERE rol = 'ENCARGADO' " +
                "ORDER BY apellido, nombre";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("email"),
                        rs.getString("password"),
                        Rol.valueOf(rs.getString("rol")),
                        rs.getBoolean("activo")
                );
                lista.add(u);
            }

        } catch (SQLException e) {
            System.err.println("Error en getEncargados()");
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Inserta un nuevo encargado.
     */
    public static void insertarEncargado(String nombre,
                                         String apellido,
                                         String email,
                                         String password,
                                         boolean activo) {
        String sql = "INSERT INTO usuario (nombre, apellido, email, password, rol, activo) " +
                "VALUES (?, ?, ?, ?, 'ENCARGADO', ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, email);
            ps.setString(4, password);
            ps.setBoolean(5, activo);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error en insertarEncargado()");
            e.printStackTrace();
        }
    }

    /**
     * Actualiza datos de un encargado.
     * Si password viene vacío o null, NO se modifica la contraseña.
     */
    public static void actualizarEncargado(int id,
                                           String nombre,
                                           String apellido,
                                           String email,
                                           String password,
                                           boolean activo) {

        boolean cambiarPassword = (password != null && !password.isBlank());
        String sql;

        if (cambiarPassword) {
            sql = "UPDATE usuario " +
                    "SET nombre = ?, apellido = ?, email = ?, password = ?, activo = ? " +
                    "WHERE id = ? AND rol = 'ENCARGADO'";
        } else {
            sql = "UPDATE usuario " +
                    "SET nombre = ?, apellido = ?, email = ?, activo = ? " +
                    "WHERE id = ? AND rol = 'ENCARGADO'";
        }

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int idx = 1;
            ps.setString(idx++, nombre);
            ps.setString(idx++, apellido);
            ps.setString(idx++, email);

            if (cambiarPassword) {
                ps.setString(idx++, password);
            }

            ps.setBoolean(idx++, activo);
            ps.setInt(idx, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error en actualizarEncargado()");
            e.printStackTrace();
        }
    }

    /**
     * Baja lógica del encargado (activo = 0).
     */
    public static void desactivarEncargado(int id) {
        String sql = "UPDATE usuario SET activo = 0 " +
                "WHERE id = ? AND rol = 'ENCARGADO'";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error en desactivarEncargado()");
            e.printStackTrace();
        }
    }

  // HISTORIAL / LOG DE SOLICITUDES


     // Registra un evento en el historial de una solicitud.

    public static void registrarLogSolicitud(int solicitudId,
                                             int usuarioId,
                                             Estado estadoAnterior,
                                             Estado estadoNuevo,
                                             String comentario) {
        String sql = "INSERT INTO solicitud_log " +
                "(solicitud_id, usuario_id, estado_anterior, estado_nuevo, comentario) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, solicitudId);
            ps.setInt(2, usuarioId);
            ps.setString(3, estadoAnterior != null ? estadoAnterior.name() : null);
            ps.setString(4, estadoNuevo != null ? estadoNuevo.name() : null);
            ps.setString(5, comentario);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error en registrarLogSolicitud()");
            e.printStackTrace();
        }
    }

    /**
     * Devuelve el historial (logs) de una solicitud.
     */
    public static List<SolicitudLog> getHistorialSolicitud(int solicitudId) {
        List<SolicitudLog> lista = new ArrayList<>();

        String sql = "SELECT l.id, l.solicitud_id, l.fecha, l.estado_anterior, l.estado_nuevo, " +
                "       l.comentario, u.nombre, u.apellido " +
                "FROM solicitud_log l " +
                "JOIN usuario u ON l.usuario_id = u.id " +
                "WHERE l.solicitud_id = ? " +
                "ORDER BY l.fecha ASC, l.id ASC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, solicitudId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SolicitudLog log = new SolicitudLog();
                    log.setId(rs.getInt("id"));
                    log.setSolicitudId(rs.getInt("solicitud_id"));

                    Timestamp ts = rs.getTimestamp("fecha");
                    if (ts != null) {
                        log.setFecha(ts.toLocalDateTime());
                    }

                    String ea = rs.getString("estado_anterior");
                    log.setEstadoAnterior(ea != null ? Estado.valueOf(ea) : null);

                    String en = rs.getString("estado_nuevo");
                    log.setEstadoNuevo(en != null ? Estado.valueOf(en) : null);

                    String usuarioNombreCompleto =
                            rs.getString("nombre") + " " + rs.getString("apellido");
                    log.setUsuarioNombre(usuarioNombreCompleto);

                    log.setComentario(rs.getString("comentario"));

                    lista.add(log);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error en getHistorialSolicitud()");
            e.printStackTrace();
        }

        return lista;
    }




}
