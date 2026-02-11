#!/bin/bash

# Bass Book AI - Startup Script

echo "Starting Bass Book AI Application..."

# Check if Java is available
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH"
    echo "Please install Java 17 or higher to run this application"
    exit 1
fi

# Check if Maven is available
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven is not installed or not in PATH"
    echo "Please install Maven 3.6 or higher to build this application"
    exit 1
fi

echo "Building the application..."
mvn clean spring-boot:run