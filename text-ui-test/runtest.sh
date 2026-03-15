#!/usr/bin/env bash

# change to script directory
cd "${0%/*}" || exit

# go to project root
cd ..

# build jar
./gradlew clean shadowJar

# go to test folder
cd text-ui-test || exit

rm -f ACTUAL.TXT EXPECTED-UNIX.TXT

# run program
java -jar ../build/libs/duke.jar < input.txt > ACTUAL.TXT 2>&1

# normalize line endings
cp EXPECTED.TXT EXPECTED-UNIX.TXT
dos2unix EXPECTED-UNIX.TXT ACTUAL.TXT

# compare
diff EXPECTED-UNIX.TXT ACTUAL.TXT

if [ $? -eq 0 ]
then
    echo "Test passed!"
    exit 0
else
    echo "Test failed!"
    exit 1
fi