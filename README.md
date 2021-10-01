# Tour Guide - Project 8 OpenClassrooms

TourGuide is the back-end of a web application. The purpose of the software is to track users, and find activities close to them with a choice of providers and prices for these activities depending on their preferences.

The application is organized in micro-services, each one with a specific domain (one for the localization, another one for the reward policy, another one for the prices). 

The goal of this project was to assume the scalability of the application by a huge increase of users : we had to design an application capable of assuming 100 000 users in an acceptable delay. 

The application is back-end, but the architecture is complete and a new microservice (for UI) can be added to the project : all the endpoints are functional for the app. 

## Prerequisite

* Java 11.
* Gradle 7.1.1
* SpringBoot
* Docker
* DockerCompose


## Usage

First, you need to build the Docker images of each microservice.
There is four of them (GpsUtilApp, RewardServiceApp, TourGuide, TripPriceApp).

Go in each repository, lauch the following command for each coreponding repository (the names are important here, the DockerCompose is set for specific names).
```bash
docker build -t userapp .
```
```bash
docker build -t rewardsapp .
```
```bash
docker build -t tripapp .
```
```bash
docker build -t gpsapp .
```
Once the Docker images are build, lauch the following command in the root repository : 
```bash
docker-compose up
```
Well done ! The application is running. 

You can now access all endpoints on http://localhost:8080

If you want to access specific endpoints defined by a microservice itself in a container, instead of passing by the UserApp here are the adresses :

TripPricerApp : http://localhost:8083

RewardProxy : http://localhost:8082

GpsUtilProxy : http://localhost:8081


## Tips - Get the API documentation easily
 Once the DockerCompose is up, do a http://localhost:8080/swagger-ui/ to see the API Documentation.
