@echo off
 setlocal enabledelayedexpansion

 REM change to script directory
 cd /d %~dp0

 REM go to project root
 cd ..

 REM build jar
 call gradlew clean shadowJar

 REM go to test folder
 cd text-ui-test || exit /b 1

 REM clean old files
 if exist ACTUAL.TXT del ACTUAL.TXT
 if exist EXPECTED-UNIX.TXT del EXPECTED-UNIX.TXT

 REM find jar file
 cd ..\build\libs
 for %%f in (*.jar) do (
     set "JAR=%%f"
 )

 REM go back to test folder
 cd ..\..\text-ui-test

 REM run program
 java -jar ..\build\libs\%JAR% < input.txt > ACTUAL.TXT 2>&1

 REM normalize line endings (basic Windows-safe way)
 type EXPECTED.TXT > EXPECTED-UNIX.TXT

 REM compare files
 FC EXPECTED-UNIX.TXT ACTUAL.TXT >nul

 if %errorlevel%==0 (
     echo Test passed!
     exit /b 0
 ) else (
     echo Test failed!
     exit /b 1
 )