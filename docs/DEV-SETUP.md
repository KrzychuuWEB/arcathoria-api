# DEV Environment Setup (Backend)

This document describes the **initial setup of the backend development environment**
with local HTTPS using `mkcert` and the Caddy reverse proxy.

---

## 1) Add local domain to `hosts`

Added this lines to your `hosts` file:

```
127.0.0.1 api.arcathoria.dev
127.0.0.1 game.arcathoria.dev
```

**Windows** — `C:\Windows\System32\drivers\etc\hosts` (run as Administrator)

**macOS/Linux** — `/etc/hosts`

---

## 2) Install `mkcert` on the host

### Windows (PowerShell jako Administrator)

**Windows**

```
choco install mkcert -y
mkcert -install
```

**MacOs**

```
brew install mkcert nss
mkcert -install
```

**Linux**

```
sudo apt-get install -y mkcert libnss3-tools
mkcert -install
```

## 3) Generate certificates (run on root project tree)

**Windows**

````
scripts\run-mkcert-setup.bat
````

**MacOs / Linux**

```
./scripts/mkcert-setup.sh
```

## 4) Run backend locally in IntelliJ

Open the module with **@SpringBootApplication** *(/bootstrap/src/main/java/com/arcathoria/Application)* and click *Run*.

## 5) Access API via Caddy reverse proxy

Open browser and navigate to:
``
https://api.arcathoria.dev
`` for API

If client is running locally, navigate to:
``
https://game.arcathoria.dev
`` for client