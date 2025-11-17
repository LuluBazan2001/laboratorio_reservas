package reserva.reserva;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import reserva.cliente.Cliente;
import reserva.extrasYPromociones.*;
import reserva.extrasYPromociones.excepciones.PromocionExistenteException;
import reserva.extrasYPromociones.excepciones.ServicioExistenteException;
import reserva.modalidad.ModalidadAlquiler;
import reserva.reserva.excepciones.*;
import reserva.vehiculo.*;

public class Reserva {
    private String codigoReserva; //Con esta identificacion unica podremos identificar la reserva
    private Vehiculo vehiculo; //Vehiculo que se va a reservar
    private Cliente cliente; //Cliente que hace la reserva
    private LocalDate fechaInicioReserva;
    private LocalDate fechaFinReserva;
    private EstadoReserva estado;
    private ModalidadAlquiler modalidad;
    
    private List<ServicioExtra> serviciosExtra;
    private List<Promocion> promociones;

    public Reserva(String codigoReserva, Vehiculo vehiculo, Cliente cliente,
                   LocalDate fechaInicioReserva, LocalDate fechaFinReserva, ModalidadAlquiler modalidad) {
        this.codigoReserva = codigoReserva;
        this.vehiculo = vehiculo;
        this.cliente = cliente;
        this.fechaInicioReserva = fechaInicioReserva;
        this.fechaFinReserva = fechaFinReserva;
        this.estado = EstadoReserva.PENDIENTE;
        this.serviciosExtra = new ArrayList<>();
        this.promociones = new ArrayList<>();
        this.modalidad = modalidad;
    }

    /******************************************** */
    /*           ESTADO DE RESERVAS               */
    /******************************************** */
    public void confirmarReserva() {
        //Si el estado de la reserva es distinto a PENDIENTE, lanza una excepcion
        if(estado != EstadoReserva.PENDIENTE) {
            throw new ConfirmacionReservaException("Solo se pueden confirmar reservas pendientes");
        }
        //Si la reserva se confirma, se cambia el estado a activa
        estado = EstadoReserva.ACTIVA;
        //Y se actualiza el estado del vehiculo a rentado
        vehiculo.setEstado(EstadoVehiculo.Estado.RENTADO);
    }
    public void cancelarReserva() {
        //Si el estado de la reserva es igual a Finalizada, lanza una excepcion
        if(estado == EstadoReserva.FINALIZADA)
            throw new CancelarReservasException("No se puede cancelar una reserva finalizada");
        //Si la reserva se cancela, se cambia el estado a cancelada
        estado = EstadoReserva.CANCELADA;
        //Y se actualiza el estado del vehiculo a disponible
        vehiculo.setEstado(EstadoVehiculo.Estado.DISPONIBLE);
    }
    public void finalizarReserva() {
        //Si el estado de la reserva es distinto a ACTIVA, lanza una excepcion
        if(estado != EstadoReserva.ACTIVA) 
            throw new FinalizarReservasException("Solo se pueden finalizar reservas activas");
        //Si la reserva se finaliza, se cambia el estado a finalizada
        estado = EstadoReserva.FINALIZADA;
        //Y se actualiza el estado del vehiculo a disponible
        vehiculo.setEstado(EstadoVehiculo.Estado.DISPONIBLE);
    }

    

    /******************************************** */
    /*    AGREGAR PROMOCIONES Y SERVICIOS EXTRA   */
    /******************************************** */
    public void agregarPromocion(Promocion promocion) {
        if(promociones.stream().anyMatch(p -> p.getNombre().equals(promocion.getNombre()))) {
            throw new PromocionExistenteException("Ya existe una promocion con el nombre " + promocion.getNombre());
        }
        promociones.add(promocion);
    }

    public void agregarServicioExtra(ServicioExtra servicioExtra) {
        if(serviciosExtra.stream().anyMatch(s -> s.getNombreServicio().equals(servicioExtra.getNombreServicio()))) {
            throw new ServicioExistenteException("Ya existe un servicio extra con el nombre " + servicioExtra.getNombreServicio());
        }
        serviciosExtra.add(servicioExtra);
    }

