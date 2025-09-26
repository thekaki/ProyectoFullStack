import React from "react";
import { useState } from "react";
import { useForm } from "../../../hooks/useForm";
import { Peticion } from "../../../helpers/Peticion";
import { Global } from "../../../helpers/Global";
import { useNavigate } from "react-router-dom";

export const CrearArticulo = () => {

    const { formulario, enviado, cambiado } = useForm({});
    const [resultado, setResultado] = useState(false)
    const [articuloId, setArticuloId] = useState(null);

    const navigate = useNavigate();

    const guardarArticulo = async (e) => {
        e.preventDefault();

        const { status, datos, cargando } = await Peticion(Global.url + "api/articulos", "POST", Global.token, formulario);

        if (status === 201) {
            setArticuloId(datos.id)
            setResultado("success");
        } else {
            console.error("Error:", datos);
            setResultado("error");
        }

    }

    return (
        <div className="jumbo">
            <h1>Crear Artículo</h1>
            <p>Formulario para crear un articulo</p>

            {resultado === "success" && (
                <div className="modal">
                    <div className="modal-content">
                        <p>Artículo guardado con éxito ✅</p>
                        <button onClick={() => navigate(`/articulo/${articuloId}`)}>Aceptar</button>
                    </div>
                </div>
            )}
            {resultado === "error" && (
                <strong>Error al guardar el artículo ❌</strong>
            )}

            <form className="formulario" onSubmit={guardarArticulo}>

                <div className="form-group">
                    <label htmlFor="titulo">Título</label>
                    <input type="text" name="titulo" placeholder="Añade un título" onChange={cambiado} />
                </div>

                <div className="form-group">
                    <label htmlFor="descripcion">Descripción</label>
                    <input type="text" name="descripcion" placeholder="Añade una descripción" onChange={cambiado} />
                </div>

                <div className="form-group">
                    <label htmlFor="contenido">Contenido</label>
                    <textarea type="text" name="contenido" placeholder="Añade el contenido" onChange={cambiado} />
                </div>

                <div className="form-group">
                    <label htmlFor="imagen">Imagen</label>
                    <input type="text" name="imagen" placeholder="Añade la url de una imagen" onChange={cambiado} />
                </div>

                <input type="submit" value="Crear" className="button" />

            </form>

        </div>
    )
}