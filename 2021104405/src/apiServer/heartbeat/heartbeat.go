package heartbeat

import(
	"../../lib/rabbitmq"
	"math/rand"
	"os"
	"strconv"
	"sync"
	"time"
)

var dataServers=make(map[string]time.Time)//map:address of data servers,time of receiving heartbeat
var mutex sync.Mutex

func ListenHeartbeat(){
	q:=rabbitmq.New(os.Getenv("RABBITMQ_SERVER"))//create mq for heartbeat message from dataserver node
	defer q.Close()
	q.Bind("apiServers")//bind apiServers exchange,it can receive all the message sent to apiServers
	c:=q.Consume()//get a channel
	go removeExpiredDataServer()//remove dataServes timeout
	for msg:=range c{
		dataServer,e:=strconv.Unquote(string(msg.Body))//Unquote:"data"->data,get the address
		if e!=nil{
			panic(e)
		}
		mutex.Lock()
		dataServers[dataServer]=time.Now()
		mutex.Unlock()
	}
}

//rmove the dataServes timeout of 10s
func removeExpiredDataServer(){
	for{
		time.Sleep(5*time.Second)
		mutex.Lock()
		for s,t:=range dataServers{
			if t.Add(10*time.Second).Before(time.Now()){
				delete(dataServers,s)
			}
		}
		mutex.Unlock()
	}
}

func GetDataServers() []string{
	mutex.Lock()
	defer mutex.Unlock()
	ds:=make([]string,0)
	for s,_:=range dataServers{
		ds=append(ds,s)
	}
	return ds
}

//get address of one dataServer
func ChooseRandomDataServer() string{
	ds:=GetDataServers()
	n:=len(ds)
	if n==0{
		return ""
	}
	return ds[rand.Intn(n)]
}