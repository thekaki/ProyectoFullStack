import React from "react";

import { useState, useEffect } from "react";
import { Global } from "../../helpers/Global";
import { Peticion } from "../../helpers/Peticion";
import { Listado } from "./Listado";

export const Articulos = () => {

    const [articulos, setArticulos] = useState([]);
    const [cargando, setCargando] = useState(true);

    useEffect(() => {
        coneguirArticulos();
    }, [])

    const coneguirArticulos = async () => {

        let token = "eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoicGFjbyIsInN1YiI6InBhY29AZ21haWwuY29tIiwiaWF0IjoxNzU4NjE3NTMzLCJleHAiOjE3NTg3MDM5MzN9.rX-MuWOfvB8_4_213YOD3K51mjywzGDV5s4Wq4DHiZA"

        const { datos, cargando } = await Peticion(Global.url + "api/articulos", "GET", token)

        setArticulos(datos);
        setCargando(cargando);
        console.log(datos)
    }

    return (
        <>
            {cargando ? "Cargando..." :

                articulos.length >= 1 ? <Listado articulos={articulos} setArticulos={setArticulos} /> : <h1>No hay articulos</h1>

            }
        </>
    )
}