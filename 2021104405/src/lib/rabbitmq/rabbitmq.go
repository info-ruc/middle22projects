package rabbitmq

import (
	"encoding/json"
	"github.com/streadway/amqp"
)

type RabbitMQ struct{
	channel *amqp.Channel
	conn *amqp.Connection
	Name string
	exchange string
}

func New(s string) *RabbitMQ{
	conn,e:=amqp.Dial(s)//create connection s:address
	if e!=nil{
		panic(e)
	}

	ch,e:=conn.Channel()//create channel
	if e!=nil{
		panic(e)
	}

	q,e:=ch.QueueDeclare(//create queue
		"",//name
		false,//durable
		true,//auto delete when unused
		false,//exclusive
		false,//whether wait for sercer ensure
		nil,//arguments
	)
	if e!=nil{
		panic(e)
	}

	mq:=new(RabbitMQ)
	mq.channel=ch
	mq.conn=conn
	mq.Name=q.Name
	return mq
}

//bind exchange with queue,so all the message send to exchange can be found in the queue
func (q *RabbitMQ) Bind(exchange string){
	e:=q.channel.QueueBind(//bind exchange and queue
		q.Name,//name of queue
		"",//routing key
		exchange,//name of exchange
		false,//whether wait for server ensure
		nil)//arguments
	if e!=nil{
		panic(e)
	}
	q.exchange=exchange
}

//send message to queue
func (q *RabbitMQ) Send(queue string,body interface{}){
	str,e:=json.Marshal(body)
	if e!=nil{
		panic(e)
	}
	e=q.channel.Publish(
		"",//name of exchange
		queue,//key routerKey
		false,//return when unhandled
		false,//return when none consume
		amqp.Publishing{//msg body
			ReplyTo:q.Name,
			Body:[]byte(str),
		})
	if e!=nil{
		panic(e)
	}
}

//send message to exchange
func (q *RabbitMQ) Publish(exchange string,body interface{}){
	str,e:=json.Marshal(body)
	if e!=nil{
		panic(e)
	}
	e=q.channel.Publish(
		exchange,
		"",
		false,
		false,
		amqp.Publishing{
			ReplyTo:q.Name,
			Body:[]byte(str),
		})
	if e!=nil{
		panic(e)
	}
}

func (q *RabbitMQ) Consume() <-chan amqp.Delivery{
	c,e:=q.channel.Consume(
		q.Name,//name of queue
		"",//name of consumer
		true,//ack
		false,//exclusive
		false,//no local
		false,//no wait
		nil,//arguments
	)
		if e != nil {
		panic(e)
	}
	return c
}

func (q *RabbitMQ) CLose(){
	q.channel.Close()
	q.conn.Close()
}