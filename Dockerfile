FROM openjdk:8
COPY . /home/app
WORKDIR /home/app
RUN ["./gradlew", "build"]
ENTRYPOINT ["./gradlew", "run"]
