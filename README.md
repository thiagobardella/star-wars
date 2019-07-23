# star-wars
Star wars API for managing planets data

## Autor: Thiago Bardella ##
 
 This implementation uses the following technologies:
 
 *   JDK 8
 *   Spring Boot Framework 2.1.6
 *   Gradle build tool
 *   JPA
 *   Swagger 
 *   springfox-swagger2 - 2.9.2
 *   springfox-swagger-ui - 2.9.2
 *   MySQL Server 8.0
 
 To run this application you must:
  
 *   clone this git repository 
 *   import this project on your favorite IDE as a gradle project
 *   change default values for database connection settings (`spring.datasource.url`) and credentials (`spring.datasource.username` and `spring.datasource.password`) on file 
    
    application.properties
    
 *   run the following gradle tasks 

    build
    bootRun
  
  or you can run from terminal with
  
    gradle build
    gradle bootRun
    

 ###    API Documentation
 
 Once application is up, you can access the API documentation through 
 
    http://localhost:8082/swagger-ui.html#/
 