# Business Process Management System (BPMS)

## Overview

The Business Process Management System (BPMS) is a backend application built using Java and Spring Boot. It helps manage and track business processes step by step.

The system allows us to define processes, create steps, assign tasks to users, and record their actions. It also includes features for managing users, roles, notifications, and audit logs.

The project is being developed step by step. Our goal is to make the system handle a complete workflow, from starting a process to completing or rejecting it.

## Features

- **User Management:** Manage users and their information.
- **Role Management:** Manage user roles and responsibilities.
- **Process Definitions:** Create and manage process definitions.
- **Process Steps:** Define the steps of a business process.
- **Process Tracking:** Store and track process instances.
- **Task Management:** Manage tasks linked to process steps.
- **Task Actions:** Record actions performed on tasks.
- **Notifications:** Store notifications for users.
- **Audit Logs:** Record important system activities.
- **REST APIs:** Allow the application to communicate through HTTP requests and responses.


## Technology Stack

- **Java:** Main programming language used for the backend.
- **Spring Boot:** Used to build the backend application and REST APIs.
- **Spring Data JPA:** Used to work with database entities and data.
- **MySQL:** Used to store project data.
- **Maven:** Used to manage project dependencies and build the application.
- **Postman:** Used to test REST APIs.
- **Git and GitHub:** Used to manage code versions and store the project repository.

## How the Workflow Works

The idea of BPMS is to move a business process through different steps. A process definition describes the steps and their order.

When a user starts a process, the system creates a process instance to track that execution. The next goal is to create a task for the first step and assign it to an active user with the required role.

When a user approves a task, the process should move to the next step. If a task is rejected, the process should end as rejected. If all steps are approved, the process should be marked as completed.


## Database Design

The project uses MySQL to store data related to users, roles, processes, tasks, and other system activities.

The database contains separate tables for different parts of the system. These tables are connected using relationships and foreign keys. For example, a process instance is linked to its process definition, and tasks are linked to process instances and process steps.

This design helps keep the data organized and makes it easier to manage and track processes.


## Installation and Setup

### Requirements

- Java JDK 21
- MySQL
- Maven (or the Maven Wrapper included in the project)
- Git

### Steps to Run the Project

1. Clone the repository from GitHub.
2. Create a MySQL database named `bpms`.
3. Configure the database connection in `application.properties`.
4. Set the `DB_PASSWORD` environment variable to your MySQL password.
5. Run the application using the Maven Wrapper.

After the application starts, you can use Postman to test the REST APIs.


## API Testing

The REST APIs are tested using Postman. It helps us send requests to the backend and check the responses returned by the application.



## Future Improvements

- Automate task creation and assignment.
- Move the process to the next step after approval.
- Handle process rejection and completion.
- Improve task authorization and workflow security.
