# Use official gradle image as the base image
FROM gradle:7.2.0-jdk11-hotspot as build

# Set the working directory in the image to /app
WORKDIR /app

# Copy the build.gradle file to download dependencies
COPY ./build.gradle ./build.gradle

# Download dependencies as specified in build.gradle
RUN gradle dependencies

# Copy the rest of the source code
COPY ./src ./src

# Build the application
RUN gradle bootJar

# Start a new build stage to minimize the final image size
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Copy the jar file from the previous build stage
COPY --from=build /app/build/libs/*.jar ./app.jar

# Expose the application's port
EXPOSE 8082

# Set environment variables
ENV MAIL_USERNAME=yongsu0312.kim@gmail.com \
    MAIL_PASSWORD=dfmqcbvvcbnimwci \
    GOOGLE_ID=930660652939-9vqf77ff7pq0ibkskt6hgbqlfgu4rh1t.apps.googleusercontent.com \
    GOOGLE_PW=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 \
    ACCESS_KEY=AKIA5P5UNLUXZSSZHN3V \
    SECRET_KEY=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

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
