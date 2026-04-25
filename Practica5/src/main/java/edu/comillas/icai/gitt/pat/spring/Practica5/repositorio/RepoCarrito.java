package edu.comillas.icai.gitt.pat.spring.Practica5.repositorio;

import edu.comillas.icai.gitt.pat.spring.Practica5.entity.Carrito;
import org.springframework.data.repository.CrudRepository;

public interface RepoCarrito extends CrudRepository<Carrito, Long> {
    Carrito findByIdCarrito(Long idCarrito);
}
