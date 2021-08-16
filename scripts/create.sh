#!/usr/bin/env bash
# Stop immediately if something goes wrong
set -o errexit
set -o nounset
set -o pipefail

ROOT="$( cd "$( dirname "${BASH_SOURCE[0]}" )/.." && pwd )"

echo "Building a new users-service jar"
"$ROOT"/gradlew clean bootJar

if [ -d "$ROOT"/build ]; then
    echo "Building new docker image"
else
    echo "Failure when building new jar"
fi

docker build -t users-service:latest .

docker-compose up --build