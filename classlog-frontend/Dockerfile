FROM node:alpine AS build

# Set up working directory
WORKDIR /project

# Install Angular CLI globally
RUN npm install -g @angular/cli

# Copy package files and install dependencies
COPY package.json package-lock.json ./
RUN npm ci

# Copy the rest of the application files
COPY . .

# Expose the development server port
EXPOSE 4200

# Start the Angular development server
CMD ["ng", "serve", "--host", "0.0.0.0"]
