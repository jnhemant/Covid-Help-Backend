## Covid-Help-Backend
#Spring Boot Restful service for CRUD operations

#Framework: Spring Boot
#Build Tool: Maven
#Database used: Elasticsearch
#Auth: Spring Security

#Steps to run the project
#Add indices in elasticsearch and run it. (indices: oxygenCylinder, oxygenConcentrator, plasma, remdesivir)
cd ~/(path to pom.xml)
mvn clean install
mvn spring-boot:run
