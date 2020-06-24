A)
docker run -d --name mysql-container -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=photoappdb -e MYSQL_USER=XXXX -e MYSQL_PASSWORD=XXX -v /var/lib/mysql:/var/lib/mysql mysql:latest

To execute sql command on above mysql databsase
docker exec -it d6ff7f5e3046 bash
mysql -u XXXX -p


B)

docker build --tag=usersmicroservice --force-rm=true .
docker tag 140a616b5d5f amsidhlokhande/usersmicroservice
docker push amsidhlokhande/usersmicroservice

docker run -d --name usersmicroservice -e "spring.cloud.config.uri=http://172.31.4.122:8012" --network host -v /home/ec2-user/photoapp-logs:/api-logs amsidhlokhande/usersmicroservice


Here --network host is used to enable docker to support random port
-v is for mapping docker container's inside log /api-logs  to outside 
