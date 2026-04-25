package edu.comillas.icai.gitt.pat.spring.Practica5.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity // indico que es tabla/entidad
public class LineaCarrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    // No imponemos condiciones pues no se nos indica ninguna:

    // Para un carrito puedo tener muchos artículos, muchas líneas de carrito
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(optional = false) private Carrito idCarrito;

    @Column private Long idArticulo;
    @Column private Long precioUnitario;
    @Column private int numUnidades;
    @Column private double costeLinea;

    // Aquí poner los set o get necesarios
    public Long getIdArticulo(){
        return idArticulo;
    }
    public int getNumUnidades(){ return numUnidades;}
    public double getPrecioUnitario(){ return precioUnitario;}
    public double getCosteLinea(){ return costeLinea;}
    public Carrito getCarrito(){ return this.idCarrito;}

    public void setNumUnidades(int numUnidades){
        this.numUnidades = numUnidades;
    }
    public void setCosteLinea(double costeLinea){
        this.costeLinea = costeLinea;
    }
    public void setCarrito(Carrito carrito){
        this.idCarrito = carrito;
    }

}
