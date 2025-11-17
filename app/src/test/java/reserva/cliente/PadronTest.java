package reserva.cliente;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

import reserva.cliente.excepciones.*;

public class PadronTest {
    /*                     REGISTRO DE CLIENTES                      */
    @Test
    public void testRegistrarClienteSinDuplicado(){
        Padron padron = new Padron();
        ClienteParticular c1 = new ClienteParticular("Jorge", "Catamarca", "3834521547", "ajshdashd@gmail.com", "44521125");
        ClienteParticular c2 = new ClienteParticular("Lourdes", "Catamarca", "3834845164", "ajshgduyqguyqdhi@gmail.com", "12345678");

        padron.registrarCliente(c1);
        padron.registrarCliente(c2);

        assertEquals(2, padron.getClientes().size());
        assertTrue(padron.comprobarDuplicado("44521125"));
        assertTrue(padron.comprobarDuplicado("12345678"));
    }
    @Test (expected = ClienteDuplicadoException.class)
    public void registrarClienteSinDuplicado_Rechaza_lanzaException(){
        Padron padron = new Padron();
        ClienteParticular c1 = new ClienteParticular("Jorge", "Catamarca", "3834521547", "ajshdashd@gmail.com", "44521125");
        ClienteParticular c2 = new ClienteParticular("Lourdes", "Catamarca", "3834845164", "ajshgduyqguyqdhi@gmail.com", "44521125");
        
        padron.registrarCliente(c1);
        padron.registrarCliente(c2); //Aqui deberia lanzar una excepcion de ClienteDuplicadoException
    }
    @Test (expected = ClienteNuloException.class)
    public void registrarClienteNulo_lanzaException(){
        Padron padron = new Padron();
        ClienteParticular c1 = null;
        
        padron.registrarCliente(c1); //Aqui deberia lanzar una excepcion de ClienteNuloException
    }

 /********************************************************************************************************************************/
    /*                     VERIFICACIÓN DE DUPLICADOS                      */
    @Test
    public void testVerificarClienteDuplicado(){
        Padron padron = new Padron();
        ClienteParticular c1 = new ClienteParticular("Jorge", "Catamarca", "3834521547", "ajshdashd@gmail.com", "44521125");

        padron.registrarCliente(c1);
        

        assertTrue(padron.comprobarDuplicado("44521125"));
        assertFalse(padron.comprobarDuplicado("00000000")); //El dni no existe
    }

 /********************************************************************************************************************************/
    /*                     BUSQUEDA POR NOMBRE                      */
    @Test
    public void testBuscarPorNombre_devuelveClientesConNombreEspecifico() {
        Padron padron = new Padron();
        ClienteParticular c1 = new ClienteParticular("Jorge", "Catamarca", "3834521547", "ajshdashd@gmail.com", "44521125");
        ClienteEmpresarial c2 = new ClienteEmpresarial("Technical", "Catamarca - Santa Cruz 555", "3834324512", "technical@gmail.com", "23-11234567-1", "Technical");

        padron.registrarCliente(c1);
        padron.registrarCliente(c2);

        List<Cliente> clientes = padron.buscarPorNombre("Jorge");
        List<Cliente> clientes2 = padron.buscarPorNombre("Technical");

        assertEquals(1, clientes.size());
        assertEquals("Retorna un cliente con nombre Jorge", "Jorge", clientes.get(0).getNombre());

        assertEquals(1, clientes2.size());
        assertEquals("Retorna un cliente con nombre Technical", "Technical", clientes2.get(0).getNombre());
    }
    @Test
    public void testBuscarPorNombre_RechazaInexistente(){
        Padron padron = new Padron();
        ClienteParticular c1 = new ClienteParticular("Jorge", "Catamarca", "3834521547", "ajshdashd@gmail.com", "44521125");
        ClienteEmpresarial c2 = new ClienteEmpresarial("Technical", "Catamarca - Santa Cruz 555", "3834324512", "technical@gmail.com", "23-11234567-1", "Technical");

        padron.registrarCliente(c1);
        padron.registrarCliente(c2);

        List<Cliente> sinResultados = padron.buscarPorNombre("Inexistente");
        assertTrue("No deberia devolver resultados para un nombre inexistente", sinResultados.isEmpty());
    }

/********************************************************************************************************************************/
    /*                     BUSQUEDA POR RAZON SOCIAL                      */
    @Test
    public void testBuscarPorRazonSocial_devuelveClientesEmpresariales() {
        Padron padron = new Padron();
         ClienteEmpresarial c1 = new ClienteEmpresarial("Technical", "Catamarca - Santa Cruz 555", "3834324512", "technical@gmail.com", "23-11234567-1", "Technical");

         padron.registrarCliente(c1);

         List<ClienteEmpresarial> clientesEmpresariales = padron.buscarPorRazonSocial("Technical");

         assertEquals(1, clientesEmpresariales.size());
         assertEquals("Retorna un cliente con razon social Technical", "Technical", clientesEmpresariales.get(0).getRazonSocial());
    }
    @Test
    public void testBuscarPorRazonSocial_RechazaInexistente(){
        Padron padron = new Padron();
         ClienteEmpresarial c1 = new ClienteEmpresarial("Technical", "Catamarca - Santa Cruz 555", "3834324512", "technical@gmail.com", "23-11234567-1", "Technical");

         padron.registrarCliente(c1);

         List<ClienteEmpresarial> sinResultados = padron.buscarPorRazonSocial("Empresa Fantasma");
         assertTrue("No deberia devolver resultados para un razon social inexistente", sinResultados.isEmpty());
    }

