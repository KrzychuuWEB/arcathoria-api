#!/usr/bin/env bash
set -euo pipefail
CERT_DIR="$(cd "$(dirname "$0")/../certs" && pwd)"
mkdir -p "$CERT_DIR"
mkcert -install
pushd "$CERT_DIR" >/dev/null
mkcert -key-file dev-key.pem -cert-file dev-cert.pem "api.arcathoria.dev" "game.arcathoria.dev" "localhost" 127.0.0.1 ::1
popd >/dev/null
echo "OK"
