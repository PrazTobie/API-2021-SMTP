FROM maven:3.8.4-jdk-8
RUN apt-get update && apt-get -y install git

RUN git clone https://github.com/HEIGVD-Course-API/MockMock.git && cd MockMock && mvn package

EXPOSE 25
EXPOSE 8282

CMD ["java", "-jar", "./MockMock/target/MockMock-1.4.0.one-jar.jar", "-p", "25", "-h", "8282"]