package locate

import(
	"../../lib/rabbitmq"
	"os"
	"strconv"
)

func Locate(name string) bool{
	_,err:=os.Stat(name)//os.Stat get status of target file
	return !os.IsNotExist(err)//是否已知错误以报告文件或目录不存在
}

func StartLocate(){
	q:=rabbitmq.New(os.Getenv("RABBITMQ_SERVER"))
	defer q.Close()
	q.Bind("dataServers")//receive all message from dataServer
	c:=q.Consume()//channel
	for msg:=range c{
		//each message is a request of locating object
		o,e:=strconv.Unquote(string(msg.Body))//get object name need to be located
		if e!=nil{
			panic(e)
		}
		if(Locate(os.Getenv("STORAGE_ROOT")+"/objects/"+o)){//get address
			q.Send(msg.ReplyTo,os.Getenv("LISTEN_ADDRESS"))//single send to api server node
		}
	}
}