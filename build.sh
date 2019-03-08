#!/bin/bash

source env.sh

echo "Create artifacts directory"
mkdir artifacts

set -e

# Build the scala package
echo "Build hello-function Scala package"
sbt assembly
cp target/scala-2.12/hat-she-function-template.jar artifacts/hat-she-function-template.jar

# Deploy
serverless deploy
