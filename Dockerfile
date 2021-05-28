FROM tomcat:8
COPY target/demo.war /usr/local/tomcat8/webapps/gestparc.war
ENTRYPOINT ["/bin/bash", "/usr/local/tomcat8/bin/catalina.sh", "run"]
