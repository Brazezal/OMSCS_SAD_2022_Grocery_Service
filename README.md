# Example Dockerized Java Service

This repository contains an example Java service to help get your group project started.
It contains two Docker separate service to demonstrate a how we may deploy a separate service for an application server and a front end service. In this example, we have a simple Java backend that uses [Spring Boot](https://spring.io/projects/spring-boot) as our web service framework, and an [nginx](https://www.nginx.com/) web server that serves our html file.

For your convenience we have defined a [docker-compose](https://docs.docker.com/compose/) file to help define our application and orchestrate the deployment locally, as well as a [Makefile](https://www.gnu.org/software/make/manual/make.html) that effectively wraps some build commands for you.

# Quickstart
We can simply run `make && make up` to start our service. For those that do not have `make` installed, we can effectively do the same thing by running the following:
```
docker build -t gatech/backend -f ./images/Dockerfile.backend ./backend
docker-compose -p gatech -f docker-compose.yml up -d
```
The first command is building the backend(with frontend together as both using springboot) images defined by our dockerfiles, and the second is using docker-compose to deploy the service locally.
To break down the first command we can read it as "build an imageusing the `./images/Dockerfile.backend` file from the `./backend` directory and tag it as `gatech/backend`"
To break down the second command, we can read it as "deploy a service called `gatech` as defined in file `docker-compose.yml`
As our application using springboot and JPA, when application start, it need to connect mysql database (database should keep running). 
It may have some glitching when initially start of backend container then it needs to run backend container again until it runs smoothly.

# docker-compose.yml
This file is well commented for additional details, and we recommend reading over this file to understand what is happening.

# Frontend and Backend
This service (as defined in our docker-compose file) is exposed on port 8080(main page, http://localhost:8080/).
Our Frontend uses Spring Boot Thymeleaf, after navigating to the main page, you can visit the different pages of the application.
A lot of this backend uses the [Spring Boot](https://spring.io/projects/spring-boot) framework. Take a look at the documentation to familiarize yourself with it.

# Database
The database is MySQL 8.0.28

# Maven 
This project uses [Apache Maven](https://maven.apache.org/) to manage itself. 
You will see the dependencies defined in `/backend/pom.xml`, and the maven commands called by the backend's Dockerfile


