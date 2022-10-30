package locate

import (
	"lib/rabbitmq"
	"os"
	"strconv"
	"time"
)

func Locate(name string) string{
	q:=rabbitmq.New("RABBITMQ_SERVER")//temp mq
	q.Publish("dataServers",name)//send message to data server
	c:=q.Consume()
	msg:=<-c
	s,_:=strconv.Unquote(string(msg.Body))
	return s
}

func Exist(name string) bool{
	return Locate(name)!=""
}