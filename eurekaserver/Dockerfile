FROM eclipse-temurin:17-jre
LABEL org.opencontainers.image.title="StoreFront Eureka Server" \
      org.opencontainers.image.description="StoreFront Eureka Server" \
      org.opencontainers.image.version="1.0.0" \
      org.opencontainers.image.vendor="StoreFront" \
      org.opencontainers.image.authors="Syed Muzakkir"
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 9035
ENTRYPOINT ["java","-jar","app.jar"]