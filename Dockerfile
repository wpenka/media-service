FROM openjdk:8-jdk-alpine
ARG JAR_FILE
ARG CONFIG_FILE
COPY ${JAR_FILE} /opt/balade/service.jar
COPY ${CONFIG_FILE} /opt/balade/application.properties
ENTRYPOINT exec java  -jar /opt/balade/service.jar --spring.config.location=file:/opt/balade/application.properties