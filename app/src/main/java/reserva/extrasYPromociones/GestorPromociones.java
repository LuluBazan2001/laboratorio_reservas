package reserva.extrasYPromociones;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import reserva.extrasYPromociones.excepciones.*;

public class GestorPromociones {
    private List<Promocion> promociones = new ArrayList<>();

    public void agregarPromocion(Promocion promo) {
        if (promociones.contains(promo)) {
            throw new PromocionExistenteException("La promoción ya existe: " + promo.getNombre());
        }
        promociones.add(promo);
    }

    public void eliminarPromocion(Promocion promo) {
        if(!promociones.contains(promo)){
            throw new PromocionNoExistenteException("La promoción no existe: " + promo.getNombre());
        }
        promociones.remove(promo);
    }

    public void modificarPromocion(Promocion promoNueva) {
        if(!promociones.contains(promoNueva)){
            throw new PromocionNoExistenteException("La promoción no existe: " + promoNueva.getNombre());
        }
        eliminarPromocion(promoNueva);
        promociones.add(promoNueva);
    }

    public List<Promocion> listarPromociones() {
        return Collections.unmodifiableList(promociones);
    }

    public Optional<Promocion> buscarPorNombre(String nombre) {
        return promociones.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }
}
