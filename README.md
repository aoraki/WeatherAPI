Minimum Requirements - Java 11.  Was the version of Java currently installed on my machine so just went with that.  
It restricts the version of Spring that can be used but that is fine for a POC style project.
Gradle wrapper is included for convenience, no need to have it installed locally on your machine

Steps for improvement/Production Hardening
More test coverage
Support the use of TLS
Add token based authorization/authentication
Monitoring and Alerting
Deploy it to AWS.  Some potential architecture
API Gateway - App running on ECS Fargate - a DB running in RDS.  Probably something like Aurora or Maria DB.  Other alternatives to
an SQL Based DB is DynamoDB.  Would need to refactor the app to handle that