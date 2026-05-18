FROM adoptopenjdk/openjdk11:alpine-jre

COPY target/*.jar buyaniserver.jar

ENTRYPOINT ["java", "-jar", "/buyaniserver.jar"]