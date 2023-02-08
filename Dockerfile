FROM maven:3.6-alpine as DEPS

WORKDIR /opt/app
COPY service-registry/pom.xml service-registry/pom.xml
COPY security/pom.xml security/pom.xml
COPY api-gateway/pom.xml api-gateway/pom.xml

COPY pom.xml .
RUN mvn -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline

FROM maven:3.6.1-jdk-11 as BUILDER
WORKDIR /opt/app
COPY --from=deps /root/.m2 /root/.m2
COPY --from=deps /opt/app/ /opt/app
COPY service-registry/src /opt/app/security-registry/src
COPY security/src /opt/app/security/src
COPY api-gateway/src /opt/app/api-gateway/src

RUN mvn clean install -DskipTests=true -X

FROM openjdk:11
WORKDIR /opt/app
RUN echo $(ls)
COPY --from=builder /opt/app/api-gateway/target/api-gateway-1.0-SNAPSHOT.jar .
COPY --from=builder /opt/app/service-registry/target/service-registry-1.0-SNAPSHOT.jar .
COPY --from=builder /opt/app/security/target/security-1.0-SNAPSHOT.jar .
EXPOSE 8080
CMD [ "java", "-jar", "/opt/app/service-registry-1.0-SNAPSHOT.jar" ]
CMD [ "java", "-jar", "/opt/app/api-gateway-1.0-SNAPSHOT.jar" ]
CMD [ "java", "-jar", "/opt/app/security-1.0-SNAPSHOT.jar" ]