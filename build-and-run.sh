#!/bin/bash

set -e

echo "Checking Java availability..."
if ! command -v java &> /dev/null; then
  export JAVA_HOME="/Volumes/backup/codebase/java"
  export PATH="$JAVA_HOME/bin:$PATH"
  if ! command -v java &> /dev/null; then
    echo "Java is not installed. Please install Java 11+ or set JAVA_HOME to /Volumes/backup/codebase/java."
    exit 1
  fi
  echo "Java not found in PATH, using JAVA_HOME=$JAVA_HOME"
fi
echo "Java version:"
java -version

echo "Checking Maven availability..."
if ! command -v mvn &> /dev/null; then
  export MAVEN_HOME="/Volumes/backup/codebase/apache-maven-3.9.9"
  export PATH="$MAVEN_HOME/bin:$PATH"
  if ! command -v mvn &> /dev/null; then
    echo "Maven is not installed. Please install Maven or set MAVEN_HOME to /Volumes/backup/codebase/apache-maven-3.9.9."
    exit 1
  fi
  echo "Maven not found in PATH, using MAVEN_HOME=$MAVEN_HOME"
fi
echo "Maven version:"
mvn -version

echo "Checking Node.js availability..."
if ! command -v node &> /dev/null; then
  export NODE_HOME="/Volumes/backup/codebase/node"
  export PATH="$NODE_HOME/bin:$PATH"
  if ! command -v node &> /dev/null; then
    echo "Node.js is not installed. Please install Node.js or set NODE_HOME to /Volumes/backup/codebase/node."
    exit 1
  fi
  echo "Node.js not found in PATH, using NODE_HOME=$NODE_HOME"
fi
echo "Node.js version:"
node -v

echo "Checking npm availability..."
if ! command -v npm &> /dev/null; then
  export NODE_HOME="/Volumes/backup/codebase/node"
  export PATH="$NODE_HOME/bin:$PATH"
  if ! command -v npm &> /dev/null; then
    echo "npm is not installed. Please install npm."
    exit 1
  fi
  echo "npm not found in PATH, using NODE_HOME=$NODE_HOME"
fi
echo "npm version:"
npm -v

echo "Building backend with Maven..."
mvn clean package -DskipTests

echo "Starting backend..."
java -jar target/*.jar &
BACKEND_PID=$!

echo "Waiting for backend to start..."
sleep 10

echo "Installing frontend dependencies with npm..."
npm install

echo "Starting frontend (React UI)..."
npm start

echo "Shutting down backend..."
kill $BACKEND_PID

# Note:
# This script is fully runnable by itself. Simply execute:
#   chmod +x build-and-run.sh
#   ./build-and-run.sh
# It will check for required tools, build and start the backend, install frontend dependencies, and start the React UI.
# No manual steps are needed beyond running this script.
