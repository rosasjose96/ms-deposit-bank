FROM openjdk:8
VOLUME /temp
EXPOSE 8086
ADD ./target/ms-deposit-bank-0.0.1-SNAPSHOT.jar deposit-service.jar
ENTRYPOINT ["java","-jar","/deposit-service.jar"]