#!/bin/bash
set -e
set -x

JAR_PATH="../build/libs/duke.jar"

if [ ! -f "$JAR_PATH" ]; then
    echo "Error: $JAR_PATH not found!"
    exit 1
fi

java -jar "$JAR_PATH" < input.txt > ACTUAL.TXT
diff ACTUAL.TXT EXPECTED.TXT