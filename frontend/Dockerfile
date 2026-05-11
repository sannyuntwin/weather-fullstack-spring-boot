# Use Node.js 18 Alpine as the base image
FROM node:18-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json first
# This takes advantage of Docker's layer caching
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code
COPY . .

# Build the React application
RUN npm run build

# Use Nginx to serve the built React application
FROM nginx:alpine

# Copy the built React application from the previous stage
COPY --from=0 /app/dist /usr/share/nginx/html

# Copy Nginx configuration to handle client-side routing
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80 for the web server
EXPOSE 80

# Start Nginx server
CMD ["nginx", "-g", "daemon off;"]
