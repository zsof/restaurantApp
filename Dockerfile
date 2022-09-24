FROM openjdk:17-alpine
EXPOSE 8080
ADD build/libs/restaurantApp-0.0.1-SNAPSHOT.jar docker.jar
ENTRYPOINT ["java","-jar","/docker.jar"]