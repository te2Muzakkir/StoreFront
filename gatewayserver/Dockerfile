FROM eclipse-temurin:17-jre
LABEL org.opencontainers.image.title="StoreFront Gateway" \
      org.opencontainers.image.description="StoreFront API Gateway" \
      org.opencontainers.image.version="1.0.0" \
      org.opencontainers.image.vendor="StoreFront" \
      org.opencontainers.image.authors="Syed Muzakkir"
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 9040
ENTRYPOINT ["java","-jar","app.jar"]