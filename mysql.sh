#!/bin/bash

# Navigate to the directory containing the Dockerfile
cd ../DockerLegendHG/

# Build the Docker image
docker compose up -d

url='brmysql.xmxcloud.net'
port='3306'
username='u2718_kZ9Gcu5iIG'
table='s2718_legendhg'
password='i+GSnfGr!0!EeT0dKFbBULlI'

docker compose exec mysql mysql -h "$url" -P "$port" -u "$username" -p"$password" "$table"