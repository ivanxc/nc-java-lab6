FROM openjdk:11
EXPOSE 8080
WORKDIR /app
COPY ./target/book-shop-0.0.1-SNAPSHOT.jar .
CMD java -jar book-shop-0.0.1-SNAPSHOT.jar