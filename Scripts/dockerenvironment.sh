
# Run MongoDB
docker run -d --name MongoDB -e MONGO_INITDB_ROOT_USERNAME=root -e MONGO_INITDB_ROOT_PASSWORD=bl@dg3r$$ -p 27017:27017 -v "B:/MongoDBData:/data/db" mongo:4.4  # Stable version

# Run Zookeeper
docker run -d --name zookeeper -p 2181:2181 -e ZOOKEEPER_CLIENT_PORT=2181 confluentinc/cp-zookeeper:8.2.3  # Stable version

# Run Kafka
docker run -d --name Franz -p 9092:9092 -e KAFKA_BROKER_ID=1 -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 -v "B:/KafkaData:/var/lib/kafka/data" confluentinc/cp-kafka:8.2.3  # Stable version

# Run PostgreSQL
docker run -d --name PostgreSQL -e POSTGRES_PASSWORD=bl@dg3r$$ -e POSTGRES_DB=mydatabase -e POSTGRES_USER=myuser -p 5432:5432 -v "B:/PostgresData:/var/lib/postgresql/data" postgres:14.5  # Stable version

# Run MariaDB
docker run -d --name MariDb -e MARIADB_ROOT_PASSWORD=bl@dg3r$$ -e MARIADB_DATABASE=mydatabase -e MARIADB_USER=myuser -e MARIADB_PASSWORD=bl@dg3r$$ -p 3306:3306 -v "B:/MariaDBData:/var/lib/mysql" mariadb:10.8  # Stable version

# Run SQL Server (consider LTS version for production)
docker run -d --name SQLServer -e 'ACCEPT_EULA=Y' -e 'SA_PASSWORD=bl@dg3r$$' -p 1433:1433 -v "B:/SQLServerData:/var/opt/mssql" mcr.microsoft.com/mssql/server:2019-latest

# Run Java 17 Container
docker run -d --name TestSpring -p 8080:8080 -v "B:/JavaApp:/usr/src/app" openjdk:17-jdk-slim
