package objects

import(
	"net/http"
	"net/url"
	"../locate"
	"fmt"
	"io"
	"../../lib/objectstream"
	"strconv"
	"log"
	"../../lib/es"
	"strings"
)

func get(w http.ResponseWriter,r *http.Request){
	name:=strings.Split(r.URL.EscapedPath(),"/")[2]//get name
	versionId:=r.URL.Query()["version"]//query return parameters in map,so we can find version from key of version
	version:=0
	var e error
	if len(versionId)!=0 {
		version,e=strconv.Atoi(versionId[0])//get version from the first element
		if e!=nil{
			log.Println(e)
			w.WriteHeader(http.StatusBadRequest)
			return
		}
	}
	meta,e:=es.GetMetaData(name,version)//get meta data by name and version
	if e!=nil{
		log.Println(e)
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
	if meta.Hash==""{//means this is a deleted element
		w.WriteHeader(http.StatusNotFound)
		return
	}
	object:=url.PathEscape(meta.Hash)//hash as the name of target object which can be searched
	stream,e:=getStream(object)//fiel stream for read
	if e!=nil{
		log.Println(e)
		w.WriteHeader(http.StatusNotFound)
		return
	}
	io.Copy(w,stream)//copy to the response
}

func getStream(object string)(io.Reader,error){
	server:=locate.Locate(object)//locate the address of server which store the object
	if server=="" {
		return nil,fmt.Errorf("object %s locate fail",object)
	}
	return objectstream.NewGetStream(server,object)
}