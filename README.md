# WP Command Center

## Project Overview

WP Command Center is a comprehensive dashboard application for managing multiple WordPress websites. It provides centralized control over WordPress installations, plugin management, site monitoring, and maintenance tasks. The application is built using a microservices architecture and is designed to work with WordPress sites hosted on AWS Lightsail.

## Architecture

The application consists of the following microservices:

1. Authentication Service (Port: 4000)
2. Space Management Service (Port: 4001)
3. WordPress Site Management Service (Port: 4002)
4. AWS Lightsail Integration Service (Port: 4003)
5. Plugin Management Service (Port: 4004)
6. Monitoring Service (Port: 4005)
7. Maintenance Task Service (Port: 4006)
8. Logging Service (Port: 4007)

Frontend:

- React-based single-page application

## Technology Stack

- Backend:
  - Java 21
  - Spring Boot 3.3.2
  - Spring Data JPA
  - Spring Security
  - AWS SDK for Java
  - Docker
- Frontend:
  - React
- Database:
  - AWS RDS (MySQL)
- Authentication:
  - Firebase Authentication
- Cloud Infrastructure:
  - AWS Lightsail
  - AWS S3
  - AWS RDS
- Logging and Monitoring:
  - Elasticsearch
  - Spring Boot Actuator
  - Micrometer

## Setup and Installation

1. Clone the repository:

   ```
   git clone https://github.com/your-username/wp-command-center.git
   cd wp-command-center
   ```

2. Set up environment variables:

   - Create a `.env` file in the root directory
   - Add necessary environment variables (database credentials, AWS credentials, etc.)

3. Build the project:

   ```
   ./mvnw clean package -DskipTests
   ```

4. Run the services:
   ```
   docker-compose up -d
   ```

## Development

- Backend development is done using IntelliJ IDEA CE
- Frontend development is done using Visual Studio Code
- Ensure you have Java 21 and Node.js installed on your development machine

## API Documentation

API documentation for each service can be found in their respective directories.

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct and the process for submitting pull requests.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

## Contact

For any queries, please reach out to [fernando.zambone@gmail.com](mailto:fernando.zambone@gmail.com).
