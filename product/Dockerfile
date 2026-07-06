FROM eclipse-temurin:17.0.19_7-jre
LABEL org.opencontainers.image.title="StoreFront Product Service" \
      org.opencontainers.image.description="StoreFront Product Microservice" \
      org.opencontainers.image.version="1.0.0" \
      org.opencontainers.image.vendor="StoreFront" \
      org.opencontainers.image.authors="Syed Muzakkir"
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","app.jar"]