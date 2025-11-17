package reserva.vehiculo;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
//import reserva.reservaVehiculo.tipoVehiculo.*;

import reserva.reserva.GestorReservas;
import reserva.vehiculo.excepciones.*;

public class FlotaTest {
    /*************************************************************************************************** */
    /*                                 TEST AGREGAR VEHICULO                                             */
    /*************************************************************************************************** */
    @Test 
    public void testAgregarVehiculo() {
        Flota flota = new Flota();
        Vehiculo vehiculo = new Vehiculo("ABC124", "Toyota", "Corolla 2020", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));

        flota.altaVehiculo(vehiculo);

        assertEquals(1, flota.getVehiculos().size());
        assertEquals("ABC124", flota.getVehiculos().get(0).getPatente());
    } 
    @Test (expected = VehiculoDuplicadoException.class)
    public void testAgregarVehiculo_rechaza() { 
        Flota flota = new Flota();
        Vehiculo vehiculo = new Vehiculo("ABC124", "Toyota", "Corolla 2020", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        Vehiculo vehiculoDuplicado = new Vehiculo("ABC124", "Toyota", "Corolla 2020", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        flota.altaVehiculo(vehiculo);
        flota.altaVehiculo(vehiculoDuplicado); // Deberia lanzar la excepcion.
        assertEquals(1, flota.getVehiculos().size());
    } 
    
    /*************************************************************************************************** */
    /*                                 TEST AGREGAR VEHICULO SIN DUPLICADOS                               */
    /*************************************************************************************************** */
    @Test(expected = VehiculoDuplicadoException.class)
    public void altaVehiculoSinDuplicados() {
        Flota flota = new Flota();
        Vehiculo vehiculo = new Vehiculo("ABC124", "Toyota", "Corolla 2020", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        Vehiculo vehiculo2 = new Vehiculo("ABC124", "Honda", "Civic 2021", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
    
        flota.altaVehiculo(vehiculo);
        flota.altaVehiculo(vehiculo2);

        assertEquals(1, flota.getVehiculos().size());
    } 
    
    /*************************************************************************************************** */
    /*                                 TEST LISTAR VEHICULOS                                               */
    /*************************************************************************************************** */
    @Test 
    public void testListarVehiculos() { 
        Flota flota = new Flota();
        Vehiculo vehiculo = new Vehiculo("ABC124", "Toyota", "Corolla 2020", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        Vehiculo vehiculo2 = new Vehiculo("ABC123", "Honda", "Civic 2021", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));

        flota.altaVehiculo(vehiculo);
        flota.altaVehiculo(vehiculo2);

        List<Vehiculo> vehiculos = flota.getVehiculos();

        assertEquals(2, vehiculos.size());
    } 
    
    /*************************************************************************************************** */
    /*                                 TEST CAMBIAR ESTADO VEHICULO                                       */
    /*************************************************************************************************** */
    @Test 
    public void cambiarEstadoVehiculo() { 
        Flota flota = new Flota();
        Vehiculo vehiculo = new Vehiculo("ABC124", "Toyota", "Corolla 2020", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));

        flota.altaVehiculo(vehiculo);
        flota.cambiarEstado("ABC124", EstadoVehiculo.Estado.MANTENIMIENTO);

        assertEquals(EstadoVehiculo.Estado.MANTENIMIENTO, flota.getVehiculos().get(0).getEstado());
    } 

    @Test(expected = VehiculoNoEncontradoException.class)
    public void cambiarEstadoVehiculo_noExiste() { 
        //Implemento prueba para manejar el caso cuando el vehículo no existe
        Flota flota = new Flota();
        
        flota.cambiarEstado("ABC124", EstadoVehiculo.Estado.MANTENIMIENTO);
    } 

    /*************************************************************************************************** */
    /*                                 TEST BUSCAR POR PATENTE                                               */
    /*************************************************************************************************** */
    @Test
    public void buscarPorPatente(){
        Flota flota = new Flota();
        Vehiculo vehic = new Vehiculo("ABC124", "Toyota", "Corolla 2020", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));

        flota.altaVehiculo(vehic);

        List<Vehiculo> buscado = flota.buscarPorPatente("ABC124");

        //assertEquals nos permite comparar valores esperados con valores reales.

        assertEquals("El vehiculo fue encontrado",1, buscado.size());
        assertEquals("ABC124", buscado.get(0).getPatente());
    }
    @Test
    public void buscarPorPatente_noExiste(){
        Flota flota = new Flota();

        List<Vehiculo> buscado = flota.buscarPorPatente("KOU457");

        assertEquals("El vehiculo no fue encontrado",0, buscado.size());
        //isEmpty devuelve true si la lista no contiene elementos.
        assertTrue("la lista deberia estar vacia porque el vehiculo no existe",buscado.isEmpty());

    }

    /*************************************************************************************************** */
    /*                                 TEST CONSULTAR DISPONIBLE POR TIPO                                  */
    /*************************************************************************************************** */
    @Test
    public void testConsultarDisponiblePorTipo(){
        Flota flota = new Flota();
        Vehiculo vehic = new Vehiculo("FGT458", "Toyota", "Corolla 2020", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        Vehiculo vehic2 = new Vehiculo("JVC123", "Peugot", "308 2024", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        Vehiculo vehic3 = new Vehiculo("ZYC397", "Renault", "Strada 2010", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));

        flota.altaVehiculo(vehic);
        flota.altaVehiculo(vehic2);
        flota.altaVehiculo(vehic3);

        List<Vehiculo> disponibles = flota.consultarDisponibilidadPorTipo(TipoVehiculo.AUTO);

        assertEquals(3, disponibles.size());
        assertTrue(disponibles.stream().anyMatch(v -> vehic.getPatente().equals("FGT458")));
        assertTrue(disponibles.stream().anyMatch(v -> vehic2.getPatente().equals("JVC123")));
        assertTrue(disponibles.stream().anyMatch(v -> vehic3.getPatente().equals("ZYC397")));
    }
    @Test
    public void testConsultarDisponiblePorTipo_noExiste(){
        Flota flota = new Flota();

        Vehiculo vehic = new Vehiculo("FGT458", "Toyota", "Corolla 2020", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        Vehiculo vehic2 = new Vehiculo("JVC123", "Peugot", "308 2024", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        Vehiculo vehic3 = new Vehiculo("ZYC397", "Renault", "Strada 2010", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));

        flota.altaVehiculo(vehic);
        flota.altaVehiculo(vehic2);
        flota.altaVehiculo(vehic3);

         List<Vehiculo> disponibles = flota.consultarDisponibilidadPorTipo(TipoVehiculo.CAMIONETA);

        assertTrue("la lista deberia estar vacia por no registrar camionetas", disponibles.isEmpty());
        
    }


    /*************************************************************************************************** */
    /*                                 TEST LISTAR TODOS DISPONIBLES                                       */    
    /*************************************************************************************************** */
    @Test
    public void listarTodosDisponibles(){
        Flota flota = new Flota();

        Vehiculo vehiculo = new Vehiculo("FGT458", "Toyota", "Corolla 2020", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        Vehiculo vehiculo2 = new Vehiculo("JVC123", "Peugot", "308 2024", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        Vehiculo vehiculo3 = new Vehiculo("ZYC397", "Renault", "Strada 2010", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));

        flota.altaVehiculo(vehiculo);
        flota.altaVehiculo(vehiculo2);
        flota.altaVehiculo(vehiculo3);

        List<Vehiculo> disponibles = flota.listarTodosDisponibles();

        assertEquals(3, disponibles.size());
    }


    /*************************************************************************************************** */
    /*                                 TEST MODIFICAR VEHICULO                                             */
    /*************************************************************************************************** */
    @Test
    public void testModificarVehiculo() {
        Flota flota = new Flota();
        Vehiculo vehiculo = new Vehiculo("FGT458", "Toyota", "Corolla 2020", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));

        flota.altaVehiculo(vehiculo);

        //Creamos un nuevo vehiculo con el mismo patente, pero cambiando el modelo
        Vehiculo vehiculoNuevo = new Vehiculo("FGT458", "Renault", "Megane 2021", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));

        //Realizamos la modificacion
        flota.modificarVehiculo(vehiculoNuevo);

        //Buscamos el vehiculo para verificar los cambios
        List<Vehiculo> vehiculos = flota.buscarPorPatente("FGT458");

        assertEquals("La marca debería haberse modificado a Renault", "Renault", vehiculos.get(0).getMarca());
        assertEquals("El modelo debería haberse modificado a Megane 2021", "Megane 2021", vehiculos.get(0).getModelo());
    }

    @Test (expected = VehiculoNoEncontradoException.class)
    public void testModificarVehiculo_noExiste(){
        Flota flota = new Flota();
        Vehiculo vehiculoInexistente = new Vehiculo("GHF478", "Toyota", "Corolla 2020", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));

        //Al internar modificar un vehiculo que no existe, deberia lanzar una excepcion
        flota.modificarVehiculo(vehiculoInexistente);
    }

    /*************************************************************************************************** */
    /*                                 TEST ELIMINAR VEHICULO                                               */
    /*************************************************************************************************** */
    @Test
    public void testEliminarVehiculo(){
        Flota flota = new Flota();
        Vehiculo vehic = new Vehiculo("FGT458", "Toyota", "Corolla 2020", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));

        flota.altaVehiculo(vehic);
        flota.eliminarVehiculo("FGT458");

        assertEquals(0, flota.getVehiculos().size());
        
    }
    @Test (expected = VehiculoNoEncontradoException.class)
    public void testEliminarVehiculo_noExiste(){
        Flota flota = new Flota();

        flota.eliminarVehiculo("ABC124");
    }

    //Verificamos que un vehículo con mantenimiento programado NO figura como disponible en ese periodo
    @Test
    public void disponibilidadVehiculo_incluyeMantenimiento() {
        Flota flota = new Flota();
        GestorMantenimientos gestorMantenimiento = new GestorMantenimientos();
        GestorReservas gestorReservas = new GestorReservas();
        gestorReservas.setGestorMantenimientos(gestorMantenimiento);
        Vehiculo vehic = new Vehiculo("FGT458", "Toyota", "Corolla 2020", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        flota.altaVehiculo(vehic);

        //Agregamos mantenimiento que solapa el periodo consultado
        LocalDate mantInicio = LocalDate.of(2025, 11, 10);
        LocalDate mantFin = LocalDate.of(2025, 11, 15);

        Mantenimiento m = gestorMantenimiento.abrirMantenimiento(vehic, mantInicio, "Apertura de mantenimiento programado");
        gestorMantenimiento.cerrarMantenimiento(m, mantFin, "Trabajo", new BigDecimal("1000"));

        boolean disponibleDentro = gestorReservas.estaDisponible(vehic, LocalDate.of(2025, 11, 12), LocalDate.of(2025, 11, 13));
        boolean disponibleFuera = gestorReservas.estaDisponible(vehic, LocalDate.of(2025, 11, 16), LocalDate.of(2025, 11, 17));

        assertFalse("El vehículo no debería estar disponible durante el mantenimiento programado", disponibleDentro);
        assertTrue("El vehículo sí debería estar disponible fuera del periodo de mantenimiento", disponibleFuera);

        //consultamos disponibilidad dentro del periodo de mantenimiento
        boolean disponible = gestorMantenimiento.esVehiculoNoDisponible(vehic, mantInicio, mantFin);

        assertFalse("El vehículo no debería estar disponible durante el mantenimiento programado", disponible);
    }

}