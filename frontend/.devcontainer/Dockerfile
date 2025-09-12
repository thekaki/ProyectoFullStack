# Dockerfile para SB Admin 2 (Node 14 compatible + herramientas build)
FROM node:14-bullseye

# Evitar prompts
ENV DEBIAN_FRONTEND=noninteractive

# Instalar herramientas necesarias para node-gyp / node-sass
RUN apt-get update \
  && apt-get install -y --no-install-recommends \
       python3 \
       python3-dev \
       python3-distutils \
       python3-venv \
       python-is-python3 \
       build-essential \
       make \
       g++ \
       git \
       curl \
       ca-certificates \
  && rm -rf /var/lib/apt/lists/*

# Aseguramos que /usr/bin/python apunta a python3 (python-is-python3 lo hace)
ENV PYTHON=/usr/bin/python3

# Directorio de trabajo
WORKDIR /workspace

# Copiar package.json primero para aprovechar cache de capa Docker
COPY package*.json ./

# Instalar dependencias con npm ci (más reproducible)
# Usamos --legacy-peer-deps por si hay peerDeps antiguos en plantillas antiguas
RUN npm ci --legacy-peer-deps

# Copiar resto de la app
COPY . .

# Exponer el puerto de desarrollo (ajusta si tu app usa otro)
EXPOSE 3000

# Comando por defecto (puedes cambiar por el script real de tu package.json)
CMD ["npm", "start"]
