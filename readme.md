# ClassLog Application

ClassLog is an application designed specifically for language schools to enhance course organization and support language learning. Built with a robust 3-tier architecture, which combines simplicity with scalability.

## Technologies

- **Backend**: Spring Boot
- **Frontend**: Angular
- **Database**: PostgreSQL

## Project Structure

- `classlog-backend/` - Backend implementation of the application.
- `classlog-frontend/` - Frontend implementation of the application.
- `db_scripts/` - Database management scripts.
- `deployment/` - Configuration files for deploying the application.

## Running the Project

### Requirements

- **Docker**
- **Docker Compose**

### Step 1: Clone the Repository

Clone the repository to your local machine:

```sh
git clone <REPOSITORY_URL>
cd <REPOSITORY_NAME>
```

### Step 2: Configure the Environment

Adjust the .env file as needed. By default, it is preconfigured for local development. You only need to provide credentials for the first user in the system.

### Step 3: Build and Run the Containers

#### Navigate to the Deployment Directory

```sh
cd deployment
```

#### Run the Entire Application

To build and run the application with all its components:

```sh
docker-compose up --build
```

#### Run a Specific Component

If you want to run a specific component of the project (e.g., only the database), use this command:

```sh
docker compose -f .\docker-compose-db.yml up
```
