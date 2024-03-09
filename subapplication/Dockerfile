FROM eclipse-temurin:17-jdk-alpine
WORKDIR /subapp
COPY build/libs/*.jar subapp.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","subapp.jar"]