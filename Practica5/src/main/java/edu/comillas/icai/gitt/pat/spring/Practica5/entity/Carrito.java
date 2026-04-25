package edu.comillas.icai.gitt.pat.spring.Practica5.entity;

import jakarta.persistence.*;

@Entity
public class Carrito{
    @Id // Indico que idCarrito será la primary key de esta tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY) public Long idCarrito; // Generado aautom. por BD

    // No imponemos condiciones pues no se nos indica ninguna:
    @Column private Long idUsuario;
    @Column private String correoUsuario;
    @Column private double precioTotal;

    // Aquí poner los set o get necesarios
    public Long getIdCarrito(){
        return idCarrito;
    }
    public Long getIdUsuario(){
        return idUsuario;
    }
    public String getCorreoUsuario(){
        return correoUsuario;
    }
    public double getPrecioTotal(){
        return precioTotal;
    }

    public void setIdCarrito(Long idCarrito){
        this.idCarrito = idCarrito;
    }
    public void setIdUsuario(Long idUsuario){
        this.idUsuario = idUsuario;
    }
    public void setCorreoUsuario(String correoUsuario){
        this.correoUsuario = correoUsuario;
    }
    public void setPrecioTotal(double precioTotal){
        this.precioTotal = precioTotal;
    }


}


