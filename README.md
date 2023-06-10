# WeatherAPI Rest Service

### Overview
The WeatherAPI is a RESTful API designed to accept readings from multiple weather sensors for various weather metrics such as temperature, rainfall, windspeed, etc.  The sensor reading data is kept in a persistent store and using the WeatherAPI, queries can be made against the sensor data.  A query might take the form of "give me the average windspeed for sensor 1 in the last week".

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
* Although the spec didn't call for it, I provided CRUD endpoints for the records held in the sensor data DB.  The query endpoint allows you make nuanced queries against the data held in the DB, whereas the CRUD endpoints are just standard endpoints to allow you to manipulate the raw data held in the DB.

### Database Considerations

#### Schema

With the above assumptions in mind, I chose a very basic DB schema with just one table called `sensor_data`.  I could have split it into separate tables for each metric type, but for the purposes of this exercise I just wanted to keep it really simple.

|FIELD|TYPE|NULL|KEY|DEFAULT|
|--|--|--|--|--|
|ID|BIGINT|NO|PRI|NULL|
|SAMPLE_ID|BIGINT|NO|UNI|NULL|
|SAMPLE_TIME|TIMESTAMP|YES||NULL|
|HUMIDITY|DOUBLE PRECISION|YES||NULL|
|RAINFALL|DOUBLE PRECISION|YES||NULL|
|TEMPERATURE|DOUBLE PRECISION|YES||NULL|
|WINDSPEED|DOUBLE PRECISION|YES||NULL|

#### Pre-loaded Data
The embedded H2 databaseWeatherAPI app comes with some pre-loaded data so that the user has some data to play with when they start up the WeatherAPI.  There are 50 records loaded, randomized across sensor ids 1 to 10.  The date range of these records ranges between 2023-05-08 and 2023-06-10 (using yyyy-MM-dd)



### REST API Endpoints
### Sumbit Metrics
Endpoint for submitting a sensor reading to the WeatherAPI
#### Sample Request
```
POST http://localhost:8080/v1/weather/metrics  
Content-Type: application/json  
  
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
For the data submission payload, sampleId, sensorId and metrics are required.  Sample Id must be unique for each submission.  The metrics list must have at least 1 metric entry present, the metricType must be one of the 4 enumerated types shown above, and the value must be compatible with a double type.

#### Sample Response
```
HTTP / 1.1 201
Content - Type: application / json

{
	"sampleId": 3,
	"sensorId": 2,
	"metrics": [{
			"metricType": "TEMPERATURE",
			"metricValue": 22.0
		},
		{
			"metricType": "WINDSPEED",
			"metricValue": 10.0
		},
		{
			"metricType": "HUMIDITY",
			"metricValue": 14.0
		},
		{
			"metricType": "RAINFALL",
			"metricValue": 18.0
		}
	]
}
```
Will return a 201 for successfully created, with a response payload representing the row of data held in the DB for that reading.  If the reading has a sampleId that has already been used, a 409 conflict will be returned.

In my design I allowed for a scenario where a sensor can submit data for just 1 or 2 metrics, and not for all 4 metrics.  In that case the record in the DB will store the metrics that it received data for, and will store null for the metrics that it didn't get data for.  In the below sample it shows what you would get back if you submitted a reading with just 2 of the metrics;

```
HTTP/1.1 201 
Content-Type: application/json

{
  "sampleId": 5,
  "sensorId": 2,
  "metrics": [
    {
      "metricType": "TEMPERATURE",
      "metricValue": null
    },
    {
      "metricType": "WINDSPEED",
      "metricValue": null
    },
    {
      "metricType": "HUMIDITY",
      "metricValue": 7.0
    },
    {
      "metricType": "RAINFALL",
      "metricValue": 14.0
    }
  ]
}
```
As you can see, for the metrics that we didn't get data for, a value of null is returned.

### Perform complex query of historical sensor data
You can make queries against the sensor data to obtained certain rolled up information over a date range.  You can specify 1 to many sensors in your query (or all of them).

#### Sample Request
```
POST http://localhost:8080/v1/weather/stats  
Content-Type: application/json  
  
