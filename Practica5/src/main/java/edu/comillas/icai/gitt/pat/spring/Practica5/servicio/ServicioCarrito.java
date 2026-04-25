package edu.comillas.icai.gitt.pat.spring.Practica5.servicio;

import edu.comillas.icai.gitt.pat.spring.Practica5.entity.Carrito;
import edu.comillas.icai.gitt.pat.spring.Practica5.entity.LineaCarrito;
import edu.comillas.icai.gitt.pat.spring.Practica5.repositorio.RepoCarrito;
import edu.comillas.icai.gitt.pat.spring.Practica5.repositorio.RepoLineaCarrito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ServicioCarrito {
    @Autowired
    RepoCarrito repoCarrito;

    @Autowired
    RepoLineaCarrito repoLineaCarrito;

    // Creo un nuevo carrito, el cual guardaré en la tabla/entidad
    public Carrito crea(Carrito nuevoCarro) {
        if (repoCarrito.findByIdCarrito(nuevoCarro.idCarrito) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Carrito ya existente");
        }
        repoCarrito.save(nuevoCarro);
        return nuevoCarro;
    }

    // Obtengo la info de un carro concreto
    public Carrito informacion(Long idCarrito) {
        Carrito carroBuscado = repoCarrito.findByIdCarrito(idCarrito);
        if (carroBuscado == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado");
        }
        return carroBuscado;
    }

    // Actualizo un carrito
    public Carrito actualizar(Carrito carritoNew, Long idCarrito) {
        // Añadimos un producto al carrito, nueva LineaCarrito
        Carrito carritoActual = repoCarrito.findByIdCarrito(idCarrito);

        if (carritoActual == null) {
            throw new RuntimeException("Carrito no encontrado");
        }

        // Modifico el existente:
        carritoActual.setIdUsuario(carritoNew.getIdUsuario());
        carritoActual.setCorreoUsuario(carritoNew.getCorreoUsuario());
        carritoActual.setPrecioTotal(carritoNew.getPrecioTotal());

        // Guardo dichas modificaciones en BD:
        repoCarrito.save(carritoActual);
        return carritoActual;
    }

    // Borro un carrito ( y las líneas de carrito que este tenga con CASCADE)
    public void borrar(Long idCarrito){
        Carrito carrito = repoCarrito.findByIdCarrito(idCarrito);
        if (carrito == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado");
        }
        repoCarrito.delete(carrito);
    }

    // Añado/ creo línea carrito
    @Transactional
    public Carrito creaLinea(LineaCarrito lineaNueva, Long idCarrito) {
        // Primero busco el carro al que está asociada
        Carrito carritoBuscado = repoCarrito.findByIdCarrito(idCarrito);

        if (carritoBuscado == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado");
        }

        // Comprobamos si ya existe dicha línea con ese artículo:
        LineaCarrito lineaExistente = repoLineaCarrito.findByIdCarritoAndIdArticulo(carritoBuscado, lineaNueva.getIdArticulo());
        //lineaNueva.setCosteLinea(lineaNueva.getPrecioUnitario()*lineaNueva.getNumeroUnidades());

        System.out.println(lineaNueva.getNumUnidades());

        if (lineaExistente != null) {
            // Si la linea ya existe: sumo unidades del artículo
            lineaExistente.setNumUnidades(lineaExistente.getNumUnidades() + lineaNueva.getNumUnidades());
            double costeLinea_antes = lineaExistente.getCosteLinea();
            //lineaExistente.setCosteLinea(costeLinea_antes + lineaNueva.getNumeroUnidades() * lineaNueva.getPrecioUnitario());
            lineaExistente.setCosteLinea(costeLinea_antes + lineaNueva.getCosteLinea()); // Ya calculado de Frontend
            repoLineaCarrito.save(lineaExistente);

        } else {
            // Si no existe, hemos de crear una nueva línea
            lineaNueva.setCarrito(carritoBuscado);
            //lineaNueva.setCosteLinea(lineaNueva.getNumeroUnidades() * lineaNueva.getPrecioUnitario());

            repoLineaCarrito.save(lineaNueva);
        }
        // Actualizamos el precio total del carrito:
        carritoBuscado.setPrecioTotal(carritoBuscado.getPrecioTotal() + lineaNueva.getCosteLinea());
        repoCarrito.save(carritoBuscado);
        return carritoBuscado;
    }

    // Para obtener las líneas asociadas a un determinado carrito:
    public List<LineaCarrito> obtenerLineas(Long idCarrito){
        List<LineaCarrito> lineas = repoLineaCarrito.findByIdCarrito_IdCarrito(idCarrito);
        return lineas;
    }


    // Borro línea carrito
    @Transactional
    public void borrarLinea(Long idLinea){
        // En primer lugar, hemos de buscar la línea a borrar:
        LineaCarrito linea = repoLineaCarrito.findById(idLinea)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Línea no encontrada"));

        // Obtenemos el carrito asociado a ella:
        Carrito carrito = linea.getCarrito();

        // Restamos el coste de lo que quitamos:
        carrito.setPrecioTotal(carrito.getPrecioTotal() - linea.getCosteLinea());
        repoCarrito.save(carrito);

        // Borramos la línea:
        repoLineaCarrito.delete(linea);
    }
}
