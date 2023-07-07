javac -d ./Framework/WEB-INF/classes ./Framework/src/etu2061/framework/*.java
@REM javac -d ./Framework/WEB-INF/classes ./Framework/src/etu2061/framework/Mapping.java
@REM javac -d ./Framework/WEB-INF/classes ./Framework/src/etu2061/framework/FileUpload.java
@REM javac -d ./Framework/WEB-INF/classes ./Framework/src/etu2061/framework/Param.java
@REM javac -d ./Framework/WEB-INF/classes ./Framework/src/etu2061/framework/UrlAnnotation.java
@REM javac -d ./Framework/WEB-INF/classes ./Framework/src/etu2061/framework/Param.java
@REM javac -d ./Framework/WEB-INF/classes ./Framework/src/etu2061/framework/Scope.java
@REM javac -d ./Framework/WEB-INF/classes ./Framework/src/etu2061/framework/ModelView.java
@REM javac -d ./Framework/WEB-INF/classes ./Framework/src/etu2061/framework/FileUpload.java
@REM javac -d ./Framework/WEB-INF/classes ./Framework/src/etu2061/framework/FrontServlet.java

cd ./Framework/WEB-INF/classes
jar cvf ../../../fw.jar ./etu2061
cd ../../..
copy fw.jar TestFramework\WEB-INF\lib

set CLASSPATH=%CLASSPATH%;%cd%\fw.jar
javac -classpath "fw.jar;%CLASSPATH%" -d ./TestFramework/WEB-INF/classes ./TestFramework/src/etu2061/framework/*.java   
@REM javac -cp ./fw.jar -d ./TestFramework/WEB-INF/classes ./TestFramework/src/etu2061/framework/*.java
javac -d ./TestFramework/WEB-INF/classes ./TestFramework/src/etu2061/framework/Emp.java
javac -d ./TestFramework/WEB-INF/classes ./TestFramework/src/etu2061/framework/Dept.java
cd ./TestFramework
jar cvf ../test.war *
cd ..
copy test.war "C:\Program Files\Apache Software Foundation\Tomcat 10.0\webapps"
start http://localhost:8080/test