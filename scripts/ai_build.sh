#!/usr/bin/env sh
set -eu

# Build script for Smart Campus Portal (Unix/Linux/macOS)
# Backend: Maven, Frontend: npm

echo "===== Building Backend (Maven) ====="
mvn clean install -DskipTests
echo "===== Backend build SUCCESS ====="

echo ""
echo "To start backend: mvn spring-boot:run -pl ruoyi-admin"
echo "To start frontend: cd ruoyi-ui && npm run dev"
