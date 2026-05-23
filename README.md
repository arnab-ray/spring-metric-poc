# Spring Metric POC

A Spring Boot application demonstrating custom latency metrics using Micrometer and AOP.

## Features

- Custom `@MeasureLatency` annotation for method-level latency tracking
- Micrometer integration with Prometheus metrics
- PostgreSQL database with JPA
- Docker containerization

## Prerequisites

- Docker and Docker Compose
- Java 21 (for local development)
- Maven (for local development)

## Running with Docker Compose

### Start the application

```bash
docker-compose up --build
```

### Run in detached mode

```bash
docker-compose up -d --build
```

### Stop the application

```bash
docker-compose down
```

### Stop and remove volumes

```bash
docker-compose down -v
```

### View logs

```bash
docker-compose logs -f app
```

## Accessing the Application

- **Application**: http://localhost:8080
- **Prometheus Metrics**: http://localhost:8080/actuator/prometheus
- **Health Check**: http://localhost:8080/actuator/health
- **All Metrics**: http://localhost:8080/actuator/metrics

## Testing Metrics

### Create a user (triggers `user.add` metric)

```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"name": "John Doe", "email": "john@example.com"}'
```

### View the `user.add` metric in Prometheus format

```bash
curl http://localhost:8080/actuator/prometheus | grep user.add
```

Expected output:
```
user_add_count 1.0
user_add_sum 205.0
user_add{quantile="0.9",} 205.0
user_add{quantile="0.95",} 205.0
user_add{quantile="0.99",} 205.0
user_add{quantile="0.999",} 205.0
user_add_max 205.0
```

### Get a user by ID

```bash
curl http://localhost:8080/users/{user-id}
```

**Note**: Metrics only appear after the annotated method is invoked at least once.

## Local Development

### Build the project

```bash
./mvnw clean package
```

### Run tests

```bash
./mvnw test
```

### Run locally

```bash
./mvnw spring-boot:run
```

## Custom Metrics

Use the `@MeasureLatency` annotation to track method execution time:

```java
@MeasureLatency("my_custom_metric_name")
public void myMethod() {
    // method implementation
}
```

The annotation supports:
- Custom metric names via the `value` parameter
- Automatic fallback to method name if no custom name is provided
- Percentile tracking (p90, p95, p99, p999)
