#rasberry pi-hez
FROM balenalib/raspberrypi3-debian-openjdk:11--stretch
EXPOSE 8080/tcp
COPY build/libs/restaurantApp-0.0.1-SNAPSHOT.jar docker.jar

ENTRYPOINT ["java","-jar","/docker.jar"]

#Azure
#FROM openjdk:11-jdk-slim
#EXPOSE 8080/tcp
#COPY build/libs/restaurantApp-0.0.1-SNAPSHOT.jar docker.jar

#ENTRYPOINT ["java","-jar","/docker.jar"]