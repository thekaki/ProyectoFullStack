import React from "react";

import { useNavigate, useLocation } from "react-router-dom";
import { useState, useEffect } from "react";
import { Global } from "../../../helpers/Global";
import { Peticion } from "../../../helpers/Peticion";
import { ListadoArticulos } from "./ListadoArticulos";

export const Articulos = () => {

    const [articulos, setArticulos] = useState([]);
    const [cargando, setCargando] = useState(true);

    const navigate = useNavigate();
    const location = useLocation();

    useEffect(() => {
        // Cada vez que cambia la URL (ej: /articulos?titulo=x) se vuelve a ejecutar
        const params = new URLSearchParams(location.search);
        const termino = params.get("titulo");
        console.log(location)
        console.log(params)
        console.log(termino)

        if (termino) {
            // buscarArticulos(termino);
            buscarArticulos(location.search);
        } else {
            coneguirArticulos();
        }
    }, [location.search]);

    //Muestra todos los articulos de la bbdd
    const coneguirArticulos = async () => {

        const { datos, cargando } = await Peticion(Global.url + "api/articulos", "GET", Global.token)

        setArticulos(datos);
        setCargando(cargando);
    }

    //Carga articulos filtrados por titulo
    const buscarArticulos = async (termino) => {
        if (!termino.trim()) {
            coneguirArticulos();
            return;
        }

        const { datos } = await Peticion(`${Global.url}api/articulos/buscar${termino}`, "GET", Global.token);

        setArticulos(datos);
        setCargando(false);
    };

    const handleEdit = (id) => {
        navigate(`/articulos/${id}`);
    };

    const eliminar = async (id) => {
        let { status } = await Peticion(`${Global.url}api/articulos/${id}`, "DELETE", Global.token)

        if (status === 200) {
            navigate('/articulos');
        }
    }

    //Usuario mockeado
    const user = {
        rol: "user",
        id: "1234"
    }

    return (
        <>
            {cargando ? "Cargando..." :

                articulos.length >= 1 ? <ListadoArticulos articulos={articulos} handleEdit={handleEdit} eliminar={eliminar} setArticulos={setArticulos} user={user} /> : <h1>No hay articulos</h1>

            }
        </>
    )
}