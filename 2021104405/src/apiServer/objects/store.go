package objects

import (
	"io"
	"net/http"
	"../heartbeat"
	"fmt"
	"lib/objectstream"
)

func storeObject(r io.Reader,object string) (int,error){
	stream,e:=putStream(object)
	if e!=nil{
		return http.StatusServiceUnavailable,e
	}
	io.Copy(stream,r)
	e.stream.Close()
	if e!=nil{
		return http.StatusInternalServerError,e
	}
	return http.StatusOK,nil
}

func putStream(object string) (*objectstream.PutStream,error){
	server:=heartbeat.ChooseRandomDataServer()
	if server==""{
		return nil,fmt.Errorf("cannot find any dataServer")
	}
	return objectstream.NewPutStream(server,object),nil
}