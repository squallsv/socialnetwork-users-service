#! /usr/bin/env bash

# "---------------------------------------------------------"
# "-                                                       -"
# "-  Common commands for all scripts                      -"
# "-                                                       -"
# "---------------------------------------------------------"

# gcloud, kubectl, java, and bazel are required for this demo
command -v gcloud >/dev/null 2>&1 || { \
 echo >&2 "I require gcloud but it's not installed.  Aborting."; exit 1; }

command -v kubectl >/dev/null 2>&1 || { \
 echo >&2 "I require kubectl but it's not installed.  Aborting."; exit 1; }

command java -version >/dev/null 2>&1 || { \
 echo >&2 "I require java but it's not installed.  Aborting."; exit 1; }