import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
    server: {
    host: "0.0.0.0", // <-- importante
    port: 5173,
    watch: {
      // fuerza polling: detecta cambios en mounts no notificados por inotify
      usePolling: true,
      interval: 100, // ms, ajusta si lo quieres menos o más sensible
    },
    hmr: {
      // forzar el host/puerto que el cliente HMR debe usar para conectar
      host: "localhost", // o la IP de tu host si necesario
      clientPort: 5173, // puerto accesible desde el navegador (host)
    },
  },
})
