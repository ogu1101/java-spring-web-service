# OSは、Amazon Linux 2
FROM tomcat:9.0.38-jdk11-corretto

COPY target/ROOT.war webapps/
