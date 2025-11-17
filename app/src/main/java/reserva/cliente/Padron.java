package reserva.cliente;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import reserva.cliente.excepciones.*;

public class Padron {
    private List<Cliente> clientes = new ArrayList<>(); 

    /******************************************** */
    /*           REGISTRO DE CLIENTES            */
    /******************************************** */
    public void registrarCliente(Cliente cliente){
        if(cliente == null) {
            throw new ClienteNuloException("El cliente no puede ser nulo");
        }
        String id = cliente.getIdentificacion();
        if(clientes.stream().anyMatch(c -> c.getIdentificacion().equals(id))) {
            throw new ClienteDuplicadoException("El cliente con identificacion " + id + " ya está registrado.");
        }
        clientes.add(cliente);
    }

    /******************************************** */
    /*           LISTADO DE CLIENTES              */
    /******************************************** */
    public List<Cliente> getClientes(){
        //unmodifiableList devuelve una vista no modificable (de solo lectura) de la lista especificada.
        return Collections.unmodifiableList(clientes);
    }

    /******************************************** */
    /*           COMPROBACION DE DUPLICADOS       */
    /******************************************** */
    public boolean comprobarDuplicado(String id){
        //Este metodo no solo nos permite comprobar si hay algun cliente duplicado, sino que nos permite verificar si el cliente que recibe como duplicado existe en la coleccion.

        
        //stream.anyMatch(recibe un predicado) devuelve si algun elemento de este flujo coincide con el predicado proporcionado.
        return clientes.stream().anyMatch(c -> c.getIdentificacion().equals(id));
    }

    /********************************************************* */
    /*                     BUSQUEDAS                          */
    /********************************************************* */
    public List<Cliente> buscarPorNombre(String nombre) {
        //equalsIgnoreCase es un metodo que compara dos cadenas y devuelve true si son iguales, ignorando mayúsculas y minúsculas
        //toList devuelve una lista con todos los elementos de la colección
        return clientes.stream().filter(a -> a.getNombre().equalsIgnoreCase(nombre)).toList();
    }

    public List<ClienteEmpresarial> buscarPorRazonSocial(String razonSocial) {
        //.map devuelve una nueva colección de elementos, transformando cada elemento con el método dado (casteo explicito)
        //toList devuelve una lista con todos los elementos de la colección
        //filter devuelve una nueva colección de elementos que cumplen la condición especificada
        //.stream devuelve un flujo de elementos
        return clientes.stream().filter(c -> c instanceof ClienteEmpresarial ce && ce.getRazonSocial().equalsIgnoreCase(razonSocial)).map(ce -> (ClienteEmpresarial) ce).toList();
    }

    public Optional<Cliente> buscarPorIdentificacion (String id) {
        //optional permite que un objeto sea nulo
        //findFirst devuelve la primera coincidencia encontrada en la colección
        return clientes.stream()
                   .filter(c -> c.getIdentificacion().equals(id))
                   .findFirst();
    }


    /********************************************************* */
    /*             MODIFICAR Y ELIMINAR CLIENTES                */
    /********************************************************* */
    public void modificarCliente(Cliente nuevoCliente) {
        //Busca el cliente con la identificacion que brinda el parametro que recibe el metodo
        Optional<Cliente> cliente = buscarPorIdentificacion(nuevoCliente.getIdentificacion());

        //Si el cliente no existe, no se puede modificar
        if(cliente.isEmpty()) {
            throw new ClienteNoEncontradoException("No se puede modificar: El cliente no existe");
        }

        //Si el cliente existe, se modifica
        clientes.remove(cliente.get());
        clientes.add(nuevoCliente);
    }

    public void eliminarCliente(String identificacion) {
        Optional<Cliente> cliente = buscarPorIdentificacion(identificacion);
        if(cliente.isEmpty()) {
            throw new ClienteNoEncontradoException("No se puede eliminar: El cliente no existe");
        }

        clientes.remove(cliente.get());
    }

}
