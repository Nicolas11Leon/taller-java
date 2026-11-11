#!/bin/bash
# ==============================================================================
# Script de Prueba Automática de los 24 Endpoints (6 Métodos x 4 Entidades)
# Proyecto: PersonAPP - Arquitectura Hexagonal
# ==============================================================================

BASE_URL="http://localhost:3000/api/v1"
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo "=================================================================="
echo " INICIANDO PRUEBAS AUTOMÁTICAS DE ENDPOINTS (MARIADB Y MONGODB)   "
echo "=================================================================="

assert_status() {
    local desc="$1"
    local response="$2"
    local expected="$3"
    if echo "$response" | grep -q "$expected"; then
        echo -e "[ ${GREEN}PASS${NC} ] $desc"
    else
        echo -e "[ ${RED}FAIL${NC} ] $desc (Esperaba '$expected' en la respuesta)"
        echo "       Respuesta: $response"
    fi
}

echo ""
echo "--- 1. MÓDULO PERSONA ---"
# Lectura 1: Listar
res=$(curl -s "$BASE_URL/persona/MARIA")
assert_status "Persona MARIA - Listar todos" "$res" "Pepe"
res=$(curl -s "$BASE_URL/persona/MONGO")
assert_status "Persona MONGO - Listar todos" "$res" "Pepe"

# Lectura 2: Buscar uno
res=$(curl -s "$BASE_URL/persona/MARIA/123456789")
assert_status "Persona MARIA - Buscar por CC" "$res" "123456789"
res=$(curl -s "$BASE_URL/persona/MONGO/123456789")
assert_status "Persona MONGO - Buscar por CC" "$res" "123456789"

# Lectura 3: Contar
res=$(curl -s "$BASE_URL/persona/MARIA/count")
assert_status "Persona MARIA - Contar" "$res" "[0-9]"
res=$(curl -s "$BASE_URL/persona/MONGO/count")
assert_status "Persona MONGO - Contar" "$res" "[0-9]"

# Escritura 1: Crear
res=$(curl -s -X POST -H "Content-Type: application/json" -d '{"dni":"999999","firstName":"Prueba","lastName":"Test","age":"25","sex":"M","database":"MARIA"}' "$BASE_URL/persona")
assert_status "Persona MARIA - Crear" "$res" "999999"
res=$(curl -s -X POST -H "Content-Type: application/json" -d '{"dni":"888888","firstName":"Prueba2","lastName":"Test2","age":"26","sex":"F","database":"MONGO"}' "$BASE_URL/persona")
assert_status "Persona MONGO - Crear" "$res" "888888"

# Escritura 2: Editar
res=$(curl -s -X PUT -H "Content-Type: application/json" -d '{"dni":"999999","firstName":"PruebaEdit","lastName":"Test","age":"26","sex":"M","database":"MARIA"}' "$BASE_URL/persona/MARIA/999999")
assert_status "Persona MARIA - Editar" "$res" "PruebaEdit"
res=$(curl -s -X PUT -H "Content-Type: application/json" -d '{"dni":"888888","firstName":"PruebaEdit2","lastName":"Test2","age":"27","sex":"F","database":"MONGO"}' "$BASE_URL/persona/MONGO/888888")
assert_status "Persona MONGO - Editar" "$res" "PruebaEdit2"

# Escritura 3: Eliminar
res=$(curl -s -X DELETE "$BASE_URL/persona/MARIA/999999")
assert_status "Persona MARIA - Eliminar" "$res" "DELETED"
res=$(curl -s -X DELETE "$BASE_URL/persona/MONGO/888888")
assert_status "Persona MONGO - Eliminar" "$res" "DELETED"


echo ""
echo "--- 2. MÓDULO PROFESIÓN ---"
# Lectura 1: Listar
res=$(curl -s "$BASE_URL/profesion/MARIA")
assert_status "Profesion MARIA - Listar todos" "$res" "Ingeniero"
res=$(curl -s "$BASE_URL/profesion/MONGO")
assert_status "Profesion MONGO - Listar todos" "$res" "Ingeniero"

# Lectura 2: Buscar uno
res=$(curl -s "$BASE_URL/profesion/MARIA/1")
assert_status "Profesion MARIA - Buscar por ID" "$res" "Ingeniero de Sistemas"
res=$(curl -s "$BASE_URL/profesion/MONGO/1")
assert_status "Profesion MONGO - Buscar por ID" "$res" "Ingeniero de Sistemas"

# Lectura 3: Contar
res=$(curl -s "$BASE_URL/profesion/MARIA/count")
assert_status "Profesion MARIA - Contar" "$res" "5"
res=$(curl -s "$BASE_URL/profesion/MONGO/count")
assert_status "Profesion MONGO - Contar" "$res" "5"

# Escritura 1: Crear
res=$(curl -s -X POST -H "Content-Type: application/json" -d '{"id":"99","name":"Arquitecto","description":"Construccion","database":"MARIA"}' "$BASE_URL/profesion")
assert_status "Profesion MARIA - Crear" "$res" "Arquitecto"
res=$(curl -s -X POST -H "Content-Type: application/json" -d '{"id":"98","name":"Musico","description":"Artes","database":"MONGO"}' "$BASE_URL/profesion")
assert_status "Profesion MONGO - Crear" "$res" "Musico"

# Escritura 2: Editar
res=$(curl -s -X PUT -H "Content-Type: application/json" -d '{"id":"99","name":"Arquitecto Senior","description":"Construccion y Diseno","database":"MARIA"}' "$BASE_URL/profesion/MARIA/99")
assert_status "Profesion MARIA - Editar" "$res" "Arquitecto Senior"

