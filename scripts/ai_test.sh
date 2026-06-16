#!/usr/bin/env sh
set -eu

# Test script for Smart Campus Portal (Unix/Linux/macOS)
# Backend: Maven test

echo "===== Running Backend Tests (Maven) ====="
mvn test

echo "===== Tests complete ====="