{
	"metrics": ["TEMPERATURE", "WINDSPEED"],
	"sensorIds": [4, 5],
	"statType": "AVG",
	"startDate": "2023-06-01",
	"endDate": "2023-06-09"
}
```
A POST may seem a bit odd for a REST call to retrieve data.  However, because the queries can carry a certain level of complexity, I decided to use a request query object to be sent in the payload of the request, rather than passing them in as path parameters.

For example, the above query can be vocalised as "Return the Average Temperature and Windspeed for sensors 4 and 5 between the 1st June and the 9th June 2023"

Another example would be;
```
POST http://localhost:8080/v1/weather/stats  
Content-Type: application/json  
  
{
	"metrics": ["TEMPERATURE", "WINDSPEED"],
	"searchAllSensors": true,
	"statType": "MAX"
}
```
which could be read as "Return the Max values for both Temperature and Windspeed for all Sensors over the current day" (it defaults to the data for the current day if you don't pass in a date range)

The following validation rules apply to the various fields of the query object;
* **metrics** : The weather metrics you are querying for.  This field must be present as a list in the payload, and the list must contain atleast 1 of the 4 enumerated metric types (TEMPERATURE, WINDSPEED, RAINFALL, HUMIDITY).  If the metrics list contains a junk value the API will return with a 400 Bad Request.
* **statType** : Must be present.  The statistic type should be one of the 4 enumerated statistic types (AVG, MIN, MAX, SUM).  If it's a junk value the API will return a 400.
* **sensorIds** : Can be used to specify specific sensors to query data for.  If the sensor ids are junk values (eg. non numerical) the API will return with a 400 Bad Request.  If the sensorIds field is not provided, then the searchAllSensors flag must be provided (see below), otherwise a 400 bad request will ensue.
* **searchAllSensors** : This flag can be provided (with a value set to true) to stipulate that you want to query data over all sensors. If both sensorIds and searchAllSensors is present in the payload, the searchAllSensors field will take precedence.
* **startDate** and **endDate** : The format of both date fields is "**yyyy-MM-dd**".  Can be provided to specify a date range to perform the search across.  startDate must be before or on the same day as endDate.  endDate cannot be a date in the future.  If you provide either one of the fields you must provide the other, otherwise a 400 Bad Request will occur.  startDate and endDate is inclusive, eg. if you provide a startDate of 2023-06-01 and an endDate of 2023-06-10 the server will assume the following in the back end;
```
startDate : 2023-06-01 00:00:00
endDate : 2023-06-11 00:00:00   
```
**Alternatively you can leave out both date fields, and if you do so the server will perform the query assuming that the data range is the current day.  This is probably the best way to test the API as when it starts up there won't be any historical data to work with.**

#### Sample Response
```
POST http://localhost:8080/v1/weather/stats

HTTP/1.1 200 
Content-Type: application/json

{
  "metricResponses": [
    {
      "metric": "TEMPERATURE",
      "statType": "AVG",
      "metricValue": 20.0
    },
    {
      "metric": "WINDSPEED",
      "statType": "AVG",
      "metricValue": 10.0
    }
  ]
}
```
If the criteria specified in the query fails to find any data, a null value will be returned to signify the absence of data.  I felt this was more appropriate than a 404 as you could have a situation where you are looking for data for 2 metrics, and there is data for one but not the other.

### Get Sensor reading by it's sample ID
You can retrieve individual sensor readings from the database, using the unique sampleId associated with the reading.
#### Sample Request
```
GET http://localhost:8080/v1/weather/metrics/3  
Content-Type: application/json
```
You pass in the sampleId as a path parameter.

#### Sample Response
```
HTTP/1.1 200 
Content-Type: application/json

