FROM openjdk:17

EXPOSE 8081

COPY target/Diplom-0.0.1-SNAPSHOT.jar diplom.jar

CMD ["java", "-jar", "diplom.jar"]


