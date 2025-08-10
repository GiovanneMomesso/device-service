# 📦 Spring Boot REST API with Java 24, Gradle, and Docker

This project is a REST API built with **Java 24** using **Spring Boot** and **Gradle**, packaged to run with **PostgreSQL** via Docker.

---

## 🛠️ Prerequisites

Before running the project, make sure you have the following installed on your machine:

- **Java 24 JDK**  
  Download and install from [Oracle](https://www.oracle.com/java/technologies/javase/jdk24-archive-downloads.html) or use an OpenJDK 24 build.

- **Gradle** (Optional, if you want to build and run locally)  
  Install via [Gradle website](https://gradle.org/install/) or use the Gradle Wrapper included in the project (`./gradlew`).

- **Docker**  
  Install Docker Desktop from [docker.com](https://www.docker.com/products/docker-desktop).

- **Docker Compose**  
  Usually included with Docker Desktop. Verify by running `docker-compose --version`.

---

## 🚀 How to Run the Project

You can start the application in two ways: **full stack** (API + database).

### 1️⃣ Start Full Application (API + Database)

Runs the **PostgreSQL database** and the **Spring Boot API** together using Docker Compose:

```bash
   docker-compose -f docker-compose.full.yml up -d --build
```
### 2️⃣ Start Only Database

Runs the **PostgreSQL database** using Docker Compose:

```bash
   docker-compose -f docker-compose.db.yml up -d --build
```