import React from 'react'

export const Listado = ({ articulos, setArticulos }) => {
    return (
        articulos.map(articulo => {
            return (
                <article className="articulo-item">
                    <div className="mask">
                        <img src={articulo.imagen} alt={`Imagen ${articulo.titulo}`} />
                    </div>
                    <div className="data">
                        <h3 className="title">{articulo.titulo}</h3>
                        <p className="description">{articulo.descripcion}</p>

                        <button className="edit">Editar</button>
                        <button className="delete">Borrar</button>
                    </div>
                </article>
            );
        })
    )
}
