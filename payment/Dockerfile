FROM eclipse-temurin:17-jre
LABEL org.opencontainers.image.title="StoreFront Payment Service" \
      org.opencontainers.image.description="StoreFront Payment Microservice" \
      org.opencontainers.image.version="1.0.0" \
      org.opencontainers.image.vendor="StoreFront" \
      org.opencontainers.image.authors="Syed Muzakkir"
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 9020
ENTRYPOINT ["java","-jar","app.jar"]