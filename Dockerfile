FROM openjdk:8-jre-alpine
COPY /target/lettlaant.jar /lettlaant.jar
EXPOSE 8080
CMD ["java", "-jar", "-Dserver.port=8080", "/lettlaant.jar"]