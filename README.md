# Flight Reservation System

A simple flight reservation system built with Spring Boot, PostgreSQL and Docker

## Installation and Running

1. Clone the repository:

git clone https://github.com/BartoszBaniak/Flight-Reservation-System.git 
cd Flight-Reservation-System

2. Build the application

mvn clean package -DskipTests

4. Run the application with Docker:

docker-compose up --build

The application will start along with a PostgreSQL database. Sample airport data is automatically initialized for testing purposes.

## API Documentation

Once running, the API documentation is available at:
http://localhost:8080/swagger-ui/index.html#
