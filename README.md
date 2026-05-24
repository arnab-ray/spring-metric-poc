# Spring Metric POC

A Spring Boot application demonstrating custom latency metrics using Micrometer and AOP.

## Features

- Custom `@MeasureLatency` annotation for method-level latency tracking
- Micrometer integration with Prometheus metrics
- Prometheus for metrics collection and querying
- Grafana for metrics visualization and dashboards
- PostgreSQL database with JPA
- Docker containerization with docker-compose

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
- **Prometheus UI**: http://localhost:9090
- **Grafana UI**: http://localhost:3000 (default credentials: `admin`/`admin`)

## Testing Metrics

### Create a user (triggers `user.add` metric)

```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"name": "John Doe", "email": "john@example.com"}'
```

### View the `user_add` metric in Prometheus format

```bash
curl http://localhost:8080/actuator/prometheus | grep user_add
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

## Visualizing Metrics with Grafana

### Setting up Grafana

1. Access Grafana at http://localhost:3000
2. Login with default credentials: `admin`/`admin`
3. Add Prometheus as a data source:
   - Navigate to **Configuration** → **Data Sources** → **Add data source**
   - Select **Prometheus**
   - Set URL to `http://prometheus:9090`
   - Click **Save & Test**

### Creating Dashboards

#### Option 1: Import a pre-built Spring Boot dashboard
- Click **+** → **Import**
- Enter dashboard ID: `4701` (JVM Micrometer) or `11378` (Spring Boot Statistics)
- Select your Prometheus data source
- Click **Import**

#### Option 2: Create custom dashboards
- Click **+** → **Dashboard** → **Add new panel**
- Use PromQL queries to visualize your custom metrics, e.g.:
  - `rate(user_add_count[5m])` - User creation rate
  - `user_add{quantile="0.95"}` - 95th percentile latency
  - `user_add_max` - Maximum latency

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
