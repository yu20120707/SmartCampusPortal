#!/usr/bin/env sh
set -eu

# Check (build + test) for Smart Campus Portal (Unix/Linux/macOS)
# Backend: Maven build + test

echo "===== Step 1: Build Backend ====="
mvn clean install -DskipTests

echo "===== Step 2: Run Backend Tests ====="
mvn test

echo "===== All checks passed ====="
