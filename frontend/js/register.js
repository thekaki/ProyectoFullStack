async function registrarUsuario() {
  const nombre = document.getElementById("txtNombre").value.trim();
  const apellido = document.getElementById("txtApellido").value.trim();
  const email = document.getElementById("txtEmail").value.trim();
  const password = document.getElementById("txtPassword").value.trim();
  const repeatPassword = document
    .getElementById("txtRepeatPassword")
    .value.trim();

  // Validaciones básicas
  if (!nombre || !apellido || !email || !password || !repeatPassword) {
    alert("Todos los campos son obligatorios ❌");
    document.getElementById("txtPassword").value = "";
    document.getElementById("txtRepeatPassword").value = "";
    return;
  }

  if (password !== repeatPassword) {
    alert("Las contraseñas no coinciden ❌");
    document.getElementById("txtPassword").value = "";
    document.getElementById("txtRepeatPassword").value = "";
    return;
  }

  const usuario = { nombre, apellido, email, password };

  try {
    const response = await fetch("/api/usuario", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
      },
      body: JSON.stringify(usuario),
    });

    if (!response.ok) {
      // Manejar error del servidor
      const errorData = await response.json().catch(() => ({}));
      alert(
        "Error al registrar usuario ❌: " +
          (errorData.message || JSON.stringify(errorData))
      );
      document.getElementById("txtPassword").value = "";
      document.getElementById("txtRepeatPassword").value = "";
      return;
    }

    const data = await response.json().catch(() => ({}));
    console.log("Usuario creado:", data);

    alert("Usuario registrado con éxito ✅");
    document.querySelector("form.user").reset();

    // Redirigir al login
    window.location.href = "login.html";
  } catch (err) {
    alert("Error de conexión con el servidor ❌");
    console.error(err);
  }
}
