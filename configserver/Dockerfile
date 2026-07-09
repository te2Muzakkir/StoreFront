FROM eclipse-temurin:17-jre
LABEL org.opencontainers.image.title="StoreFront Config Server" \
      org.opencontainers.image.description="StoreFront Config Server" \
      org.opencontainers.image.version="1.0.0" \
      org.opencontainers.image.vendor="StoreFront" \
      org.opencontainers.image.authors="Syed Muzakkir"
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 9030
ENTRYPOINT ["java","-jar","app.jar"]