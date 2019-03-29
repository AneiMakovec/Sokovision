# Sokovision

Sokovision is an application for visualizing the solving of Sokoban problems using multiple solving algorithms.

WARRNING: work in progress!

## Prerequisites

To use this application you will need [Java](https://www.java.com/en/) and [Maven](http://maven.apache.org/download.cgi), a project management tool, installed on your system.

A quick guide to install Maven on Windows is available [here](https://maven.apache.org/guides/getting-started/windows-prerequisites.html).

Also [here](http://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) you can find a comprehensive guide to using Maven.

## Download and build

After downloading the project open up a terminal in Linux/Mac or launch Maven in Windows then navigate into the downloaded folder. Run the following command:

```
mvn clean compile assembly:single
```

After the process is completed a folder ```target``` will appear in the project folder. Inside you will find a JAR file called ```Sokovision-xxx.jar```. Double-click on it to start the application.
