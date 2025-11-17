package reserva.extrasYPromociones;

import org.junit.Test;

import reserva.cliente.Cliente;
import reserva.cliente.ClienteEmpresarial;
import reserva.cliente.ClienteParticular;
import reserva.modalidad.ModalidadAlquiler;
import reserva.modalidad.PorDia;
import reserva.reserva.Reserva;
import reserva.vehiculo.EstadoVehiculo;
import reserva.vehiculo.TipoVehiculo;
import reserva.vehiculo.Vehiculo;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PromocionTest {
    @Test
    public void promocion_aplicaSoloAElectricos() {
        TipoVehiculo tipo = TipoVehiculo.ELECTRICO;
        Vehiculo vehiculoElectrico = new Vehiculo("TES-001", "Ford", "Focus", EstadoVehiculo.Estado.DISPONIBLE, tipo, new BigDecimal("9000"));
        Vehiculo vehiculoNoElectrico = new Vehiculo("TES-002", "Ford", "Focus", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));

        Promocion promoSoloElectricos = new Promocion("ELECTRO", new BigDecimal("0.30"), true, true, true, "La promocion incluye un descuento de 30%");
        Cliente cliente = new ClienteEmpresarial("Empresa Tech", "Calle 123", "3834234516", "empresaTech@gmail.com", "23-11234567-1", "Empresa Tech");


        assertTrue("La promocion debe aplicarse al vehiculo electrico", promoSoloElectricos.aplicaA(vehiculoElectrico, cliente));
        assertFalse("La promocion no deberia aplicarse al vehiculo no electrico", promoSoloElectricos.aplicaA(vehiculoNoElectrico, cliente));
    }

    @Test
    public void promocion_aplicaSegunTipoDeClienteYVehiculo() {
        //Promoción que aplica solo a vehículos eléctricos y a clientes empresariales
        Promocion promo = new Promocion("Promocion 1",new BigDecimal("0.15"),true,true,true, "15% para empresas y electricos");

        Vehiculo vehiculoElectrico = new Vehiculo("TES-001", "Ford", "Focus", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.ELECTRICO, new BigDecimal("9000"));
        Vehiculo vehiculoNoElectrico = new Vehiculo("TES-002", "Ford", "Focus", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));

        ClienteEmpresarial empresa = new ClienteEmpresarial("Empresa SA", "Direccion X", "123456", "empresa@x.com", "30-99999999-9", "Empresa SA");
        ClienteParticular particular = new ClienteParticular("Juan Gomez", "Calle Y", "34343434", "juanGomez@mail.com", "20123456");

        //Casos esperados:
        //electrico + empresa -> aplica
        assertTrue("La promo debería aplicarse a vehiculo electrico y cliente empresarial",promo.aplicaA(vehiculoElectrico, empresa));

        //electrico + particular -> NO aplica (cliente no es empresarial)
        assertFalse("La promo NO debería aplicarse a vehiculo electrico con cliente particular", promo.aplicaA(vehiculoElectrico, particular));

        //vehiculo no electrico + empresa -> NO aplica (vehiculo no electrico)
        assertFalse("La promo NO debería aplicarse a vehiculo naftero aunque el cliente sea empresarial", promo.aplicaA(vehiculoNoElectrico, empresa));

        //vehiculo no electrico + particular -> NO aplica
        assertFalse("La promo NO debería aplicarse a naftero y particular", promo.aplicaA(vehiculoNoElectrico, particular));
    }

    @Test
    public void promociones_noCombinables_noSeAplicanConjuntas() {
        //si existe al menos una promoción NO COMBINABLE aplicable, entonces se aplica sólo la promoción no-combinable más beneficiosa y ninguna otra
        GestorPromociones gestor = new GestorPromociones();
        // promo A no combinable
        //Esta promo aplica un 30% de descuento, no combinable
        Promocion promocionNoCombinable = new Promocion("Promocion 1", new BigDecimal("0.30"), false, true, false, "La promocion incluye un descuento de 30%. No es combinable");
        // promo B combinable
        //esta promo aplica un 10% de descuento, combinable
        Promocion promocionCombinable = new Promocion("Promocion 2", new BigDecimal("0.10"), true, true, false, "La promocion incluye un descuento de 10%. Combinable");

        gestor.agregarPromocion(promocionNoCombinable);
        gestor.agregarPromocion(promocionCombinable);

        //Creamos una reserva sencilla con un vehículo y sin extras
        Vehiculo vehiculoElectrico = new Vehiculo("TES-001", "Ford", "Focus", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.ELECTRICO, new BigDecimal("9000"));
        Cliente cliente = new ClienteParticular("Juan Gomez", "Calle 123", "3834234516", "juan@gmail.com", "23-11234567-1");

        LocalDate inicioReserva = LocalDate.now();
        //la reserva de inicio a fin es de 3 dias (PorDia calcula inclusive ambos extremos) entonces el coste base esperado es 9000 * 3 = 27000
        //3 dias de alquiler
        LocalDate finReserva = inicioReserva.plusDays(2);

        ModalidadAlquiler modalidad = new PorDia();
        Reserva reserva = new Reserva("123", vehiculoElectrico, cliente, inicioReserva, finReserva, modalidad);

        //Usamos el metodo de reserva para agregar promociones
        reserva.agregarPromocion(promocionCombinable);
        reserva.agregarPromocion(promocionNoCombinable);

        //Calculamos el costo total
        BigDecimal total = reserva.getCostoTotal();

        //Si tenemos una promocion NO COMBINABLE entre las aplicadas a la reserva, solo se aplica esa promocion (y no se suman otras que puedan aplicarse), tipicamente tomando la promocion mas beneficiosa segun politica (En este caso, el 30%).
        //Si todas las promociones aplicadas a la reserva son COMBINABLES, se suman todas las promociones aplicadas (Por ejemplo, 10% + 5% = 15%) tipicamente tomando la promocion mas beneficiosa segun politica (En este caso, el 10%).
        //En nuestro caso, la reserva tiene dos promociones aplicadas, una que es combinable y otra que no es combinable, entonces la no combinable debería anular la combinable.
        //Por lo tanto el resultado esperado es:
        //totalEsperado = 27000 - 27000 * 0.30 = 18900
        //Si erroneamente se aplica la combinable, el resultado sería:
        //totalErroneo = 27000 - 27000 *(0.30 + 0.10) = 16200

        //El siguiente assert compara el total devuelto por reserva.getCostoTotal() con el totalEsperado, y ademas comprueba que no sea igual al totalErroneo

        //Coste base esperado: 9000 * 3 = 27000
        BigDecimal costeBase = new BigDecimal("9000").multiply(new BigDecimal("3"));
        //Si se aplica solo la no-combinable (30%):
        BigDecimal esperadoSoloNoCombinable = costeBase
                .subtract(costeBase.multiply(new BigDecimal("0.30"))); //27000 - 8100 = 18900

        //Si se aplicaran ambas (incorrecto), sería:
        BigDecimal esperadoAmbas = costeBase
                .subtract(costeBase.multiply(new BigDecimal("0.40"))); //27000 - 10800 = 16200

        assertNotNull("El total no debe ser null", total);

        //Comparamos con el esperado (usamos compareTo por el BigDecimal)
        assertTrue("Se esperaba que la promoción no combinable (30%) sea la aplicada. " +
                        "Total esperado = " + esperadoSoloNoCombinable + " pero fue: " + total,
                total.compareTo(esperadoSoloNoCombinable) == 0);

        //Aseguramos que no se aplicaron ambas promociones (no debe coincidir con el caso erróneo)
        assertFalse("No debería haberse aplicado la suma de promociones (30% + 10%).",
                total.compareTo(esperadoAmbas) == 0);
    }
}