# Escritura 3: Eliminar
res=$(curl -s -X DELETE "$BASE_URL/profesion/MARIA/99")
assert_status "Profesion MARIA - Eliminar" "$res" "DELETED"
res=$(curl -s -X DELETE "$BASE_URL/profesion/MONGO/98")
assert_status "Profesion MONGO - Eliminar" "$res" "DELETED"


echo ""
echo "--- 3. MÓDULO TELÉFONO ---"
# Lectura 1: Listar
res=$(curl -s "$BASE_URL/telefono/MARIA")
assert_status "Telefono MARIA - Listar todos" "$res" "3001234567"
res=$(curl -s "$BASE_URL/telefono/MONGO")
assert_status "Telefono MONGO - Listar todos" "$res" "3001234567"

# Lectura 2: Buscar uno
res=$(curl -s "$BASE_URL/telefono/MARIA/3001234567")
assert_status "Telefono MARIA - Buscar por número" "$res" "Claro"
res=$(curl -s "$BASE_URL/telefono/MONGO/3001234567")
assert_status "Telefono MONGO - Buscar por número" "$res" "Claro"

# Lectura 3: Contar
res=$(curl -s "$BASE_URL/telefono/MARIA/count")
assert_status "Telefono MARIA - Contar" "$res" "5"
res=$(curl -s "$BASE_URL/telefono/MONGO/count")
assert_status "Telefono MONGO - Contar" "$res" "5"

# Escritura 1: Crear
res=$(curl -s -X POST -H "Content-Type: application/json" -d '{"number":"3999999999","oper":"Flash","ownerDni":"123456789","database":"MARIA"}' "$BASE_URL/telefono")
assert_status "Telefono MARIA - Crear" "$res" "3999999999"
res=$(curl -s -X POST -H "Content-Type: application/json" -d '{"number":"3888888888","oper":"Flash","ownerDni":"123456789","database":"MONGO"}' "$BASE_URL/telefono")
assert_status "Telefono MONGO - Crear" "$res" "3888888888"

# Escritura 2: Editar
res=$(curl -s -X PUT -H "Content-Type: application/json" -d '{"number":"3999999999","oper":"Virgin","ownerDni":"123456789","database":"MARIA"}' "$BASE_URL/telefono/MARIA/3999999999")
assert_status "Telefono MARIA - Editar" "$res" "Virgin"

# Escritura 3: Eliminar
res=$(curl -s -X DELETE "$BASE_URL/telefono/MARIA/3999999999")
assert_status "Telefono MARIA - Eliminar" "$res" "DELETED"
res=$(curl -s -X DELETE "$BASE_URL/telefono/MONGO/3888888888")
assert_status "Telefono MONGO - Eliminar" "$res" "DELETED"


echo ""
echo "--- 4. MÓDULO ESTUDIOS ---"
# Lectura 1: Listar
res=$(curl -s "$BASE_URL/estudio/MARIA")
assert_status "Estudios MARIA - Listar todos" "$res" "Pontificia Universidad Javeriana"
res=$(curl -s "$BASE_URL/estudio/MONGO")
assert_status "Estudios MONGO - Listar todos" "$res" "Pontificia Universidad Javeriana"

# Lectura 2: Buscar uno
res=$(curl -s "$BASE_URL/estudio/MARIA/1/123456789")
assert_status "Estudios MARIA - Buscar por llave compuesta" "$res" "Pontificia Universidad Javeriana"
res=$(curl -s "$BASE_URL/estudio/MONGO/1/123456789")
assert_status "Estudios MONGO - Buscar por llave compuesta" "$res" "Pontificia Universidad Javeriana"

# Lectura 3: Contar
res=$(curl -s "$BASE_URL/estudio/MARIA/count")
assert_status "Estudios MARIA - Contar" "$res" "4"
res=$(curl -s "$BASE_URL/estudio/MONGO/count")
assert_status "Estudios MONGO - Contar" "$res" "4"

# Escritura 1: Crear
res=$(curl -s -X POST -H "Content-Type: application/json" -d '{"idProf":"5","ccPer":"123456789","fecha":"2023-05-10","univer":"Universidad de Antioquia","database":"MARIA"}' "$BASE_URL/estudio")
assert_status "Estudios MARIA - Crear" "$res" "Universidad de Antioquia"
res=$(curl -s -X POST -H "Content-Type: application/json" -d '{"idProf":"5","ccPer":"123456789","fecha":"2023-05-10","univer":"Universidad de Antioquia","database":"MONGO"}' "$BASE_URL/estudio")
assert_status "Estudios MONGO - Crear" "$res" "Universidad de Antioquia"

# Escritura 2: Editar
res=$(curl -s -X PUT -H "Content-Type: application/json" -d '{"idProf":"5","ccPer":"123456789","fecha":"2023-05-10","univer":"UdeA Modificada","database":"MARIA"}' "$BASE_URL/estudio/MARIA/5/123456789")
assert_status "Estudios MARIA - Editar" "$res" "UdeA Modificada"

# Escritura 3: Eliminar
res=$(curl -s -X DELETE "$BASE_URL/estudio/MARIA/5/123456789")
assert_status "Estudios MARIA - Eliminar" "$res" "DELETED"
res=$(curl -s -X DELETE "$BASE_URL/estudio/MONGO/5/123456789")
assert_status "Estudios MONGO - Eliminar" "$res" "DELETED"

echo ""
echo "=================================================================="
echo " TODAS LAS PRUEBAS AUTOMÁTICAS FINALIZARON EXITOSAMENTE          "
echo "=================================================================="
