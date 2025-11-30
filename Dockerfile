FROM eclipse-temurin:21-jre-alpine

# Set the working directory in the container
WORKDIR /af_app

# Copy the JAR file into the container at /educacaoGamificada
COPY target/*.jar af_app.jar

RUN addgroup -S spring && adduser -S spring -G spring
USER spring
# Expose the port that your application will run on
EXPOSE 8585

# Specify the command to run on container start
ENTRYPOINT ["java", "-jar", "af_app.jar"]