{
  "sampleId": 3,
  "sensorId": 2,
  "metrics": [
    {
      "metricType": "TEMPERATURE",
      "metricValue": 22.0
    },
    {
      "metricType": "WINDSPEED",
      "metricValue": 10.0
    },
    {
      "metricType": "HUMIDITY",
      "metricValue": 14.0
    },
    {
      "metricType": "RAINFALL",
      "metricValue": 18.0
    }
  ]
}
```
The API will return a 404 not found if a reading for the sampleId provided cannot be found.

### Get All Sensor Readings
You can retrieve all sensor readings from the DB

#### Sample Request
```
GET http://localhost:8080/v1/weather/metrics  
Content-Type: application/json
```

#### Sample Response
```
HTTP/1.1 200 
Content-Type: application/json

[
  {
    "sampleId": 3,
    "sensorId": 2,
    "metrics": [
      {
        "metricType": "TEMPERATURE",
        "metricValue": 22.0
      },
      {
        "metricType": "WINDSPEED",
        "metricValue": 10.0
      },
      {
        "metricType": "HUMIDITY",
        "metricValue": 14.0
      },
      {
        "metricType": "RAINFALL",
        "metricValue": 18.0
      }
    ]
  },
  {
    "sampleId": 4,
    "sensorId": 2,
    "metrics": [
      {
        "metricType": "TEMPERATURE",
        "metricValue": 20.0
      },
      {
        "metricType": "WINDSPEED",
        "metricValue": 10.0
      },
      {
        "metricType": "HUMIDITY",
        "metricValue": 7.0
      },
      {
        "metricType": "RAINFALL",
        "metricValue": 14.0
      }
    ]
  }
]
```
The API will return an empty list if there are no readings in the DB.

### Update a Sensor Reading
You can update an individual sensor reading in the DB

#### Sample Request
```
PATCH http://localhost:8080/v1/weather/metrics  
Content-Type: application/json  
  
{
	"sampleId": 1,
	"sensorId": 2,
	"metrics": [{
		"metricType": "HUMIDITY",
		"metricValue": 10.0
	}, {
		"metricType": "RAINFALL",
		"metricValue": 150.0
	}]
}
```
Please note that is a PATCH request, meaning that you can do a partial update of the metric data held for that particular reading.

#### Sample Response
```
HTTP/1.1 200 
Content-Type: application/json

{
  "sampleId": 3,
  "sensorId": 2,
  "metrics": [
    {
      "metricType": "TEMPERATURE",
      "metricValue": 22.0
    },
    {
      "metricType": "WINDSPEED",
      "metricValue": 10.0
    },
    {
      "metricType": "HUMIDITY",
      "metricValue": 10.0
    },
    {
      "metricType": "RAINFALL",
      "metricValue": 150.0
    }
  ]
}
```
The API will return a 404 if it can't find the record you are trying to update


### Delete a Sensor Reading
You can delete an individual sensor reading in the DB using it's sampleId

#### Sample Request
```
DELETE http://localhost:8080/v1/weather/metrics/2  
Content-Type: application/json
```
Please note that is a PATCH request, meaning that you can do a partial update of the metric data held for that particular reading.

#### Sample Response
```
HTTP/1.1 200 
Content-Type: application/json

true
```
The API will return a 404 if it can't find the record you are trying to delete.  Otherwise it will return a 200 with a simple payload of "true"

#### To view Swagger 3 API docs
Swagger has been enabled for the project.  To access the swagger UI, run the server and browse to http://localhost:8080/swagger-ui.html

### Database Access
The database used for this project is the embedded H2 database that comes with Spring boot.  When the WeatherAPI app is running, you can access the H2 console at the following url; `http://localhost:8080/h2`.  The credentials are as follows;
```
Usr : sa
Pwd : Password
```
A sample query to run would be;
```
SELECT * from SENSOR_DATA
```
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