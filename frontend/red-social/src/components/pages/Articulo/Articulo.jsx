import React from "react";

import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom"
import { Peticion } from "../../../helpers/Peticion";
import { Global } from "../../../helpers/Global";

export const Articulo = () => {

    const { id } = useParams();
    const [articulo, setArticulo] = useState(null);
    const [cargando, setCargando] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        getArticulo();
    }, [id]);

    const getArticulo = async () => {

        const { datos, cargando } = await Peticion(`${Global.url}api/articulos/${id}`, "GET", Global.token)

        setArticulo(datos);
        setCargando(cargando);
    }

    const handleEdit = (id) => {
        // navigate(`/articulo/editar/${id}`); // Navega al JSX de edición
        console.log("Editar articulo " + id)
    };

    const eliminar = async (id) => {
        let { status } = await Peticion(`${Global.url}api/articulos/${id}`, "DELETE", Global.token)

        if (status === 200) {
            navigate('/articulos');
        }
    }

    return (
        <>
            {cargando ? ("Cargando...") : articulo ? (
                <article className="articulo-detalle">
                    <div className="articulo-detalle-header">
                        <div className="mask">
                            <img src={articulo.imagen} alt={`Imagen ${articulo.titulo}`} />
                        </div>
                        <div className="data">
                            <h3 className="title">{articulo.titulo}</h3>
                            <p className="description">{articulo.descripcion}</p>
                        </div>
                    </div>

                    <p className="contenido">{articulo.contenido}</p>

                    <div className="acciones">
                        <button className="edit" onClick={() => handleEdit(articulo.id)}>Editar</button>
                        <button className="delete" onClick={() => eliminar(articulo.id)}>Borrar</button>
                    </div>
                </article>
            ) : (
                <p>No se encontró el articulo</p>
            )}
        </>
    );
}