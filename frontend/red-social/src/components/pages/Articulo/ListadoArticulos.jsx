import React from 'react'
import { Link } from "react-router-dom";

export const ListadoArticulos = ({ articulos, handleEdit, eliminar, setArticulos, user }) => {

    return (
        articulos.map(articulo => {
            return (
                <article key={articulo.id} className="articulo-item">
                    <div className="mask">
                        {articulo.imagen && <img src={articulo.imagen} alt={`Imagen ${articulo.titulo}`} />}
                        {!articulo.imagen && <img src="https://metabob.com/images/code-article.jpeg" alt="Imagen generica" />}
                    </div>
                    <div className="data">
                        <h3 className="title">{articulo.titulo}</h3>
                        <p className="description">{articulo.descripcion}</p>

                        <Link to={`/articulos/${articulo.id}`} className="seguir-leyendo">
                            Seguir leyendo →
                        </Link>

                        {user?.rol === "ADMIN" && (
                            <>
                                <div className="acciones">
                                    <button onClick={() => handleEdit(articulo.id)}>Editar</button>
                                    <button onClick={() => eliminar(articulo.id)}>Borrar</button>
                                </div>
                            </>
                        )}

                        {user?.id === articulo.autorId && (
                            <button onClick={() => handleEdit(articulo.id)}>Editar</button>
                        )}

                    </div>
                </article>
            );
        })
    )
}
