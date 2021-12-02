FROM digitalbaseimagesregistry.azurecr.io/olam/corretto-jdk11:11.0.8

ARG JAR_FILE=target/logic-service-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} logic-service-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ARG profile

ENV spring_profile=${profile}

RUN echo $spring_profile

CMD ["sh", "-c", "java -Dspring.profiles.active=$spring_profile -jar logic-service-0.0.1-SNAPSHOT.jar"]
