<<<<<<< HEAD
# BuyAni Server
# Introduction
Server that is responsible for general server functionality.

# Getting Started
## Pre-requisite
1. [Homebrew](https://docs.brew.sh/)
2. [Java 11](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html)
3. [Apache Maven](https://maven.apache.org/download.cgi)
4. [MySQL WorkBench](https://dev.mysql.com/downloads/workbench/)

## Installation
### Setup
**Step 1:** Clone this repository\
**Step 2:** Go to cloned repository directory\
**Step 3** Create MySQL Database, User schema locally
```
-- Create SQL Server Database for local buyani or use the current buyani database
CREATE DATABASE buyani CHARACTER SET utf8 COLLATE utf8_unicode_ci;

-- Use the database
USE  buyani`;

DROP USER 'server_user_local_login'@'localhost';

-- Create MySQL User
CREATE USER 'server_user_local_login'@'localhost' IDENTIFIED BY '!Password1';

-- Grant privileges to the user
GRANT ALL PRIVILEGES ON  buyani`.* TO 'server_user_local_login'@'localhost';

```
**Step 4:** Create the local .env file
```
BASE_PATH=/api
DB_URL=jdbc:mysql://localhost:3306 buyani?allowPublicKeyRetrieval=true&useSSL=false
DB_USER=root
DB_PASSWORD=
DB_DRIVER=com.mysql.cj.jdbc.Driver
SHOW_SQL=true
OPEN_IN_VIEW=false

UI_SERVICE_HOST=http://localhost:5173
ALLOWED_ORIGINS=http://localhost:5173,http://localhost:3000
JWT_EXPIRATION_MS=3600000
JWT_REFRESH_EXP_MS=86400000

MAIL_SERVER_HOST = smtp.gmail.com
MAIL_SERVER_PORT = 587
MAIL_SERVER_PROTOCOL = smtp
MAIL_SERVER_USERNAME = cocoon.1718@gmail.com
MAIL_SERVER_PASSWORD = 
MAIL_SERVER_FROM      = no-reply buyani.com
MAIL_SERVER_NAME      = BuyAni

BASE_URL=http://localhost:5173
# BASE_URL=<CHANGE IT TO HOSTNAME>

STORAGE_URL = 
STORAGE_PATH_URL =
STORAGE_ACCESS_KEY = 
STORAGE_SECRET_KEY = 

```
**Step 4:** Export environment variables using `export $(cat .env | tr -d ' ' | grep -v "#" | xargs)`\
**Step 5:** Clean and install the build artifact into the local repository with `mvn clean install`

## Build and Run
**Step 1:** Run development server locally with `mvn spring-boot:run`
=======
# backend
my backend code 
>>>>>>> 908d941f89a6f275872c5854115eb262f7ef2197
