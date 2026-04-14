FROM adoptopenjdk/openjdk11:alpine-jre

COPY target/*.jar bayaniserver.jar

ENTRYPOINT ["java", "-jar", "/bayaniserver.jar"]