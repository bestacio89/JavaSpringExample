# Docker Environment Setup

This script sets up the Docker environment with containers for MongoDB, Zookeeper, Kafka, PostgreSQL, MariaDB, SQL Server, and Java 17. Follow the instructions below to configure and run the environment on your machine.

## Prerequisites

Ensure you have Docker installed on your machine. You can download it from [Docker's official website](https://www.docker.com/products/docker-desktop).

## Setup Instructions

1. **Clone the Repository**

   ```bash
   git clone <your-repository-url>
   cd <your-repository-directory>
   ```

2. **Edit the Paths in the Script**

   Open `setup_docker_environment.sh` and adjust the paths to match your local machine. Replace the placeholders with the correct paths where you want to store the data for each service.

   ```bash
   # Example path modifications:
   # Replace "B:/MongoDBData" with your desired path for MongoDB data
   # Replace "B:/KafkaData" with your desired path for Kafka data
   # Replace "B:/PostgresData" with your desired path for PostgreSQL data
   # Replace "B:/MariaDBData" with your desired path for MariaDB data
   # Replace "B:/SQLServerData" with your desired path for SQL Server data
   # Replace "B:/JavaApp" with the path to your Java application
   ```

3. **Make the Script Executable**

   ```bash
   chmod +x setup_docker_environment.sh
   ```

4. **Run the Script**

   ```bash
   ./setup_docker_environment.sh
   ```

## Java Application Path

Ensure you update the Java application path in the script to point to the directory containing your Java application:

```bash
# Run Java 17 Container
docker run -d --name TestSpring -p 8080:8080 -v "<path-to-your-java-app>:/usr/src/app" openjdk:17-jdk-slim
```

Replace `<path-to-your-java-app>` with the absolute path to your Java application on your local machine.

## Verifying the Setup

After running the script, you can verify that all containers are running using:

```bash
docker ps
```

This command will list all running containers. You should see `MongoDB`, `zookeeper`, `Franz`, `PostgreSQL`, `MariDb`, `SQLServer`, and `TestSpring` in the list.

## Accessing Databases

You can connect to the databases using your preferred database management tools with the following details:

- **MongoDB**:
  - Host: `localhost`
  - Port: `27017`
  - Username: `root`
  - Password: `bl@dg3r$$`

- **PostgreSQL**:
  - Host: `localhost`
  - Port: `5432`
  - Username: `myuser`
  - Password: `bl@dg3r$$`
  - Database: `mydatabase`

- **MariaDB**:
  - Host: `localhost`
  - Port: `3306`
  - Username: `myuser`
  - Password: `bl@dg3r$$`
  - Database: `mydatabase`

- **SQL Server**:
  - Host: `localhost`
  - Port: `1433`
  - Username: `sa`
  - Password: `bl@dg3r$$`

## Common Issues

- Ensure that Docker is running before executing the script.
- Verify that the specified directories for volume mounts exist on your machine.
- If you encounter any network-related errors, ensure your firewall or antivirus is not blocking Docker.

