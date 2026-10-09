FROM eclipse-temurin:21-jre

# Run as a non-root user - the image shouldn't execute as root even though nothing here
# requires elevated privileges.
RUN useradd --uid 1111 --no-create-home --shell /usr/sbin/nologin app
USER 1111

WORKDIR /app
COPY target/jumbo-store-locator-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
