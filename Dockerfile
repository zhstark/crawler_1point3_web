FROM tomcat:9.0.41

#WORKDIR /code
#
#COPY src src/
#COPY lib lib/
#COPY pom.xml ./
#COPY web2.iml ./
#COPY images images/
#
#RUN jar -cvfM0 web.war ./ && mv web.war /usr/local/tomcat/webapps/web.war

WORKDIR /src

COPY out/artifacts/web2_war2/web2_war.war /usr/local/tomcat/webapps/web_war.war

