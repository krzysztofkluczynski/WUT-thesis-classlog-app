FROM node:alpine AS build

WORKDIR /project

RUN npm install -g @angular/cli

COPY package.json package-lock.json ./
RUN npm ci

COPY . .

ARG BACKEND_URL
RUN sed -i "s|BACKEND_URL_PLACEHOLDER|${BACKEND_URL}|g" src/environment/environment.prod.ts

RUN ng build --configuration production

EXPOSE 4200

CMD ["ng", "serve", "--host", "0.0.0.0", "--configuration", "production"]
