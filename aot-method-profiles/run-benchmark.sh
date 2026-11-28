#!/usr/bin/env bash
# Requires JDK 25 or newer on the PATH.
set -euo pipefail

mvn -q package -DskipTests

JAR=target/aot-method-profiles.jar
MAIN=com.geekyhacker.aotprofiles.Main

# training run: creates the AOT cache including method profiles
java -XX:AOTCacheOutput=app.aot -cp "$JAR" "$MAIN" 20 200 > /dev/null

java -cp "$JAR" "$MAIN" 100 200 > no-cache.csv
java -XX:AOTCache=app.aot -cp "$JAR" "$MAIN" 100 200 > with-profiles.csv
java -XX:+UnlockDiagnosticVMOptions -XX:-AOTReplayTraining \
     -XX:AOTCache=app.aot -cp "$JAR" "$MAIN" 100 200 > without-profiles.csv

java -cp "$JAR" com.geekyhacker.aotprofiles.Compare no-cache.csv with-profiles.csv without-profiles.csv
