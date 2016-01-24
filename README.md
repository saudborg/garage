# Ysura-The Garage
This project was developed for Ysura. It represents a system that simulate a garage.

### Prerequisities
- [Apache Maven](https://maven.apache.org/index.html)


## Built with
- Java 1.6
- Maven 3.2.1
- Spring Boot (JPA, TEST, MVC)
- HSQLDB (run time)
- JUnit
- Thymeleaf
- Bootstrap
- JQuery

## Installation
- Clone the project
- Download all the maven dependencies in the project 


### How to use 
To exectute the system you just have to run [Application.java](https://github.com/saudborg/garage/blob/master/src/main/java/com/sauloborges/ysura/Application.java).
The system will read the variables on application.context : 
- main (boolean): True - If the system will execute via console / False - Only via web
- main.levels (Integer): How many floors the garage should has
- main.levels.spaces (Integer): How many spaces in each level the garage should has
- main.spaces.fit (Integer): How many spaces will be filled by the system before the interaction: Ex: 10 vehicles in the garage.

All of informations you can see in log console.

### How it works (Web Application)
The system was developed using the concept MVC. After you run Application.java the system will be avaiable at http://localhost:8080/
- /home - This screen represents the main screen. Here you can see the building, each floor with your spaces. If there any vehicle in garage, in this screen you can see where;
- /settings - This screen you can change all the attributes. Level: How many levels the garage will has / Spaces: How many spaces in each level the garage will has / Fill Spaces: How many vehicles would you like to put before you start the application. NOTE: Everytime that you submit, the system will remove all vehicles from garage, but doesn't remove the vehicle from the system.
- /vehicle - This screen you will put a licence plate which you would like to know if it exists
- /createAVehicle - If the vehicle doesn't exists on system, will show this screen, where you can create. You have to choose the type of vehicle and the licence plate. After submit the system saves and put in the garage if has spaces enough.
- /detailsVehicle - If you search and the system finds, but doesn't in the garage, in this screen you have the possibility to put it on again if the building has spaces free
- /showVehicleInGarage - If you search and the system find in the garage, in this screen you can see exactly where it is and other details. Also you can take it out from garage


### How it works (Console Application)
Basicly the system works in the same way, only difference is everthing is on console. To execute you have to change de variable 'main' to true on application.properties and then execute Application.java
IIt reads the variables on the application.properties to set how many floors, how many  spaces in each floor and if you would like and fill some vehicles .
After this you will see:
Please select your option (Just numbers)
<br>
<br>
<br>
1 - ) Find a car (Here the system searchs if the vehicle exists or not. If founds show where is it)
<br>
2 - ) Add a car (Here the system creates a vehicle type car after you put the licence plate)
<br>
3 - ) Add a motorbike (Here the system creates a vehicle type motorbike after you put the licence plate)
<br>
4 - ) Remove a vehicle (Here the system removes a vehicle from garage after you put the licence plate)
<br>
5 - ) Get a vehicle (Here the system shows the vehicle details. If the vehicle is outside from garage, you have the option to put it)
<br>



