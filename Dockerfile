FROM java:8
EXPOSE 8080
CMD java -jar /test-status.jar
ADD ./build/libs/test-status.jar test-status-cloud-docker.jar