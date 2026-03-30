FROM eclipse-temurin:17-jdk-jammy

# Limit memory (safe for your laptop)
ENV JAVA_OPTS="-Xms64m -Xmx128m"

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8761

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]