 /********************************************************************************************************************************/
    /*                     BUSQUEDA POR IDENTIFICACIÓN                      */
    @Test
    public void testBuscarPorIdentificacion_devuelveClienteParticular() {
        Padron padron = new Padron();
        ClienteParticular c1 = new ClienteParticular("Jorge", "Catamarca", "3834521547", "ajshdashd@gmail.com", "44521125");

        padron.registrarCliente(c1);

        Optional<Cliente> cliente = padron.buscarPorIdentificacion("44521125");

        assertTrue(cliente.isPresent());
        assertTrue("El cliente encontrado deberia ser un cliente particular", cliente.get() instanceof ClienteParticular);

        ClienteParticular encontrado = (ClienteParticular) cliente.get();

        assertEquals("Retorna un cliente con dni 44521125", "44521125", encontrado.getDni()); //Valido el dni
        assertEquals("Retorna un cliente con el nombre Jorge", "Jorge", encontrado.getNombre()); //Para reforzar la busqueda, se comprueba que el nombre es correcto
    }
    @Test
    public void testBuscarPorIdentificacion_RechazaInexistente(){
        Padron padron = new Padron();
        ClienteParticular c1 = new ClienteParticular("Jorge", "Catamarca", "3834521547", "ajshdashd@gmail.com", "44521125");

        padron.registrarCliente(c1);

        Optional<Cliente> noEncontrado = padron.buscarPorIdentificacion("9999999999");
        assertFalse("No deberia devolver resultados para un dni inexistente", noEncontrado.isPresent());
    }

 /********************************************************************************************************************************/
    /*                     BUSQUEDA POR IDENTIFICACIÓN                      */
    @Test
    public void testBuscarPorIdentificacion_devuelveClienteEmpresarial() {
        Padron padron = new Padron();
        ClienteEmpresarial c1 = new ClienteEmpresarial("Technical", "Catamarca - Santa Cruz 555", "3834324512", "technical@gmail.com", "23-11234567-1", "Technical");

        padron.registrarCliente(c1);

        Optional<Cliente> cliente = padron.buscarPorIdentificacion("23-11234567-1");

        assertTrue(cliente.isPresent());
        assertTrue("El cliente encontrado debería ser empresarial", cliente.get() instanceof ClienteEmpresarial);
        
        ClienteEmpresarial encontrado = (ClienteEmpresarial) cliente.get();

        assertEquals("Retorna un cliente con cuit 23-11234567-1", "23-11234567-1", encontrado.getCuit()); //Valido el cuit
        assertEquals("Retorna un cliente con el nombre Technical", "Technical", encontrado.getNombre()); //Para reforzar la busqueda, se comprueba que el nombre es correcto    
    }
    @Test
    public void testBuscarPorIdentificacion_devuelveClienteEmpresarial_Rechaza(){
        Padron padron = new Padron();
        ClienteEmpresarial c1 = new ClienteEmpresarial("Technical", "Catamarca - Santa Cruz 555", "3834324512", "technical@gmail.com", "23-11234567-1", "Technical");

        padron.registrarCliente(c1);

        Optional<Cliente> noEncontrado = padron.buscarPorIdentificacion("30-9999999999-1");
        assertFalse("No deberia devolver resultados para un cuit inexistente", noEncontrado.isPresent());
    }

