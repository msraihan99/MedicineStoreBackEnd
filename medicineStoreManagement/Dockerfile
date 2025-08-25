# Use official OpenJDK image as base
FROM openjdk:17-jdk-slim

# Set working directory inside container
WORKDIR /app

# 3. Copy pom.xml and source files
COPY pom.xml .
COPY src ./src

# 4. Install Maven (in Alpine)
RUN apk add --no-cache maven

# 5. Build the Spring Boot project
RUN mvn clean package -DskipTests

# Copy the JAR file into the container
COPY target/medicineStoreManagement-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8095

# Run the JAR file
ENTRYPOINT ["java","-jar","app.jar"]
