FROM amazoncorretto:17 AS build

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradlew ./
COPY gradle/ ./gradle/

RUN ./gradlew dependencies --no-daemon

COPY src ./src

COPY script.sql /docker-entrypoint-initdb.d/

RUN ./gradlew bootJar --no-daemon

FROM amazoncorretto:17

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

# Expose port 8080
EXPOSE 8080

ENV JAVA_TOOL_OPTIONS="-Dcom.sun.net.ssl.checkRevocation=false"

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
