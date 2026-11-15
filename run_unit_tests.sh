#!/usr/bin/env bash
# ==============================================================================
# Script para ejecutar las pruebas unitarias de JUnit 5 con Maven y Docker
# ==============================================================================

set -e

echo "=================================================================="
echo " EJECUTANDO PRUEBAS UNITARIAS JUNIT 5 (PERSONAPP)                 "
echo "=================================================================="

if command -v mvn >/dev/null 2>&1; then
    echo "Ejecutando con Maven local..."
    mvn test
else
    echo "Ejecutando con Maven en contenedor Docker (JDK 17)..."
    docker run --rm -v "$(pwd)":/build -w /build maven:3.9.6-eclipse-temurin-17 mvn test
fi

echo ""
echo "=================================================================="
echo " TODAS LAS PRUEBAS UNITARIAS DE JUNIT 5 FINALIZARON CON ÉXITO     "
echo "=================================================================="
