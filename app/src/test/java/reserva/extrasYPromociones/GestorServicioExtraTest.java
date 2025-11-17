package reserva.extrasYPromociones;

import org.junit.Test;

import reserva.extrasYPromociones.excepciones.ServicioExistenteException;
import reserva.extrasYPromociones.excepciones.ServicioNoExistenteException;

import static org.junit.Assert.*;

import java.util.List;
import java.math.BigDecimal;
import java.util.Optional;

public class GestorServicioExtraTest {
    /*************************************************************************************************** */
    /*                                 TEST AGREGAR SERVICIO EXTRA                                               */
    /*************************************************************************************************** */
    @Test
    public void agregarServicioExtra_exitoso() {
        GestorServicioExtra gestor = new GestorServicioExtra();
        ServicioExtra servicio = new ServicioExtra("GPS", new BigDecimal("50.0"), true);

        gestor.agregarServicio(servicio);

        Optional<ServicioExtra> encontrado = gestor.buscarPorNombre("GPS");
        assertEquals("GPS", encontrado.get().getNombreServicio());
        assertEquals(new BigDecimal("50.0"), encontrado.get().getCostoServicio());
    }

    @Test(expected = ServicioExistenteException.class)
    public void agregarServicioExtra_existente_lanzaExcepcion() {
        GestorServicioExtra gestor = new GestorServicioExtra();
        ServicioExtra servicio = new ServicioExtra("GPS", new BigDecimal("50.0"), true);

        gestor.agregarServicio(servicio);

        // Act - intentar agregar otra con mismo nombre -> excepción
        ServicioExtra servicioDuplicado = new ServicioExtra("GPS", new BigDecimal("50.0"), true);
        gestor.agregarServicio(servicioDuplicado);
    }

    /*************************************************************************************************** */
    /*                                 TEST MODIFICAR SERVICIO EXTRA                                               */
    /*************************************************************************************************** */

    @Test
    public void modificarServicioExtra_exitoso() {
        GestorServicioExtra gestor = new GestorServicioExtra();
        ServicioExtra servicio = new ServicioExtra("GPS", new BigDecimal("50.0"), true);

        gestor.agregarServicio(servicio);

        //creamos un servicio extra con los cambios y usar el método modificar
        ServicioExtra servicioModificado = new ServicioExtra("GPS", new BigDecimal("15.0"), true);

        gestor.modificarServicio(servicioModificado);

        Optional<ServicioExtra> encontrado = gestor.buscarPorNombre("GPS");
        assertTrue(encontrado.isPresent());
        assertEquals(new BigDecimal("15.0"), encontrado.get().getCostoServicio());
    }

    @Test(expected = ServicioNoExistenteException.class)
    public void modificarServicioExtra_noExiste_lanzaExcepcion() {
        GestorServicioExtra gestor = new GestorServicioExtra();
        ServicioExtra servicioModificado = new ServicioExtra("GPS", new BigDecimal("15.0"), true);

        gestor.modificarServicio(servicioModificado);
    }

    /*************************************************************************************************** */
    /*                                 TEST ELIMINAR SERVICIO EXTRA                                               */
    /*************************************************************************************************** */
    @Test
    public void eliminarServicioExtra_exitoso() {
        GestorServicioExtra gestor = new GestorServicioExtra();
        ServicioExtra servicio = new ServicioExtra("GPS", new BigDecimal("50.0"), true);

        gestor.agregarServicio(servicio);
        gestor.eliminarServicio(servicio);

        Optional<ServicioExtra> encontrado = gestor.buscarPorNombre("GPS");
        assertFalse(encontrado.isPresent());
    }
    @Test(expected = ServicioNoExistenteException.class)
    public void eliminarServicioExtra_noExiste_lanzaExcepcion() {
        GestorServicioExtra gestor = new GestorServicioExtra();
        ServicioExtra servicio = new ServicioExtra("GPS", new BigDecimal("50.0"), true);

        gestor.eliminarServicio(servicio);
    }

    /*************************************************************************************************** */
    /*                                 TEST LISTAR SERVICIOS EXTRA                                               */
    /*************************************************************************************************** */
    @Test
    public void listarServiciosExtra_exitoso() {
        GestorServicioExtra gestor = new GestorServicioExtra();
        ServicioExtra servicio = new ServicioExtra("GPS", new BigDecimal("50.0"), true);
        ServicioExtra servicio2 = new ServicioExtra("AIRE", new BigDecimal("20.0"), true);

        gestor.agregarServicio(servicio);
        gestor.agregarServicio(servicio2);

        List<ServicioExtra> disponibles = gestor.listarServicios();

        assertEquals(2, disponibles.size());
    }
}
