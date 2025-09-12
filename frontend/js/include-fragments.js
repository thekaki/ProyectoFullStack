// include-fragments.js (con normalización de rutas y mensajes de error más claros)
(function () {
  // debounce helper
  let debounceTimer = null;
  function debounce(fn, wait = 50) {
    return function () {
      if (debounceTimer) clearTimeout(debounceTimer);
      const args = arguments;
      debounceTimer = setTimeout(() => fn.apply(this, args), wait);
    };
  }

  // Normaliza la ruta recibida para que sea root-relative.
  // - Si ya es absoluta (http(s):// o comienza con /) la deja.
  // - Si es relativa, la convierte en '/'+path (root-relative).
  function normalizePath(path) {
    if (!path) return path;
    path = path.trim();
    if (/^https?:\/\//i.test(path)) return path; // absolute external url
    if (path.startsWith("/")) return path; // already root-relative
    // remove leading './' or '.\' if present
    path = path.replace(/^\.\/+/, "");
    // ensure leading slash to make root-relative
    return "/" + path;
  }

  async function fetchText(url) {
    const normalized = normalizePath(url);
    const res = await fetch(normalized, { cache: "no-store" });
    if (!res.ok) {
      // throw error with useful info for debugging
      throw new Error(
        `HTTP ${res.status} ${res.statusText} when fetching ${normalized}`
      );
    }
    return await res.text();
  }

  function insertHtmlAndExecuteScripts(container, html) {
    const tpl = document.createElement("template");
    tpl.innerHTML = html.trim();

    while (tpl.content.firstChild) {
      container.appendChild(tpl.content.firstChild);
    }

    const scripts = container.querySelectorAll("script");
    for (const oldScript of Array.from(scripts)) {
      const newScript = document.createElement("script");
      for (const attr of oldScript.attributes) {
        newScript.setAttribute(attr.name, attr.value);
      }
      if (oldScript.src) {
        // normalize src too (in case fragment references relative resources)
        newScript.src = normalizePath(oldScript.getAttribute("src"));
        document.body.appendChild(newScript);
      } else {
        newScript.text = oldScript.textContent;
        document.body.appendChild(newScript);
      }
      oldScript.parentNode.removeChild(oldScript);
    }
  }

  async function loadFragments(root = document) {
    const nodes = Array.from(
      root.querySelectorAll("[data-fragment]:not([data-fragment-loaded])")
    );
    if (!nodes.length) return;

    if (typeof requireAuth === "function") {
      const ok = requireAuth();
      if (!ok) return; // requireAuth may have redirected
    }

    for (const node of nodes) {
      const rawPath = node.getAttribute("data-fragment");
      if (!rawPath) continue;
      try {
        const html = await fetchText(rawPath);
        node.innerHTML = "";
        insertHtmlAndExecuteScripts(node, html);
        node.setAttribute("data-fragment-loaded", "true");
      } catch (err) {
        console.error(`Error cargando fragment ${rawPath}:`, err);
        node.innerHTML = `<div class="alert alert-danger p-2">Error cargando fragment: ${rawPath} — ${err.message}</div>`;
        node.setAttribute("data-fragment-loaded", "error");
      }
    }
  }

  window.loadFragments = loadFragments;

  function onReady() {
    loadFragments().finally(() => {
      if (document.documentElement.style.visibility !== "") {
        document.documentElement.style.visibility = "";
      }
    });
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", onReady);
  } else {
    onReady();
  }

  // Observer para nodos añadidos dinámicamente
  const observer = new MutationObserver(
    debounce((mutations) => {
      let shouldRun = false;
      for (const m of mutations) {
        if (m.addedNodes && m.addedNodes.length) {
          for (const n of Array.from(m.addedNodes)) {
            if (n.nodeType === 1) {
              if (n.hasAttribute && n.hasAttribute("data-fragment")) {
                shouldRun = true;
                break;
              }
              if (n.querySelector && n.querySelector("[data-fragment]")) {
                shouldRun = true;
                break;
              }
            }
          }
        }
        if (shouldRun) break;
      }
      if (shouldRun) loadFragments();
    }),
    60
  );

  try {
    observer.observe(document.body, { childList: true, subtree: true });
  } catch (e) {
    console.warn(
      "include-fragments: MutationObserver no disponible o body aún no presente."
    );
  }
})();
