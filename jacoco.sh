# this shell script is for Test Coverage on SonarQube
mvn clean \
org.jacoco:jacoco-maven-plugin:prepare-agent \
test \
integration-test \
verify \
org.jacoco:jacoco-maven-plugin:report
