# WP-Command-Center

WP-Command-Center is a centralized control panel designed for managing and maintaining 50+ WordPress sites hosted on AWS Lightsail. This tool enables efficient site management with features like one-click updates, rollback functionality, user management, notifications, and basic logging.

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running Locally](#running-locally)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Features

- **One-Click Updates**: Easily update WordPress core, themes, and plugins across all sites.
- **Rollback Functionality**: Revert to previous versions if something goes wrong.
- **User Management**: Manage access with Firebase Authentication.
- **Notifications**: Get notified via Slack and Email for important updates.
- **Basic Logging**: Track essential events and errors.
- **SSL/TLS Encryption**: Secure communication between services.

## Tech Stack

- **Frontend**: React (TypeScript)
- **Backend**: Node.js with Express.js
- **Database**: MongoDB via AWS DocumentDB
- **Authentication**: Firebase Authentication
- **Task Scheduler**: RabbitMQ via AWS MQ
- **Deployment**: Docker for local development, AWS Elastic Beanstalk for production

## Getting Started

### Prerequisites

- **Docker** and **Docker Compose** installed
- **Node.js** (v18 or later)
- **npm** (v9 or later)
- **AWS Account** (for Lightsail, DocumentDB, and Elastic Beanstalk)
- **Firebase Account** (for Authentication)

### Installation

1. **Clone the repository**:

   ```bash
   git clone https://github.com/your-username/WP-Command-Center.git
   cd WP-Command-Center
   ```

2. **Install dependencies for both frontend and backend**:
   ```bash
   cd backend
   npm install
   cd frontend
   npm install
   ```

### Running Locally

1. **Start Docker containers**:

   ```bash
   docker-compose up --build
   ```

2. **Access Application**:

- **Backend**: http://localhost:3000
- **Frontend**: http://localhost:5000

### Usage

- **Dashboard**: View and manage all WordPress websites.
- **Updates**: Trigger manual updates to WordPress core, themes and plugins.
- **User Management**: Manage users and roles with Firebase Authentication.
- **Notifications**: Configure Slack and Email for notifications.

### Contributing

For questions or suggestions, please contact fernando.zambone@gmail.com.
