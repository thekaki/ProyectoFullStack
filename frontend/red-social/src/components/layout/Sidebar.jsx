import React, { useState } from "react"
import { useNavigate } from "react-router-dom"

export const Sidebar = () => {
  const [query, setQuery] = useState("")
  const navigate = useNavigate()

  const handleSubmit = (e) => {
    e.preventDefault()
    if (query.trim() !== "") {
      navigate(`/articulos?titulo=${encodeURIComponent(query)}`)
    }
  }

  return (
    <aside className="lateral">
      <div className="search">
        <h3 className="title">Buscador</h3>
        <form onSubmit={handleSubmit}>
          <input
            type="text"
            id="search_field"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
          />
          <button id="search" type="submit">Buscar</button>
        </form>
      </div>
    </aside>
  )
}
