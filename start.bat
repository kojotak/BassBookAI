@echo off
REM Bass Book AI - Startup Script for Windows

echo Starting Bass Book AI Application...

REM Check if Java is available
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Java is not installed or not in PATH
    echo Please install Java 17 or higher to run this application
    pause
    exit /b 1
)

REM Check if Maven is available
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Maven is not installed or not in PATH
    echo Please install Maven 3.6 or higher to build this application
    pause
    exit /b 1
)

echo Building the application...
mvn clean spring-boot:run

pause