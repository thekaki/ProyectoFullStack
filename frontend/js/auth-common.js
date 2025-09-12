// auth-common.js
// CANDIDATO ÚNICO: incluir en todas las páginas PROTEGIDAS (index.html, usuarios.html, etc).
// NO incluir en login.html ni en register.html

const TOKEN_KEY = "token";

/* ------------------- helpers token ------------------- */
function saveTokenFromResponse(data) {
  const type = data.type || "Bearer";
  const raw = data.token || data.accessToken || data.access_token || "";
  if (!raw) return;
  localStorage.setItem(TOKEN_KEY, `${type} ${raw}`);
}

function getAuthHeaderValue() {
  return localStorage.getItem(TOKEN_KEY);
}

function getRawToken() {
  const s = getAuthHeaderValue();
  if (!s) return null;
  if (s.startsWith("Bearer ")) return s.substring(7);
  const parts = s.split(" ");
  return parts.length > 1 ? parts[1] : s;
}

function removeToken() {
  localStorage.removeItem(TOKEN_KEY);
}

/* ------------------- JWT parsing ------------------- */
function parseJwtPayload(token) {
  try {
    const base64Url = token.split(".")[1];
    if (!base64Url) return null;
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const padLength = (4 - (base64.length % 4)) % 4;
    const base64Padded = base64 + "=".repeat(padLength);
    const jsonPayload = decodeURIComponent(
      atob(base64Padded)
        .split("")
        .map(function (c) {
          return "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2);
        })
        .join("")
    );
    return JSON.parse(jsonPayload);
  } catch (e) {
    return null;
  }
}

function isTokenExpired(token) {
  if (!token) return true;
  const payload = parseJwtPayload(token);
  if (!payload) return true;
  if (payload.exp) {
    return Date.now() / 1000 >= payload.exp;
  }
  return true;
}

/* ------------------- validación y requerimiento (public) ------------------- */
/**
 * Comprueba la validez del token. Si no existe o está expirado:
 * - borra token y redirige a login.html
 * Si es válido, se asegura de que la página sea visible (quita la ocultación)
 */
function requireAuth() {
  const headerValue = getAuthHeaderValue();
  const raw = getRawToken();

  if (!headerValue || !raw || isTokenExpired(raw)) {
    removeToken();
    // Redirección definitiva
    window.location.replace("login.html");
    return false;
  }

  // Si hemos escondido la página, la mostramos
  if (document.documentElement.style.visibility === "hidden") {
    document.documentElement.style.visibility = "";
  }
  return true;
}

/* ------------------- wrapper fetch que añade Authorization ------------------- */
async function apiFetch(url, options = {}) {
  const headers = Object.assign(
    {
      Accept: "application/json",
      "Content-Type": "application/json",
    },
    options.headers || {}
  );

  const auth = getAuthHeaderValue();
  if (auth) {
    headers["Authorization"] = auth;
  } else {
    // Si no hay token, redirigimos inmediatamente (evita llamadas inútiles)
    removeToken();
    window.location.replace("login.html");
    throw new Error("No token");
  }

  const res = await fetch(url, { ...options, headers });

  if (res.status === 401 || res.status === 403) {
    // token inválido o expirado: limpiar y redirigir al login
    removeToken();
    window.location.replace("login.html");
    throw new Error("Unauthorized");
  }

  return res;
}

/* ------------------- ejecución inmediata para que la página no sea accesible sin token ------------------- */
/*
  Nota: para evitar *flicker* (que el HTML se muestre brevemente antes de validar),
  es recomendable añadir el pequeño snippet inline que aparece más abajo INSIDE <head>
  (antes de que el navegador renderice). Si no lo quieres, este check sigue funcionando:
  si la página está ya visible, el script redirigirá rápido, pero puede producir
  un parpadeo visual.
*/
(function immediateCheck() {
  try {
    // Si el script se carga, su comportamiento debe ser: validar y revelar o redirigir.
    // Ocultamos la página si aún no estaba oculta (para minimizar flash).
    if (document.documentElement.style.visibility !== "hidden") {
      // Solo aplicamos si el HTML no tenía reglas especiales; es seguro sobrescribir aquí.
      document.documentElement.style.visibility = "hidden";
    }

    // Ejecutar la validación (requireAuth también se encarga de mostrar la página si ok)
    requireAuth();
  } catch (e) {
    // En caso de error, limpiar y redirigir
    removeToken();
    window.location.replace("login.html");
  }
})();