 /********************************************************************************************************************************/
    /*                     MODIFICACIÓN DE CLIENTES                      */
    @Test
    public void testModificarCliente(){
        Padron padron = new Padron();
        ClienteParticular c1 = new ClienteParticular("Jorge", "Catamarca", "3834521547", "ajshdashd@gmail.com", "44521125");
        ClienteParticular c2 = new ClienteParticular("Juan", "Cordoba 333", "3834934912", "juan@gmail.com", "44521125");

        //Registro el nuevo cliente
        padron.registrarCliente(c1);

        //Modifico el cliente con identificacion 44521125
        padron.modificarCliente(c2);

        //Verifico que el cliente se modificó
        Optional<Cliente> resultado = padron.buscarPorIdentificacion("44521125");
        assertTrue("El cliente deberia haberse modificado", resultado.isPresent());

        //Con esta instrucción se obtiene el cliente modificado, con los nuevos datos
        ClienteParticular clienteFinal = (ClienteParticular) resultado.get();

        //Comparo los nuevos datos
        assertEquals("El cliente deberia tener el nombre modificado", "Juan", clienteFinal.getNombre());
        assertEquals("El cliente deberia tener el email modificado", "juan@gmail.com", clienteFinal.getEmail());
        assertEquals("El cliente deberia tener el telefono modificado", "3834934912", clienteFinal.getTelefono());
        assertEquals("El cliente deberia tener la direccion modificada", "Cordoba 333", clienteFinal.getDireccion());
    }
    @Test (expected = ClienteNoEncontradoException.class)
    public void modificarCliente_noExiste_lanzaException(){
        Padron padron = new Padron();
        ClienteParticular c1 = new ClienteParticular("Jorge", "Catamarca", "3834521547", "ajshdashd@gmail.com", "44521125");

        padron.modificarCliente(c1); //Aqui deberia lanzar una excepcion de ClienteNoEncontradoException
    }

 /********************************************************************************************************************************/
    /*                     ELIMINACIÓN DE CLIENTES                      */
    @Test
    public void testEliminarCliente(){
        Padron padron = new Padron();
        ClienteParticular c1 = new ClienteParticular("Jorge", "Catamarca", "3834521547", "ajshdashd@gmail.com", "44521125");
        ClienteParticular c2 = new ClienteParticular("Juan", "Cordoba 333", "3834934912", "juan@gmail.com", "43224512");

        //Registro los nuevos clientes
        padron.registrarCliente(c1);
        padron.registrarCliente(c2);

        //elimino el cliente con identificacion 44521125
        padron.eliminarCliente("44521125");

        //Verifico que quede un solo cliente en la lista
        assertEquals(1, padron.getClientes().size());
    }
    @Test (expected = ClienteNoEncontradoException.class)
    public void testEliminarCliente_noExiste_lanzaException(){
        Padron padron = new Padron();
        ClienteParticular c1 = new ClienteParticular("Jorge", "Catamarca", "3834521547", "ajshdashd@gmail.com", "44521125");

        padron.registrarCliente(c1);

        padron.eliminarCliente("00000000"); //Aqui deberia lanzar una excepcion de ClienteNoEncontradoException
    }
}
