package SeminarioPractica;

public class Material {
    private String descripcion;
    private double cantidad;
    private String unidad;

    public Material() {}

    public Material(String descripcion, double cantidad, String unidad) {
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.unidad = unidad;
    }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }

    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }

    @Override
    public String toString() {
        return descripcion + " (" + unidad + ")";
    }
}
