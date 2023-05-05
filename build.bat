javac -d ./Framework/WEB-INF/classes ./Framework/src/etu2061/framework/*.java
jar cvf fw.jar ./Framework/WEB-INF/classes/etu2061
copy fw.jar TestFramework\WEB-INF\lib
