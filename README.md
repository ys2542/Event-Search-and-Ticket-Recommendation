# Event Search and Ticket Recommendation
An interactive web application full stack project for users to search events by their geolocations and get recommendations based on their favorite records.(Events are fetched from TicketMaster API)

## Tech Stack
* Front end: HTML/CSS/JavaScript
* Back end: JAVA8(Eclipse JEE)
* Database: MYSQL MongoDB
* Java servlet: Apache Tomcat v9.0
* Test: Apache JMeter

## Logic layer
![alt text](/demo/1.png)

## Recommendation Algorithm(content-based)
In this project, I recommend events based on categories that the user has favorited. By knowing the category of the item the user favorited, I recommend some events belong to this category nearby this user. 

Concrete steps are as follow:
* Fetch all the events (ids) this user has visited.
* Given all these events, fetch the categories of these events. 
* Given these categories, find what are the events that belong to them. 
* Filter events that this user has visited. 
* Sort the recommendation list on ascending order of distance between recommended events's locations and user's location.

## RPC
Java servelet could handle the lists RPCs below:
* login
* logout
* register
* search items
* recommend items
* set/unset favorite items

## Database
I implement two databases: MySQL and MongoDB. I prefer MySQL in the project at first, but if I need to scale the prject, MongoDB could be a good choice. The image below is the schema for MySQL database of this project.
### Schema of data in MySQL database
![alt text](/demo/2.png)

## Screenshots
### login/logout
![alt text](/demo/4.png)
### register
![alt text](/demo/5.png)
### search
![alt text](/demo/6.png)
### favorite
![alt text](/demo/7.png)
### recommendation 
![alt text](/demo/8.png)

## Deployment
Deployment Environment: Amazon EC2 
[demo link](http://54.202.63.63/Event-Search-Recommendation-Engine/) 

## Test
I use Apache JMeter to test the maximum QPS. The peak throughput happens between 1000 and 2000 threads, so I can test more data points between 1000 and 2000 threads. The throughput will be around 150QPS. When I used 2500 threads, I got errors, which means the memory size is my server's bottleneck.
### Summarized report
![alt text](/demo/3.png)

