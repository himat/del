FROM openjdk:8-alpine

COPY target/uberjar/super-browser-tool.jar /super-browser-tool/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/super-browser-tool/app.jar"]
