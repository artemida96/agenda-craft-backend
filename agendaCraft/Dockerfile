# Use a specific version of the JDK
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory within the container
WORKDIR /app

# Copy the JAR file from the build context into the container
COPY target/agendaCraft-0.0.1-SNAPSHOT.jar agendaCraft.jar


COPY backup.sh /backup.sh
RUN chmod +x /backup.sh

# Start the Java application
CMD ["java", "-jar", "agendaCraft.jar"]