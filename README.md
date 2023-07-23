# Agenda Craft Application Readme

## Introduction
Agenda Craft is a simple yet powerful application built using Spring Boot, JPA (Java Persistence API), Hibernate, and PostgreSQL. The application allows users to manage their schedules, appointments, and tasks efficiently. This README provides essential information on setting up and running the Agenda Craft application.

## Features
- User Registration and Authentication: Users can create an account and log in securely to access their agendas.
- Agenda Management: Users can create, view, update, and delete multiple agendas.
- Appointment Management: Users can schedule appointments, set reminders, and manage their appointments.
- Task Tracking: Users can create tasks, mark them as completed, and organize tasks within agendas.
- Data Persistence: The application uses PostgreSQL database through JPA and Hibernate for data storage.

## Prerequisites
Before running the Agenda Craft application, ensure you have the following installed on your system:
- Java Development Kit (JDK) 11 or higher
- Maven
- PostgreSQL database (with username, password, and database name ready)

## Getting Started
1. Clone the Agenda Craft repository from GitHub
2. Open the `src/main/resources/application.properties` file and configure the PostgreSQL database connection details:

application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
spring.datasource.username=your_database_username
spring.datasource.password=your_database_password


3. Build the application using Maven:
  mvn clean package

5. Run the application:
  java -jar target/agenda-craft-0.1.0.jar
