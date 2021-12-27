#!/usr/bin/env bash
# Stop immediately if something goes wrong
set -o errexit
set -o nounset
set -o pipefail

ROOT="$( cd "$( dirname "${BASH_SOURCE[0]}" )/.." && pwd )"

echo "Running acceptance tests"
"$ROOT"/gradlew test