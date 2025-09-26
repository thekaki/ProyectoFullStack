import React from "react";
import { Routes, Route, BrowserRouter, Navigate } from "react-router-dom";
import { Inicio } from "../components/pages/Inicio";
import { Articulos } from "../components/pages/Articulo/Articulos";
import { Header } from "../components/layout/Header";
import { Nav } from "../components/layout/Nav";
import { Sidebar } from "../components/layout/Sidebar";
import { Footer } from "../components/layout/Footer";
import { CrearArticulo } from "../components/pages/Articulo/CrearArticulo";
import { Articulo } from "../components/pages/Articulo/Articulo";

export const Rutas = () => {
    return (
        <BrowserRouter>
            {/* LAYOUT */}
            <Header />
            <Nav />
            {/* Contenido central y rutas */}
            <section id="content" className="content">
                <Routes>
                    <Route path="/" element={<Inicio />} />
                    <Route path="/inicio" element={<Inicio />} />
                    <Route path="/articulos" element={<Articulos />} />
                    <Route path="/crear-articulos" element={<CrearArticulo />} />
                    <Route path="/articulos/:id" element={<Articulo />}></Route>
                    <Route path="/contacto" element={<Inicio />} />
                </Routes>
            </section>

            <Sidebar />

            <Footer />

        </BrowserRouter>
    )
}