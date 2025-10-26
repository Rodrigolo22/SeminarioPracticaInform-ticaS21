package SeminarioPractica;

public class Obra {
    private int id;
    private String nombre;
    private String ubicacion;
    private boolean activa;

    public Obra(int id, String nombre, String ubicacion, boolean activa) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.activa = activa;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getUbicacion() { return ubicacion; }
    public boolean isActiva() { return activa; }

    @Override
    public String toString() {
        return id + " - " + nombre + " (" + ubicacion + ")";
    }
}
