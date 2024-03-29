FROM node:21 AS ng-builder

RUN npm i -g @angular/cli

WORKDIR /ngapp

COPY client/package*.json .
COPY client/angular.json .
COPY client/tsconfig.* .
COPY client/src src

RUN npm ci && ng build

FROM openjdk:21-bookworm AS sb-builder

WORKDIR /sbapp

COPY src src
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .
COPY mvnw.cmd .
COPY --from=ng-builder /ngapp/dist/client-side/browser/ src/main/resources/static

RUN ./mvnw package -Dmvn.test.skip=true

FROM openjdk:21-bookworm

COPY --from=sb-builder /sbapp/target/ecommerce-0.0.1-SNAPSHOT.jar app.jar

ENV PORT=8080
ENV SPRING_DATA_MONGODB_URI=NOT_SET 
ENV SPRING_DATASOURCE_URL=localhost 
ENV SPRING_DATASOURCE_USERNAME=default 
ENV SPRING_DATASOURCE_PASSWORD=abc123

EXPOSE $PORT

ENTRYPOINT SERVER_PORT=${PORT} java -jar ./app.jar
