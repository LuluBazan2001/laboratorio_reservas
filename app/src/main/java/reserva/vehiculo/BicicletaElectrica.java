package reserva.vehiculo;

import java.math.BigDecimal;

public class BicicletaElectrica extends Vehiculo implements Electrico {

    private double autonomia; // km
    private double potencia;  // kW
    private double tiempoCarga; // horas
    private int nivelBateria; // 0-100

    public BicicletaElectrica(String patente, String marca, String modelo, EstadoVehiculo.Estado estado, TipoVehiculo tipo, BigDecimal tarifaBase, double autonomia, double potencia, double tiempoCarga, int nivelBateria) {
        super(patente, marca, modelo, estado, TipoVehiculo.BICICLETA_ELECTRICA, tarifaBase);
    }

    @Override public double getAutonomia() { return autonomia; }
    @Override public double getPotencia() { return potencia; }
    @Override public double getTiempoCarga() { return tiempoCarga; }
    @Override public int getNivelBateria() { return nivelBateria; }
    @Override public void recargarBateria() { this.nivelBateria = 100; }

    //Vamos a usar getTipo().isElectrico() cuando queramos una comprobación rapida para, por ejemplo, aplicar algun descuento por ser electrico u otra cuestion que implique esto
    //Usaremos instanceof Electrico cuando necesitemos utilizar comportamientos (consultar el nivel de bateria, recargar la bateria, etc.)
}