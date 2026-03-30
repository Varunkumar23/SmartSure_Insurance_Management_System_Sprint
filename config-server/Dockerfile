FROM eclipse-temurin:17-jdk-jammy
ENV JAVA_OPTS="-Xms64m -Xmx128m"
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8888
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]