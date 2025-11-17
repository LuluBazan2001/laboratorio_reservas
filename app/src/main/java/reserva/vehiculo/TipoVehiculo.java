package reserva.vehiculo;

public enum TipoVehiculo {
    //Este enum nos ayudara a filtrar/decidir que tipo de vehiculo se va a crear, y llevar ciertos metadatos
    AUTO(false, "Auto"),
    SUV(true, "SUV"),
    CAMIONETA(false, "Camioneta"),
    //Bicicleta electrica es un tipo de vehiculo electrico, lo dejo a modo de ejemplo, para test
    BICICLETA_ELECTRICA(true, "Bicicleta eléctrica"),
    //este ultimo es genérico, y se usa para todos los tipos de vehiculos electricos
    ELECTRICO(true, "Electrico");
    //Cada tipo tiene dos atributos: un booleano que indica si aplica descuento ecologico o politicas de mantenimiento distintas
    //y el nombre que permite mostrarlo con formato legible (para interfaces, reportes o tickets de reserva)

    private boolean electrico;
    private String nombre;

    TipoVehiculo(boolean electrico, String nombre) {
        this.electrico = electrico;
        this.nombre = nombre;
    }

    public boolean isElectrico() {
        return electrico;
    }

    public String getNombre() {
        return nombre;
    }
}
