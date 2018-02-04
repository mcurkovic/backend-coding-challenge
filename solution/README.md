# Running solution
## 1. Edit DB configuration
Before running the solution edit backend database connection properties with appropriate values, see configuration file:
*_./solution/src/main/resources/application.properties_*. Following entries must be defined (here are example values):
-   spring.datasource.url=jdbc:mysql://localhost:3306/test
-   spring.datasource.username=test
-   spring.datasource.password=password

## 2. Run applications
1. run frontend app (runs on http://localhost:8080):
./gulp
2. run backend app (runs on http://locahost:8090):
./solution/./gradlew bootRun

# Solution
All user stories are covered by solution. Simple client side validation is implemented using pattern validation (date, amount of expense) and standard required field validation. Additional full validation is done serverside.
Security is implemented using Basic Authenitcation mechanism. Basic authenticaiton properties can be edited in ./solution/src/main/resources/applicatin.properties configuration file.

Technology stack used:
- Spring Boot
- Gradle
- Retrofit2
    - OkHttpClient
- Liquibase - database migration tool
- Spring Data JPA
- fixer.io service for fetching exchange rates

Gradle as build tool is chosen just so solution can be run without any additional installation, just running gradlew.sh script.

## Model
Model consists of two entities: Expense and User. Latter is implemented just for demonstration purposes. Solution does handle only Expense entity (save and findAll expenses).
