// usuarios.js

// Este archivo asume que auth-common.js ya está cargado.
document.addEventListener("DOMContentLoaded", async () => {
  // Comprobar existencia y vigencia del token
  if (typeof requireAuth === "function") {
    requireAuth();
  }

  try {
    await cargarUsuarios();
    // Inicializa DataTable solo después de tener la tabla poblada
    if (window.jQuery && $.fn.DataTable) {
      $("#usuariosTabla").DataTable();
    }
  } catch (err) {
    console.error("Error cargando usuarios:", err);
  }
});

// Función para cargar usuarios desde el backend
async function cargarUsuarios() {
  const response = await apiFetch("/api/usuarios", { method: "GET" });
  if (!response.ok) {
    throw new Error("Error al cargar usuarios");
  }
  const usuarios = await response.json();

  let listadoHtml = "";

  for (let usuario of usuarios) {
    let usuarioHTML = `
      <tr>
        <td>${usuario.id}</td>
        <td>${usuario.nombre ?? ""} ${usuario.apellido ?? ""}</td>
        <td>${usuario.email ?? ""}</td>
        <td>${usuario.telefono ?? ""}</td>
        <td>
          <a href="#" data-id="${
            usuario.id
          }" class="btn btn-danger btn-circle btn-lg eliminar-usuario">
            <i class="fas fa-trash"></i>
          </a>
        </td>
      </tr>`;
    listadoHtml += usuarioHTML;
  }

  document.querySelector("#usuariosTabla tbody").innerHTML = listadoHtml;

  // Delegación de eventos para botones de eliminar
  document
    .querySelector("#usuariosTabla tbody")
    .addEventListener("click", function (e) {
      const btn = e.target.closest(".eliminar-usuario");
      if (btn) {
        const id = btn.dataset.id;
        eliminarUsuario(id);
      }
    });
}

// Función para eliminar usuario
async function eliminarUsuario(id) {
  if (!confirm("¿Seguro que quieres eliminar este usuario?")) {
    return;
  }

  try {
    const response = await apiFetch(`/api/usuario/${id}`, {
      method: "DELETE",
    });

    if (response.ok) {
      alert("Usuario eliminado con éxito");
      await cargarUsuarios(); // refresca la tabla después de eliminar
    } else {
      const text = await response.text().catch(() => "");
      alert("Error al eliminar usuario: " + text);
    }
  } catch (err) {
    console.error(err);
    // apiFetch ya redirige al login en 401/403; cualquier otro error lo mostramos:
    alert("Error al eliminar usuario (revisa consola).");
  }
}
