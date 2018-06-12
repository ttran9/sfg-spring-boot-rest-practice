[![CircleCI](https://circleci.com/gh/springframeworkguru/spring-rest-client-examples.svg?style=svg)]
(https://circleci.com/gh/springframeworkguru/spring-rest-client-examples)
# Spring Rest Client

This repository is for an example application built from John Thompson's Udemy course, Spring Framework 5 - Beginner to Guru

The course can be seen [here.](http://courses.springframework.guru/p/spring-framework-5-begginer-to-guru/?product_id=363173)

# Running In Local Environment

- go to the root directory and enter in mvn spring-boot:run
    - you can download maven [here](https://maven.apache.org/download.cgi).
        - make sure maven is on your path to run it from the command prompt/terminal.
    - if there are issues with this command ensure a few things
        1) your JAVA_HOME environment points to where your Java JDK is.
        2) ensure that your local .m2 repository has the proper dependencies as required in the pom.xml file. 
        
- To simulate the HTTP Methods GET, POST, PUT, PATCH, DELETE, AND POST I used [Postman](https://www.getpostman.com/apps) 
to do test the requests.       

- Remember it may be necessary to use mvn clean and compile commands if there are errors related to the mapper objects
(the three mappers for the three POJOs).