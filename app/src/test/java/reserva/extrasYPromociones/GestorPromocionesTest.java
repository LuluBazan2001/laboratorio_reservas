package reserva.extrasYPromociones;

import org.junit.Test;

import reserva.extrasYPromociones.excepciones.PromocionExistenteException;
import reserva.extrasYPromociones.excepciones.PromocionNoExistenteException;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

public class GestorPromocionesTest {
    /*************************************************************************************************** */
    /*                                 TEST AGREGAR PROMOCION                                               */
    /*************************************************************************************************** */
    @Test
    public void agregarPromocion_exitoso() {
        GestorPromociones gestor = new GestorPromociones();
        Promocion promo = new Promocion("Promocion 1", new BigDecimal(0.10), true, false, false, "La promocion incluye un descuento de 10%");

        gestor.agregarPromocion(promo);

        //Con encontrada verificamos que la promocion se ha agregado correctamente
        Optional<Promocion> encontrada = gestor.buscarPorNombre("Promocion 1");
        //Verificamos que la promocion se ha agregado correctamente
        assertTrue("La promoción debe estar presente después de agregarla", encontrada.isPresent());
        assertEquals("Promocion 1", encontrada.get().getNombre());
    }
    @Test(expected = PromocionExistenteException.class)
    public void agregarPromocion_existente_lanzaExcepcion() {
        GestorPromociones gestor = new GestorPromociones();
        Promocion promo = new Promocion("Promocion 1", new BigDecimal(0.10), true, false, false, "La promocion incluye un descuento de 10%");
        Promocion promoDuplicada = new Promocion("Promocion 1", new BigDecimal(0.10), true, false, false, "La promocion incluye un descuento de 10%");

        gestor.agregarPromocion(promo);
        //Intento agregar otra con el mismo nombre -> debe lanzar excepción
        gestor.agregarPromocion(promoDuplicada);
    }

    /*************************************************************************************************** */
    /*                                 TEST MODIFICAR PROMOCION                                               */
    /*************************************************************************************************** */

    @Test
    public void modificarPromocion_exitoso() {
        GestorPromociones gestor = new GestorPromociones();
        Promocion promo = new Promocion("Promocion 1", new BigDecimal(0.10), true, false, false, "La promocion incluye un descuento de 10%");

        gestor.agregarPromocion(promo);

        //creamos una promoción con los cambios y usamos el método modificar
        Promocion promoModificada = new Promocion("Promocion 1", new BigDecimal(0.10), true, true, true, "La promocion incluye un descuento de 10%");

        gestor.modificarPromocion(promoModificada);

        Optional<Promocion> encontrada = gestor.buscarPorNombre("Promocion 1");
        assertTrue(encontrada.isPresent());
        assertEquals(new BigDecimal(0.10), encontrada.get().getPorcentajeDescuento());
    }

    @Test(expected = PromocionNoExistenteException.class)
    public void modificarPromocion_noExiste_lanzaExcepcion() {
        GestorPromociones gestor = new GestorPromociones();
        Promocion promoModificada = new Promocion("Promocion 1", new BigDecimal(0.15), true, false, false, "La promocion incluye un descuento de 15%");

        gestor.modificarPromocion(promoModificada);
    }

    /*************************************************************************************************** */
    /*                                 TEST ELIMINAR PROMOCION                                               */
    /*************************************************************************************************** */
    @Test
    public void eliminarPromocion_exitoso() {
        GestorPromociones gestor = new GestorPromociones();
        Promocion promo = new Promocion("Promocion 1", new BigDecimal(0.10), true, false, false, "La promocion incluye un descuento de 10%");

        gestor.agregarPromocion(promo);
        gestor.eliminarPromocion(promo);

        Optional<Promocion> encontrada = gestor.buscarPorNombre("Promocion 1");
        assertFalse(encontrada.isPresent());
    }
    @Test(expected = PromocionNoExistenteException.class)
    public void eliminarPromocion_noExiste_lanzaExcepcion() {
        GestorPromociones gestor = new GestorPromociones();
        Promocion promo = new Promocion("Promocion 1", new BigDecimal(0.10), true, false, false, "La promocion incluye un descuento de 10%");

        gestor.eliminarPromocion(promo);
    }

    /*************************************************************************************************** */
    /*                                 TEST LISTAR PROMOCIONES                                               */
    /*************************************************************************************************** */
    @Test
    public void listarPromociones_exitoso() {
        GestorPromociones gestor = new GestorPromociones();
        Promocion promo = new Promocion("Promocion 1", new BigDecimal(0.10), true, false, false, "La promocion incluye un descuento de 10%");
        Promocion promo2 = new Promocion("Promocion 2", new BigDecimal(0.15), true, false, false, "La promocion incluye un descuento de 15%");
        gestor.agregarPromocion(promo);
        gestor.agregarPromocion(promo2);

        List<Promocion> disponibles = gestor.listarPromociones();

        assertEquals(2, disponibles.size());
    }
}
