# Project Repo Information

Import this repository into Eclipse. This project provides everything needed to:

* Host the web application on a local web server
* Run unit tests with coverage
* Run acceptance tests

**To run JUnit tests:**

Right-click project -> Run As -> "Maven test"

**To generate coverage report for JUnit tests:**

Right-click "cobertura.launch" -> Run As -> "cobertura".

**To host your web application:**

Right-click "run.launch" -> Run As -> "run". It will be hosted on https://localhost:8080.

**To run Cucumber tests:**

Make sure the web server is running when you run the Cucumber tests. Right-click "cucumber.launch" -> Run As -> "cucumber".

**To spin up an initialized local database:**

Run `docker-compose up` in a terminal at the root directory of this repository.