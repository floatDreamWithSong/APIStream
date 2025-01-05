# Release v1.1.1 - APIStream Project

## Overview
APIStream project aims to provide a simplified and self-deployable solution for JavaScript developers. It addresses common challenges such as complex dependency management, cumbersome deployment processes, and the need for a flexible and cost-effective cloud platform.

## Key Features
- **Self-Deployable Backend Tools**: A backend tool that can be deployed on personal servers, acting as a runtime environment and management center. It handles the execution of compiled JavaScript scripts, data collection, and service monitoring.
- **Lightweight npm Development Toolkit**: The toolkit, with a compressed size of approximately 13Kb, simplifies the development process by allowing developers to write JavaScript modules for cloud execution and communicate with the backend tool.
- **Seamless Deployment and Monitoring**: The project supports local and cloud debugging, enabling quick deployment with simple commands and automatic data collection and monitoring.

## Architecture
The project is structured into four main layers to ensure maintainability and scalability:
- **Application Layer**: Utilizes Express and Axios in a lightweight npm tool (apistream-sdk).
- **Gateway Layer**: Manages permissions, with the flexibility to use Nginx or other gateway frameworks for traffic control.
- **Service Layer**: Built on Spring Boot, it executes user-uploaded JavaScript scripts using GraalVM JS and provides SQL execution interfaces by injecting specific Java classes into the JS context.
- **Infrastructure Layer**: Integrates MySQL, Minio, and MyBatis-Plus for storage, persistence, and large file management.

## Modules
- **apistream-sdk (npm Tool)**: Facilitates quick configuration and writing of cloud function modules, generating code for interaction with cloud services. It simplifies deployment and integrates management, logging, and parameter handling for cloud functions.
- **Java Backend (Apistream)**: Provides a robust backend service that supports the execution and management of cloud modules, with features like module activation, deactivation, and log querying.

## Usage
- **Installation**: Developers can install the APIStream SDK using npm and utilize various command-line tools for project initialization, deployment, and management.
- **Configuration**: A simple configuration file is provided, which can be customized to fit specific project needs.
- **Deployment**: Cloud modules can be deployed with a single command, generating local functions for interacting with the deployed modules.
- **Monitoring**: A user-friendly UI interface is available for monitoring and managing deployed cloud modules.

## System Requirements
- **JDK**: JDK 17 or higher
- **Database**: MySQL 8.3
- **Object Storage**: Minio OSS
