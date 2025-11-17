package reserva.vehiculo;

public interface Electrico {
    //Con esta interfaz aislamos las caracteristicas especificas de los vehiculos electricos (como una bici o un auto electrico), sin mezclarlas en la clase Vehiculo.
    //La interfaz nos da la logica concreta cuando se necesite manipular un vehiculo electrico, sea su bateria u otras acciones electricas

    double getAutonomia(); //Distancia que puede recorrer el vehiculo con una carga completa
    double getPotencia(); //Potencia que puede dar al vehiculo con una carga completa
    double getTiempoCarga(); //Tiempo que tarda en cargar el vehiculo
    int getNivelBateria(); //Nivel de bateria del vehiculo (Porcentaje del 0 al 100)
    void recargarBateria(); //Metodo para recargar la bateria del vehiculo

}
