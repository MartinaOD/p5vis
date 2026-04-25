// Podemos generar un único JS para todos pero hemos de poner distintos ids para cada cosa

// Declaramos los objetos que voy a usar
const crear = document.getElementById("formCrear");
const salida = document.getElementById("salida");
const tablaCesta = document.getElementById("cesta_compra_dinamica");

const baseUrl = "http://localhost:8080";
    

/*
Crear un carrito al hacer load --> se crea el id
Añado el artículo
Mostrar con función JS el mensaje de que se ha añadido al carrito durante 5 segs
*/


// Funciones útiles:

function getBaseUrl() {
    return baseUrl;
}


function mostrarSalida(datos) {
    if (typeof datos === "string") {
        salida.textContent = datos;
    } else {
        salida.textContent = JSON.stringify(datos, null, 2);
    }
}

// Para que luego se pueda usar con await
async function procesarRespuesta(response) {
    const texto = await response.text();

    let datos;
    try {
        datos = texto ? JSON.parse(texto) : null; // Si está bien, convierto el texto (String) en JSON
    } catch {
        datos = texto;
    }

    if (!response.ok) {
        const mensaje = typeof datos === "object" && datos !== null
            ? JSON.stringify(datos)
            : texto || `HTTP ${response.status}`;
        throw new Error(mensaje);
    }
    return datos;
}

function mostrarError(error) {
    //salida.textContent = `Error: ${error.message}`;
}


// // Creamos el carrito al cargar la página --> Ponerlo en la pág específica
// document.addEventListener("DOMContentLoaded", () =>
//     {
//         // Como segundo parámetro del addEventListener, defino la función que se ejecutará
//         inicializarCarrito();
//     }
// );

/* Generar variables de usuario y correo */
async function inicializarCarrito(){
    try{
        // Intentamos crear el Carrito
        let url = baseUrl + "/api/carrito";
        alert(url);

        // Llamo a la API:
        const response = await fetch(url, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({
                idUsuario: 5,
                correoUsuario: "martina@gmail.com",
                precioTotal: 0
            }) // Me creo unos valores iniciales por defecto
        });
    
        // Response poseerá la respuesta HTTP
        const datos_carrito = await procesarRespuesta(response); // Espero la respuesta

        // Guardo el id del carrito para luego usarlo:
        localStorage.setItem("idCarrito", datos_carrito.idCarrito);

        mostrarSalida(datos_carrito);       
    
    } catch (error) {
        mostrarError(error); 
    }
}


// Para crear una línea de Carrito (al clicar sobre "Añadir al carrito"):

async function crearLinea(productoId, precioUnitario, cantidad){
    // Al añadir un producto a nuestra cesta
    //alert('El id del producto seleccionado: '+ productoId);
    //alert('La cantidad del producto seleccionado: '+ cantidad);

    try{
        // Intentamos crear la Linea, la cual debe tener un carrito
        const idCarrito = localStorage.getItem("idCarrito");
        
        if (!idCarrito){
            throw new Error("No existe carrito activo");
        }

        let url = baseUrl + "/api/carrito/" + idCarrito;

        const body = JSON.stringify({
            idArticulo: productoId,
            precioUnitario: precioUnitario,
            numUnidades: cantidad,
            costeLinea: precioUnitario*cantidad,
            idCarrito: idCarrito
        });

        console.log(body);

        // Llamo a la API (con el idCarrito guardado en localStorage):
        const response = await fetch(url, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: body
        });
    
        // Response poseerá la respuesta HTTP
        const datos_linea = await procesarRespuesta(response); // Espero la respuesta
        mostrarSalida(datos_linea);
    } catch (error) {
        mostrarError(error);
    }
}


/*
Actualizar el carrito -> añadir producto
Borrar línea carrito -> borrar toda la cesta
*/


function EnviarArticulo(boton){
    let productoId = Number(boton.id);
    //alert('Id producto: ' + productoId);
    let cajaId = "txt_" + productoId;
    var caja = document.getElementById(cajaId);
    let cantidad = Number(caja.value);

    let precioId = "lbl_" + productoId;
    var etiquetaPrecio = document.getElementById(precioId);
    //alert('Cantidad: ' + caja.value);
    alert('Precio: ' + etiquetaPrecio.textContent);

    crearLinea(productoId, Number(etiquetaPrecio.textContent), cantidad);  
      
}

// Crear tabla dinámica que muestre la cesta con sus artículos según se vayan añadiendo
async function mostrarCesta() {
    try {
        const idCarrito = localStorage.getItem("idCarrito");

        if (!idCarrito) {
            throw new Error("No existe carrito activo");
        }

        let url = baseUrl + "/api/carrito/lineas/" + idCarrito;

        const response = await fetch(url,
            { method: "GET" }
        );

        const lineas = await procesarRespuesta(response);

        const tbody = document.getElementById("cesta_compra_dinamica");
        tbody.innerHTML = ""; // Limpio la tabla

        if (Array.isArray(lineas) && lineas.length > 0) {
            lineas.forEach(linea => {
                const fila = document.createElement("tr");
                fila.innerHTML = `
                    <td>${linea.idArticulo}</td>
                    <td>${linea.numUnidades}</td>
                    <td>${linea.precioUnitario ?? ""} €</td>
                    <td>${linea.costeLinea ?? ""} €</td>
                    <td>
                        <button onclick="borrarLinea(${linea.id})">
                            ❌
                        </button>
                    </td>
                `;
                tbody.appendChild(fila);
            });
        }
    } catch (error) {
        mostrarError(error);
    }
}



// Borrar producto:

async function borrarLinea(lineaId) {
    try {
        if (!lineaId) {
            throw new Error("No existe la linea");
        }

        let url = baseUrl + "/api/carrito/borrarLinea/" + lineaId;

        const response = await fetch(url,
            { method: "DELETE" }
        );

        const respuesta_nula = await procesarRespuesta(response);
        await mostrarCesta(); // Refrescar la tabla, volvemos a pintarla
        mostrarSalida(respuesta_nula);

    } catch (error) {
        mostrarError(error);
    }
}

// Envio mensaje al pagar

function pagar() {
    const mensaje = document.getElementById("mensajePago");
    mensaje.style.display = "block";

    setTimeout(() => {
        mensaje.style.display = "none";
    }, 3000);
}