    public List<Promocion> getPromociones() {
        return Collections.unmodifiableList(this.promociones);
    }

    public List<ServicioExtra> getServiciosExtra() {
        return Collections.unmodifiableList(this.serviciosExtra);
    }

    /******************************************** */
    /*           GETTERS                          */
    /******************************************** */
    public String getCodigoReserva() { return codigoReserva; }
    public Vehiculo getVehiculo() { return vehiculo; }
    public Cliente getCliente() { return cliente; }
    public LocalDate getFechaInicioReserva() { return fechaInicioReserva; }
    public LocalDate getFechaFinReserva() { return fechaFinReserva; }
    public EstadoReserva getEstado() { return estado; }
    
    public BigDecimal getCostoBase() {
        return modalidad.calcularCosto(vehiculo, fechaInicioReserva, fechaFinReserva);
    }

    public BigDecimal getCostoExtras() {
        BigDecimal costoExtras = BigDecimal.ZERO;
        for (ServicioExtra e : serviciosExtra) {
            costoExtras = costoExtras.add(e.getCostoServicio());
        }
        return costoExtras;
    }
    
    public List<Promocion> getPromocionesAplicadas() {
        return Collections.unmodifiableList(this.promociones);
    }



    /******************************************** */
    /*         CALCULO DE COSTO TOTAL             */
    /******************************************** */
    //si hay una promo no-combinable, no se combinan otras promociones
    //Si hay varias no-combinables aplicables, aplica la más beneficiosa (la que más descuento da)
    //Si no hay no-combinables, permite la suma de todas las promocionales combinables
    public BigDecimal getCostoTotal() {
        //Costo base según modalidad y vehículo
        BigDecimal costoBase = modalidad.calcularCosto(vehiculo, fechaInicioReserva, fechaFinReserva);

        //Suma de extras
        BigDecimal costoExtras = BigDecimal.ZERO;
        for (ServicioExtra extra : serviciosExtra) {
            costoExtras = costoExtras.add(extra.getCostoServicio());
        }

        BigDecimal subtotal = costoBase.add(costoExtras);

        //Descuentos
        List<Promocion> aplicables = new ArrayList<>();
        for (Promocion promo : promociones) {
            if (promo.aplicaA(vehiculo, cliente)) {
                aplicables.add(promo);
            }
        }

        //Si existe al menos una promoción NO COMBINABLE -> aplicamos solo la mejor (mayor porcentaje)
        BigDecimal totalDescuento = BigDecimal.ZERO;
        List<Promocion> noCombinables = aplicables.stream().filter(p -> !p.isCombinable()).collect(Collectors.toList());

        if (!noCombinables.isEmpty()) {
            //Elegimos la promoción con el mayor porcentajeDescuento
            Promocion mejorNoCombinable = noCombinables.stream().max(Comparator.comparing(Promocion::getPorcentajeDescuento)).get(); //seguro porque la lista no está vacía
            BigDecimal descuento = subtotal.multiply(mejorNoCombinable.getPorcentajeDescuento());
            totalDescuento = totalDescuento.add(descuento);

        } else {
            //Si no hay no-combinables -> sumar descuentos de todas las promociones (todas son combinables en este caso)
            for (Promocion promo : aplicables) {
                BigDecimal descuento = subtotal.multiply(promo.getPorcentajeDescuento());
                totalDescuento = totalDescuento.add(descuento);
            }
        }

        BigDecimal total = subtotal.subtract(totalDescuento);
        return total.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : total;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Reserva)) return false;
        Reserva reserva = (Reserva) obj;
        return Objects.equals(vehiculo, reserva.vehiculo) &&
                Objects.equals(cliente, reserva.cliente) &&
                Objects.equals(fechaInicioReserva, reserva.fechaInicioReserva) &&
                Objects.equals(fechaFinReserva, reserva.fechaFinReserva);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehiculo, fechaInicioReserva, fechaFinReserva);
    }
}
