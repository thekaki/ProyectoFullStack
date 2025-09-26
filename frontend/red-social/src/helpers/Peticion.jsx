export const Peticion = async (url, metodo, token, datosGuardar = " ") => {

    let cargando = true;

    let opciones = {
        method: "GET"
    };

    if (metodo == "GET" || metodo == "DELETE") {
        opciones = {
            method: metodo,
            headers: {
                "Authorization": "Bearer " + token
            }
        };
    }

    if (metodo == "POST" || metodo == "PUT") {
        opciones = {
            method: metodo,
            body: JSON.stringify(datosGuardar),
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            }
        };

    }
    const peticion = await fetch(url, opciones);
    const datos = await peticion.json();

    cargando = false;

    return {
        status: peticion.status,
        datos: datos.data,
        cargando
    }
}