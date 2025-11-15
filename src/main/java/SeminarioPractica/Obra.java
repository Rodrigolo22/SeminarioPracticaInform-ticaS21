package SeminarioPractica;

public class Obra {

    private int id;
    private String nombre;
    private String ubicacion;
    private boolean activa;

    public Obra() {
    }

    public Obra(int id, String nombre, String ubicacion, boolean activa) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.activa = activa;
    }
    public Obra(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    @Override
    public String toString() {
        return nombre + " - " + ubicacion;
    }
}
