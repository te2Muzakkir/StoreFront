FROM eclipse-temurin:17.0.19_7-jre
LABEL org.opencontainers.image.title="StoreFront Inventory Service" \
      org.opencontainers.image.description="StoreFront Inventory Microservice" \
      org.opencontainers.image.version="1.0.0" \
      org.opencontainers.image.vendor="StoreFront" \
      org.opencontainers.image.authors="Syed Muzakkir"
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 9010
ENTRYPOINT ["java","-jar","app.jar"]