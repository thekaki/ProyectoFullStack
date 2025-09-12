async function login() {
  const email = document.getElementById("txtEmail").value.trim();
  const password = document.getElementById("txtPassword").value.trim();

  if (!email || !password) {
    alert("Todos los campos son obligatorios ❌");
    return;
  }

  const usuario = { email, password };

  try {
    const response = await fetch("/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
      },
      body: JSON.stringify(usuario),
    });

    if (!response.ok) {
      alert("Usuario o contraseña incorrectos ❌");
      document.getElementById("txtEmail").value = "";
      document.getElementById("txtPassword").value = "";
      return;
    }

    const data = await response.json();

    // Usar helper para guardar token en localStorage
    if (typeof saveTokenFromResponse === "function") {
      saveTokenFromResponse(data);
    } else {
      // fallback: intentar campos comunes
      const type = data.type || "Bearer";
      const raw = data.token || data.accessToken || data.access_token || "";
      if (raw) localStorage.setItem("token", `${type} ${raw}`);
    }

    window.location.href = "index.html";
  } catch (err) {
    alert("Error de conexión con el servidor ❌");
    console.error(err);
  }
}
