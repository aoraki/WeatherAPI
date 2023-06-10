# WeatherAPI Rest Service

### Overview
This application is a REST API.  The WeatherAPI is designed to accept readings from multiple weather sensors for various weather metrics such as temperature, rainfall, windspeed, etc.  The sensor reading data is kept in a persistent store and using the WeatherAPI, queries can be made against the sensor data.  A query might take the form of "give me the average windspeed for sensor 1 in the last week".

### Pre-requisites
* Git (For cloning purposes)
* A Java 11 JVM (JRE or JDK)
* Docker Engine (Optional - See Docker section below)

### How to clone
```
git clone https://github.com/aoraki/WeatherAPI.git
```

### Tech Stack Overview

**Language** : Java 11.0.12
**IDE** : IntelliJ Ultimate Edition.
**Build Tool** : Gradle Wrapper 7.6.1
**Web Container** : Tomcat (built into Spring Boot)
**Frameworks** : Spring Boot (2.7.12), Spring Boot Starter JPA, Spring Boot Web, Spring Boot Actuator, Spring Boot Validation, Springdoc OpenAPI, Spring Boot Starter Test.
**Persistence** : H2 Database (built into Spring Boot)

Java 11 was used mainly for convenience purely because it was the version of Java that I had installed on my machine.  This dictated the version of Spring Boot I could use.  For the data access layer and persistence I went with Spring Boot JPA and H2 as the DB.  Again these were the most convenient to use and made sense for a POC project of this nature where there were time constraints.

### How to build the project
This is a Gradle project.  The Gradle wrapper has been provided for convenience, the version of the wrapper provided is `7.6.1`.

To build the project, run unit tests, generate a test coverage report and build an executable Jar file simply navigate to the root of the repo and run this command;
```
./gradlew build
```

### How to run the WeatherAPI app
There are a few different ways to run the WeatherAPI

1. Directly from your IDE.  Open up the WeatherAPI repo in an IDE such as IntelliJ and you can run the `WeatherApiApplication.class` as a Spring Boot application
2. Build and run the Jar using the `java` command.  Build the project as outlined in the section above.  Once built, from the root of the repository run the following command
```
java -jar build/libs/WeatherAPI-0.0.1-SNAPSHOT.jar 
```
3. Using Docker (see *Optional Docker Support* section below)

### How to use interact with the WeatherAPI app
Once the WeatherAPI app is running there are a few different ways you can make requests against it;

1. For IntelliJ you can use the IntelliJ http client.  A `requests.http` file has been provided in the root folder of the repo, and in it are test requests for each of the endpoints in the WeatherAPI app.
2. Using a tool like Postman.
3. Using cURL (for purists only!)
4. Using Swagger (see below for the URL)

### Design Decisions/Assumptions Made

* The app was built to ingest data for 4 different weather metrics; Temperature, Windspeed, Rainfall and Humidity.  I just picked these 4 to work with.
* A sensor can submit data for any of the 4 metrics.  So a sensor could send a reading of temperature, windspeed, rainfall and humidity to the server in one request.  It does not have to send all 4 however.
* Each submission of data to the server will come with a unique sampleId which is supplied by the sensor and is part of the data payload to the server.
* When querying collected data, you specify the metrics you want to query (you can base your query for a single metric, or for all 4 of them), the statistic type you want to apply to the data (avg, min, max etc) and a data range.  You can also narrow your query to certain sensors, or you can query for all sensors.
* If you don't provide a data range in the query request the server will use the current day as the date range (this constitutes the "latest data" stipulation in the assignment brief).

### REST API Endpoints

#### To view Swagger 3 API docs
Swagger has been enabled for the project.  To access the swagger UI, run the server and browse to http://localhost:8080/swagger-ui.html

### Database Access
The database used for this project is the embedded H2 database that comes with Spring boot.  When the WeatherAPI app is running, you can access the H2 console at the following url; `http://localhost:8080/h2`.  The credentials are as follows;
```
Usr : sa
Pwd : Password
```
If you want

### Optional Docker Support

