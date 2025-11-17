package reserva.vehiculo;

import java.math.BigDecimal;
import java.util.Objects;

import reserva.vehiculo.excepciones.PatenteVaciaException;

public class Vehiculo {
    private String patente;
    private String marca;
    private String modelo;
    //enum nos permite definir un conjunto de constantes con nombre
    //en este caso los posibles estados de un vehiculo
    private EstadoVehiculo.Estado estado;
    private TipoVehiculo tipo;
    //El costo por día lo tenemos en esta clase, y en reserva llamamos a modalidad.calcularCosto() para obtener el costo base y luego aplicar extras/promociones
    private BigDecimal tarifaBase;

    public Vehiculo(String patente, String marca, String modelo, EstadoVehiculo.Estado estado, TipoVehiculo tipo, BigDecimal tarifaBase) {
        //Verificamos que la patente no sea vacio
        if (patente == null || patente.isBlank()) {
            throw new PatenteVaciaException("La patente no puede estar vacía");
        }

        this.patente = patente;
        this.marca = marca;
        this.modelo = modelo;
        this.estado = estado;
        this.tipo = tipo;
        this.tarifaBase = tarifaBase;
    }

    public String getPatente() {return patente;}
    public void setPatente(String patente) {this.patente = patente;}

    public String getMarca() {return marca;}
     public void setMarca(String marca) {this.marca = marca;}

    public String getModelo() {return modelo;}
    public void setModelo(String modelo) {this.modelo = modelo;}

    public EstadoVehiculo.Estado getEstado() { return estado;}
    public void setEstado(EstadoVehiculo.Estado estado) {this.estado = estado; }

    public TipoVehiculo getTipo() {return tipo; }
    public void setTipo(TipoVehiculo tipo) { this.tipo = tipo;}

    public BigDecimal getTarifaBase() { return tarifaBase; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vehiculo)) return false;
        Vehiculo vehiculo = (Vehiculo) obj;
        return Objects.equals(patente, vehiculo.patente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patente);
    }
}
