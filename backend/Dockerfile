# Backend Dockerfile (backend/Dockerfile)

# Development stage
FROM node:20.16-alpine as development

# Set working directory
WORKDIR /app

# Install dependencies
COPY package*.json ./
RUN npm install

# Copy source files
COPY . .

# Start the server with hot-reloading using nodemon
CMD ["npx", "nodemon", "src/index.ts"]

# Expose the new port for development
EXPOSE 4000

# Production stage
FROM node:20.16-alpine as production

# Set working directory
WORKDIR /app

# Install only production dependencies
COPY package*.json ./
RUN npm install --production

# Copy source files
COPY . .

# Compile TypeScript to JavaScript
RUN npx tsc

# Start the server
CMD ["node", "dist/index.js"]

# Expose the new port for production
EXPOSE 4000