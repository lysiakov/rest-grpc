To run project:  
    1. `mvn clean install`  
    2. `docker build . -t webservice:latest`  
    3. `docker run -p8080:8080 -p8081:8081 -p9010:9010 --name webservice webservice:latest` 


To stop project in docker: `docker stop webservice`