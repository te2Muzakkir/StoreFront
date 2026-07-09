# Local Development Guide

## Overview

This guide explains how to run the StoreFront application locally using Eclipse IDE while running the infrastructure components with Docker.

The application services are started directly from Eclipse, while PostgreSQL, RabbitMQ, Redis, Prometheus, and Grafana run in Docker containers.

---

## Prerequisites

- Java 17
- Maven 3.9+
- Eclipse IDE
- Docker Desktop
- Git

---

## Clone Repository

```bash
git clone ...
```

---

## Import into Eclipse

- Import Existing Maven Projects
- Update Maven Dependencies
- Build Workspace

---

## Infrastructure

Infrastructure is started using Docker.

Application services are started from Eclipse.

---

## Start PostgreSQL

```bash
	docker compose up -d postgres
		(OR)
	docker pull postgres:17
	docker run -d --name storefront-postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=storefront -p 5432:5432 postgres:17
	docker ps
	docker exec -it storefront-postgres psql -U postgres
		#\l
	docker exec -it storefront-postgres psql -U postgres -d sf_payment
```

Verify

```bash
docker ps
```

---

## Start RabbitMQ

```bash
	docker compose up -d rabbitmq
		(OR)
	docker run -d -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4.0-management
```

Verify

```
http://localhost:15672 guest/guest
```

---

## Start Redis

```bash
	docker compose up -d redis
		(OR)
	docker run -d --name storefront-redis -p 6379:6379 redis:8.8-alpine redis-server --requirepass StrongPassword123
	docker exec -it storefront-redis redis-cli
		> PING gives PONG
```

---

## Start Prometheus

```bash
	docker compose up -d prometheus
		(OR)
	docker run -d --name storefront-prometheus -p 9090:9090 -v "%cd%\infrastructure\prometheus\prometheus.yml:/etc/prometheus/prometheus.yml" prom/prometheus:v3.5.0
	http://localhost:9090 
	After changing the config in yml file 
	>>> call curl -X POST http://localhost:9090/-/reload -> reloads config without container restart
```

---

## Start Grafana

```bash
	docker compose up -d grafana
		(OR)
	docker run -d --name storefront-grafana -p 3000:3000 grafana/grafana:12.2.0
	http://localhost:3000 admin/admin
```

---

## Verify Infrastructure

| Component | URL |
|------------|-----|
| RabbitMQ | http://localhost:15672 |
| Prometheus | http://localhost:9090 |
| Grafana | http://localhost:3000 |

---

## Start Services from Eclipse

Run the following applications in order.

1. Config Server
2. Eureka Server
3. User Service
4. Product Service
5. Inventory Service
6. Payment Service
7. Order Service
8. Gateway Server

## Verify Eureka

Once all Spring Boot applications have started, open the Eureka dashboard.

**URL**

```
http://localhost:8761
```

Verify that the following services are registered and have a status of **UP**:

- CONFIG-SERVER
- GATEWAY-SERVER
- USER-SERVICE
- PRODUCT-SERVICE
- INVENTORY-SERVICE
- ORDER-SERVICE
- PAYMENT-SERVICE

If a service is missing:

- Ensure Config Server is running.
- Verify the service started successfully.
- Check the service logs for registration errors.
- Confirm the Eureka URL is correctly configured.

---

## Verify Database

Connect to PostgreSQL using pgAdmin or your preferred SQL client.

Verify the following databases exist:

```
sf_user
sf_product
sf_inventory
sf_order
sf_payment
```

Verify Flyway has created the schema history table.

Example:

```
flyway_schema_history
```

Each service should have its own tables created successfully.

---

## Verify Swagger

Open the Swagger UI for each microservice.

| Service | URL |
|----------|-----|
| User Service | http://localhost:8081/swagger-ui.html |
| Product Service | http://localhost:8082/swagger-ui.html |
| Inventory Service | http://localhost:8083/swagger-ui.html |
| Order Service | http://localhost:8084/swagger-ui.html |
| Payment Service | http://localhost:8085/swagger-ui.html |

Verify that:

- Swagger loads successfully.
- API endpoints are visible.
- Requests can be executed.
- Protected endpoints require JWT authentication.

---

## Verify RabbitMQ

Open the RabbitMQ Management Console.

```
http://localhost:15672
```

Verify the following:

- Exchanges have been created.
- Required queues are present.
- Queue bindings are configured.
- Consumers are connected.
- No messages remain stuck in queues after successful processing.

When placing an order, observe events flowing through RabbitMQ.

---

## Verify Redis

Open a Redis CLI session.

```bash
docker exec -it storefront-redis redis-cli
```

List cached keys.

```bash
KEYS *
```

Trigger a few product retrieval requests and verify product cache entries are created.

---

## Verify Actuator

Each service exposes Spring Boot Actuator endpoints.

Example:

```
http://localhost:8082/actuator/health
```

Expected response:

```json
{
  "status": "UP"
}
```

You can also verify available metrics:

```
http://localhost:8082/actuator/metrics
```

And Prometheus metrics:

```
http://localhost:8082/actuator/prometheus
```

---

## Verify Prometheus

Open Prometheus.

```
http://localhost:9090
```

Navigate to:

```
Status → Targets
```

Verify all configured services are in the **UP** state.

You can also execute sample queries such as:

```
http_server_requests_seconds_count
```

```
jvm_memory_used_bytes
```

```
system_cpu_usage
```

---

## Verify Grafana

Open Grafana.

```
http://localhost:3000
```

Verify:

- Prometheus is configured as the data source.
- Dashboards load successfully.
- JVM metrics are visible.
- HTTP request metrics are updating.
- Business metrics (Products, Orders, Payments, etc.) are displayed.

---

## Smoke Test

Run the following end-to-end workflow to confirm the platform is functioning correctly.

1. Register a new user.
2. Log in and obtain a JWT token.
3. Create a new product.
4. Verify the product appears in the catalog.
5. Add inventory for the product.
6. Place an order.
7. Confirm inventory is reserved.
8. Confirm payment is processed.
9. Verify the order status changes to **CONFIRMED**.
10. Check RabbitMQ to confirm events were processed.
11. Verify Redis contains cached product data.
12. Observe metrics in Grafana updating as requests are made.

If all the above steps complete successfully, the StoreFront platform is running correctly in your local development environment.

---

## Shutdown

Stop Eclipse applications.

Then

```bash
docker compose down
```