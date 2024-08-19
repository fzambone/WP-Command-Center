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

# Install development dependencies globally
RUN npm install -g nodemon typescript

# Start the server with hot-reloading using nodemon
CMD ["nodemon", "--watch", "src", "--exec", "ts-node", "src/server.ts"]

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
RUN npm run build

# Start the server
CMD ["node", "dist/server.js"]

# Expose the new port for production
EXPOSE 4000