FROM azul/zulu-openjdk:17
COPY web-service/target/web-service-0.0.1-SNAPSHOT.jar webservice.jar
EXPOSE 8080 8081 9010
ENTRYPOINT ["java","-Dcom.sun.management.jmxremote=true","-Dcom.sun.management.jmxremote.port=9010","-Dcom.sun.management.jmxremote.authenticate=false","-Dcom.sun.management.jmxremote.ssl=false","-Djava.rmi.server.hostname=localhost","-Dcom.sun.management.jmxremote.rmi.port=9010","-jar","webservice.jar"]