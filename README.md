**E-Commerce Backend Platform Build with Microservices**

Overview
This repository houses an E-Commerce application developed using Java and Spring Boot, following a microservices architecture. The primary focus of this project is to provide a scalable and modular solution for building and managing various aspects of an online retail system.

**High Level Architecture**

![image](https://github.com/AllanT102/ecommerce-platform/assets/92118801/97b664ae-b09a-4075-86a7-fafb6642ded1)

**Technologies**

Java, Spring Boot, MongoDB, MySQL, Apache Kafka, Docker, Elasticsearch, Kibana, Logstash

**Services**

**api-gateway-service**: Serves as the entry point for external clients, routing requests to appropriate microservices.
**configuration-server**: Manages configuration settings for all microservices, ensuring centralized and dynamic configuration management.
**discovery-server**: Implements service discovery for the microservices, enabling automatic detection and registration of services within the ecosystem.
**inventory-service**: Handles inventory management, tracking product availability and stock levels.
**notification-service**: Utilizes Kafka as a messaging system to implement notifications, ensuring real-time communication with users.
**order-service**: Manages the order processing workflow, handling customer orders and coordinating with other services.
**product-service**: Manages product-related information, including details, pricing, and availability.

**ELK Stack Observability**
The project incorporates the ELK (Elasticsearch, Logstash, Kibana) stack for observability and monitoring. This setup enhances the project's ability to track and analyze log data, providing insights into system behavior and aiding in troubleshooting and performance optimization.

Key Observability Features:

Elasticsearch: Centralized storage for logs, enabling efficient search and retrieval of relevant information.
Logstash: Configured to parse and forward log data to Elasticsearch, ensuring proper indexing and structuring of logs.
Kibana: Empowers users to visualize and explore log data through interactive dashboards, facilitating effective monitoring and analysis.
