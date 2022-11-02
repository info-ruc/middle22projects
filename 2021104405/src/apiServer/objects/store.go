package objects

import (
	"io"
	"net/http"
	"../heartbeat"
	"fmt"
	"../../lib/objectstream"
	"../../lib/es"
	"../../lib/utils"
	"log"
	"net/url"
	"strings"
)

func put(w http.ResponseWriter,r *http.Request){
	hash:=utils.GetHashFromHeader(r.Header)
	if hash==""{
		log.Println("missing object hash digest header")
		w.WriteHeader(http.StatusBadRequest)
		return
	}
	c,e:=storeObject(r.Body,url.PathEscape(hash))
	if e != nil {
		log.Println(e)
		w.WriteHeader(c)
		return
	}
	if c != http.StatusOK {
		w.WriteHeader(c)
		return
	}

	name:=string.Split(r.URL.EscapedPath(),"/")[2]
	size := utils.GetSizeFromHeader(r.Header)
	e=es.AddVersion(name,hash,size)
	if e!=nil{
		log.Fatal(e)
		w.WriteHeader(http.StatusInternalServerError)
	}
}

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