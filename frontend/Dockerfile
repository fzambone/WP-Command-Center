# Frontend Dockerfile (frontend/Dockerfile)

# Development stage
FROM node:20.16-alpine as development

# Set working directory
WORKDIR /app

# Install dependencies
COPY package*.json ./
RUN npm install

# Copy source files
COPY . .

# Start development server with hot-reloading
CMD ["npm", "run", "start"]

# Production stage
FROM node:20.16-alpine as production

# Set working directory
WORKDIR /app

# Install dependencies
COPY package*.json ./
RUN npm install --production

# Copy source files
COPY . .

# Build the app
RUN npm run build

# Install serve to serve the built app
RUN npm install -g serve

# Serve the built app
CMD ["serve", "-s", "build", "-l", "3000"]

# Expose the production port
EXPOSE 3000