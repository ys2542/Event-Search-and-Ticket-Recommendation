# Event Search and Ticket Recommendation
An interactive web application full stack project for users to search events by their geolocations and get recommendations based on their favorite records.(Events are fetched from TicketMaster API)

## Tech Stack
* Front end: HTML/CSS/JavaScript
* Back end: JAVA8(Eclipse JEE)
* Database: MYSQL MongoDB
* Java servlet: Apache Tomcat v9.0
* Test: Apache JMeter

## Recommendation Algorithm(content-based)
In this project, I recommend events based on categories that the user has favorited. By knowing the category of the item the user favorited, I recommend some events belong to this category nearby this user. 

Concrete steps are as follow:
* Fetch all the events (ids) this user has visited.
* Given all these events, fetch the categories of these events. 
* Given these categories, find what are the events that belong to them. 
* Filter events that this user has visited. 
* Sort the recommendation list on ascending order of distance between recommended events's locations and user's location.

## RPC
Java servelet could handle the lists RPCs below.
* login
* logout
* register
* search items
* recommend items
* set/unset favorite items

## Database
I implement two databases: MySQL and MongoDB. I prefer MySQL in the project at first, but if I need to scale the prject, MongoDB could be a good choice;





## Screenshots
logic layer of this project
![](https://github.com/weijian2/Event-Search-Recommendation-Engine/raw/master/demoPics/logic.png)
login page
![](https://github.com/weijian2/Event-Search-Recommendation-Engine/raw/master/demoPics/login.png)
nearby page
![](https://github.com/weijian2/Event-Search-Recommendation-Engine/raw/master/demoPics/nearby.png)
favorite page
![](https://github.com/weijian2/Event-Search-Recommendation-Engine/raw/master/demoPics/favorite.png)
recommendation page
![](https://github.com/weijian2/Event-Search-Recommendation-Engine/raw/master/demoPics/recommendation.png)

## Deployment
Deployment Environment: Amazon EC2 <br>
[demo link](http://54.202.63.63/Event-Search-Recommendation-Engine/) <br>

## Test
Tool: Apache JMeter

