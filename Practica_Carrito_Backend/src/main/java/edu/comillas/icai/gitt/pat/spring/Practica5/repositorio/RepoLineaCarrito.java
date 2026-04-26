package edu.comillas.icai.gitt.pat.spring.Practica5.repositorio;

import edu.comillas.icai.gitt.pat.spring.Practica5.entity.Carrito;
import edu.comillas.icai.gitt.pat.spring.Practica5.entity.LineaCarrito;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RepoLineaCarrito extends CrudRepository<LineaCarrito, Long> {
// Por si quiero buscar por algo que no sea la PK
    LineaCarrito findByIdCarritoAndIdArticulo(Carrito idCarrito, Long idArticulo);
    List<LineaCarrito> findByIdCarrito_IdCarrito(Long idCarrito); // Para obtener las lineas de un carrito especifico
    /*Dame todas las LineaCarrito cuyo LineaCarrito.idCarrito.idCarrito == idCarrito*/
}
