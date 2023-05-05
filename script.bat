javac -d ./TestFramework/WEB-INF/classes ./TestFramework/src/etu2061/framework/*.java
cd ./TestFramework
jar cvf ../test.war *
cd ..
copy test.war "C:\Program Files\Apache Software Foundation\Tomcat 10.0\webapps"
start http://localhost:8080/test