#!/usr/bin/env bash
if [ -d "/usr/lib/jvm/java-17-openjdk-amd64" ]; then
    export JAVA_HOME="/usr/lib/jvm/java-17-openjdk-amd64"
fi
MAVEN_BIN="/tmp/apache-maven-3.9.6/bin/mvn"
if [ ! -f "$MAVEN_BIN" ]; then
    if command -v mvn >/dev/null 2>&1; then
        MAVEN_BIN="mvn"
    else
        echo "Maven no encontrado en $MAVEN_BIN ni en PATH." >&2
        exit 1
    fi
fi
exec "$MAVEN_BIN" "$@"
