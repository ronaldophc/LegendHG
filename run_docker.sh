#!/bin/bash

# Navigate to the directory containing the Dockerfile
cd ../DockerLegendHG/

# Build the Docker image
docker compose up -d

# Run the Docker container
docker compose exec minecraft ./start.sh