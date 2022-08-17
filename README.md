# TechnicalTest

## What you’ll build
You will build and deploy the TechnicalTest API using docker

## What you’ll need
About 10 minutes

Docker

## Installing IntelliJ IDEA
If you don’t have docker installed yet, visit the link https://docs.docker.com/engine/install/

## Getting Started guide
You need some code, so clone or copy the repository:

```
$ git clone https://github.com/Hijail/TechnicalTest.git
```

Go to the folder you just downloaded and open shell

execute the following commands
```
docker build -t technical-test .
docker run -p 8080:8080 -t technical-test
```

Now the Technical Test API is launched, and you can use it 

## How to use API

to see the different routes a swagger is available on the url http://localhost:8080/swagger-ui.html

and to have examples of use you have the postman collection available in the directory ./documentation of the project

