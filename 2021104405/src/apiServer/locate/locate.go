package locate

import (
	"../../lib/rabbitmq"
	"os"
	"strconv"
	"time"
)

func Locate(name string) string{
	q:=rabbitmq.New(os.Getenv("RABBITMQ_SERVER"))//temp mq,just for locate
	q.Publish("dataServers",name)//send the name of target object to data server,then all the queue bind dataServers will receive this
	c:=q.Consume()//channel
	go func(){
		time.Sleep(time.Second)
		q.Close()//wait for one second then close the rabbitmq
	}()
	msg:=<-c
	s,_:=strconv.Unquote(string(msg.Body))
	return s
}

func Exist(name string) bool{
	return Locate(name)!=""
}