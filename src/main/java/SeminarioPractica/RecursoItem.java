package SeminarioPractica;

public abstract class RecursoItem {
    private String descripcion;
    private double cantidad;
    private String unidad;

    protected RecursoItem(String descripcion, double cantidad, String unidad) {
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.unidad = unidad;
    }

    public String getDescripcion() { return descripcion; }
    public double getCantidad() { return cantidad; }
    public String getUnidad() { return unidad; }

    // Ejemplo de Polimorfismo, cada subclase define su propia presentación
    public abstract String presentacionCorta();

    // Validaciones comunes a todas las subclases
    public void validar() {
        if (descripcion == null || descripcion.isBlank())
            throw new IllegalArgumentException("La descripción del ítem es obligatoria.");
        if (cantidad <= 0)
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        if (unidad == null || unidad.isBlank())
            throw new IllegalArgumentException("La unidad es obligatoria.");
    }
}
