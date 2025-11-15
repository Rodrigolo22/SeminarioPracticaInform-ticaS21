package SeminarioPractica;

public enum Prioridad {
    BAJA,
    MEDIA,
    ALTA,
    CRITICA;

    // Uso explícito de arreglo
    public static String[] toLabelArray() {
        Prioridad[] values = Prioridad.values();
        String[] labels = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            labels[i] = values[i].name();
        }
        return labels;
    }
}
