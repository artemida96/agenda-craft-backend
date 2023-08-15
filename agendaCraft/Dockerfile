# Use the official OpenJDK 17 Alpine image as the base
FROM openjdk:17-alpine


# Copy the JAR file from the build context into the container
COPY target/agendaCraft-0.0.1-SNAPSHOT.jar agendaCraft.jar

# Set the entrypoint to run the Java application
CMD ["sh", "-c", "java -jar agendaCraft.jar"]



