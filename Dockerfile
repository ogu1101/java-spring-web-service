FROM maven:3.8.7-amazoncorretto-11 AS builder
WORKDIR /work
COPY src/ ./src/
COPY pom.xml ./
RUN mvn -f pom.xml -P production compile \
    && mvn -f pom.xml -P production package -DskipTests=true

FROM tomcat:9.0.71-jdk11-corretto-al2
COPY --from=builder /work/target/ROOT.war webapps/
