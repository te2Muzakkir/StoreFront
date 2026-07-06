

Docker command for store front infra

PostgreSQL
	docker pull postgres:17
	docker run -d --name storefront-postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=storefront -p 5432:5432 postgres:17
	docker ps
	docker exec -it storefront-postgres psql -U postgres
		\l
	docker exec -it storefront-postgres psql -U postgres -d sf_payment
		
RabbitMQ
	docker run -d -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4.0-management
	http://localhost:15672 guest/guest
	
Redis
	docker run -d --name storefront-redis -p 6379:6379 redis:8.8-alpine redis-server --requirepass StrongPassword123
	docker exec -it storefront-redis redis-cli
		> PING gives PONG
		
Prometheus
	docker run -d --name storefront-prometheus -p 9090:9090 -v "%cd%\infrastructure\prometheus\prometheus.yml:/etc/prometheus/prometheus.yml" prom/prometheus:v3.5.0
	http://localhost:9090 
	
Grafana
	docker run -d --name storefront-grafana -p 3000:3000 grafana/grafana:12.2.0
	http://localhost:3000 admin/admin