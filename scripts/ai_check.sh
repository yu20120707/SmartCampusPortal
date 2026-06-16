#!/usr/bin/env sh
set -eu

# Check (build + test) for Smart Campus Portal (Unix/Linux/macOS)
# Backend: Maven build + test, Frontend: npm build

echo "===== Step 1: Build Backend ====="
mvn clean install -DskipTests

echo "===== Step 2: Run Backend Tests ====="
mvn test

echo "===== Step 3: Build Frontend ====="
cd ruoyi-ui
npm install
npm run build:prod
cd ..

echo "===== All checks passed ====="
