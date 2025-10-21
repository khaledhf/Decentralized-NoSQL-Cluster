FROM openjdk:20
COPY target/WorkerNode.jar WorkerNode.jar
COPY Database Database
COPY Users Users
ENV port 1500
ENV nodeId 1
CMD echo $port
CMD echo $nodeId
CMD java -jar WorkerNode.jar $port $nodeId
