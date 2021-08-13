FROM openjdk:11.0.12-jdk-oraclelinux8
MAINTAINER squallsv
COPY build/libs/users-0.0.1-SNAPSHOT.jar users-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/users-0.0.1-SNAPSHOT.jar"]