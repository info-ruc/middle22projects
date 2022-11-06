#!/bin/bash
for i in `seq 1 6`;do mkdir -p ../src/tmp/$i/objects;done
export RABBITMQ_SERVER=amqp://test:test@localhost:5672
export ES_SERVER=localhost:9200
LISTEN_ADDRESS=192.168.1.1:12345 STORAGE_ROOT=../src/tmp/1 go run ../src/dataServer/dataServer.go &
LISTEN_ADDRESS=192.168.1.2:12345 STORAGE_ROOT=../src/tmp/2 go run ../src/dataServer/dataServer.go &
LISTEN_ADDRESS=192.168.1.3:12345 STORAGE_ROOT=../src/tmp/3 go run ../src/dataServer/dataServer.go &
LISTEN_ADDRESS=192.168.1.4:12345 STORAGE_ROOT=../src/tmp/4 go run ../src/dataServer/dataServer.go &
LISTEN_ADDRESS=192.168.1.5:12345 STORAGE_ROOT=../src/tmp/5 go run ../src/dataServer/dataServer.go &
LISTEN_ADDRESS=192.168.1.6:12345 STORAGE_ROOT=../src/tmp/6 go run ../src/dataServer/dataServer.go &
LISTEN_ADDRESS=192.168.2.1:12345 go run ../src/apiServer/apiServer.go &