**NB!** This requires that you have Docker locally installed on your machine.   A docker file has been provided in the repo which outlines a virtualized runtime to run a Java 11 REST application, exposing the port 8080 to the outside world.  The Dockerfile is located in the root of the code repository.

To build an image from the Dockerfile navigate to the root of the code repository and run the following command;
```
docker build -t weather-api .
```
To run the newly built image as a container run the following command;
```
docker run -p 8080:8080 weather-api
```
You can send requests to http://localhost:8080 the same way you would if you were running the Jar file directly or running the WeatherAPI app directly from your IDE.

### Continuous Integration
The CI in CI/CD.  This repo is housed in Github so advantage can be taken of the built-in Github Actions capability of Github.  A simple workflow file has been provided in the repo at `.github/workflow/gradle-build.yml`.  This workflow will check-out the repo, set up a Java 11 environment and then run the gradle wrapper build command.  The build will also trigger the unit tests, generate a Jacoco test Coverage report and build a Jar file.

### Test Coverage
More time was probably spent writing unit tests than the application code itself :).  I tried to focus on tests at the controller and service layer, as well as integration tests.  I have also provided a couple of basic tests at the JPA repository layer, mainly to demonstrate how you would write tests at that layer.  But definitely more tests need to be added.  At the time of writing there are
80 separate tests with a line coverage of 96%.

The Jacoco plugin has been installed in the gradle build environment.  Each build of the project will run the unit tests and a HTML test coverage report will be generated at `build/jacoco/test/html/index.html`

### Future Enhancements/Improvements
- Update to use latest versions of Java/Spring Boot
- Add more unit tests
- Use a different DB to H2.  Because H2 should never be used for anything other than simple dev work.
- Refactor the DB Repository layer part of the application.  Due to time contraints I used a simple JPA Repository which is super easy to
  set up and great for simple CRUD operations.  However I would move to
  a mechanism that would allow me to programatically build the database queries on the fly.  The approach I have taken will not scale well, but was taken due to the POC nature of this project and time constraints,  This
  would be the first improvement I would make.
- Oh, did I mention more unit tests? :)

### Hardening For Production
The following are some common steps you would take if you were to take this basic POC project and get it ready for Production.

* Build out a full CI/CD pipeline.
* Load/Bench Testing
* Profiling of application to ensure no nasty surprises like memory leaks.
* Monitoring/Alerting/Metric Gathering. Focussing on the Golden Signals for RESTful APIs and Databases.  Ensuring the App is running the various health/readiness/liveness endpoints that are required to enable monitoring.
* Use of a logging tool such as Splunk/Datadog.  And when doing so making sure that your logging volumes are not excessive ($$$ Considerations)
* Deploy to a proper cloud environment in AWS or some other Cloud provider.  Build on the basic containerization capability provided and maybe consider the use of something like ECS Fargate to run the container, with something like RDS Aurora as the DB.  Container images to be stored in a image repo like ECR.
* Build any cloud infrastructure using Terraform (or Cloudformation) and hook it into the CI/CD pipeline.
* Separate environments for Dev, Test and Prod
* Security.  Using TLS with a proper rooted certificate and consider the use of OAuth Token based Authentication/Authorization.  Any secrets or sensitive information within the application need to be externalized and read in at runtime/deploy time from a secure location (such as Hashicorp Vault or AWS Secrets Manager)
* Use of a DB Schema management tool such as Flyway or Liquibase.  Build it into the CI/CD pipeline.



Sensor data submission example payload
```
{
	"sampleId": 3,
	"sensorId": 2,
	"metrics": [{
		"metricType": "HUMIDITY",
		"metricValue": 14.0
	}, {
		"metricType": "RAINFALL",
		"metricValue": 18.0
	}, {
		"metricType": "TEMPERATURE",
		"metricValue": 22.0
	}, {
		"metricType": "WINDSPEED",
		"metricValue": 10.0
	}]
}
```
For the data submission payload, all three attributes are required.  Sample Id must be unique for each submission.  The metrics list must have at least 1 metric entry, the metricType must be one of the 4 enumerated types shown above, and the value must be compatible with a double type.


Database Schema
With the above assumptions in mind, I chose a very basic DB schema with just one table called `sensor_data`.