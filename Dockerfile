FROM openjdk:11
ARG JAR_FILE=build/libs/*.jar
COPY ./target/sensor-data-0.0.1-SNAPSHOT.jar TxT-sensor-data.jar
ENTRYPOINT ["java", "-jar", "/TxT-sensor-data.jar"]
