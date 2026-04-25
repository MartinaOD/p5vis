package edu.comillas.icai.gitt.pat.spring.Practica5.controlador;

import edu.comillas.icai.gitt.pat.spring.Practica5.entity.Carrito;
import edu.comillas.icai.gitt.pat.spring.Practica5.entity.LineaCarrito;
import edu.comillas.icai.gitt.pat.spring.Practica5.servicio.ServicioCarrito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ControladorREST {
    // Aquí será donde me encargue de realizar las operaciones CRUD sobre Carrito

    @Autowired
    ServicioCarrito servicioCarrito;

    // Para crear el Carrito:
    @PostMapping("/api/carrito") // Añade un carrito nuevo o crear
    // RequestBody, que reciba la petición como un JSON
    public Carrito crea(@RequestBody Carrito nuevoCarro){
        return servicioCarrito.crea(nuevoCarro);
    }

    // Para obtener información de un determinado Carrito:
    @GetMapping("/api/carrito/{idCarrito}") // Devuelve los datos del carrito
    public Carrito informacion(@PathVariable Long idCarrito){
        return servicioCarrito.informacion(idCarrito);
    }

    // Para actualizar el Carrito:
    @PutMapping("/api/carrito/{idCarrito}")
    public Carrito add(@RequestBody Carrito carritoActualizado, @PathVariable Long idCarrito){
        return  servicioCarrito.actualizar(carritoActualizado, idCarrito);
    }

    // Para borrar un Carrito:
    @DeleteMapping("/api/carrito/borrarCarrito/{idCarrito}") // Elimino carrito
    public void borrar(@PathVariable Long idCarrito){
        servicioCarrito.borrar(idCarrito);
    }

    // Para añadir una Linea de Carro:
    @PostMapping("/api/carrito/{idCarrito}")
    public Carrito nuevaLinea(@RequestBody LineaCarrito lineaNueva, @PathVariable Long idCarrito){
        System.out.println(lineaNueva.getNumUnidades());
        return servicioCarrito.creaLinea(lineaNueva, idCarrito);
    }

    // Para obtener líneas de un determinado Carrito:
    @GetMapping("/api/carrito/lineas/{idCarrito}") // Devuelve los datos del carrito
    public List<LineaCarrito> obtenerLineas(@PathVariable Long idCarrito){
        return servicioCarrito.obtenerLineas(idCarrito);
    }


    // Para borrar una Línea de Carro:
    @DeleteMapping("/api/carrito/borrarLinea/{idLinea}") // Elimino producto del carrito
    public void borrarLinea(@PathVariable Long idLinea) {
        try{
            servicioCarrito.borrarLinea(idLinea);
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
