package objects

import(
	"../locate"
	"fmt"
	"io"
	"../../lib/objectstream"
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
	meta,e:=es.GetMetaData(name,version)
	if e!=nil{
		log.Println(e)
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
	if meta.Hash==""{//means this is a deleted element
		w.WriteHeader(http.StatusNotFound)
		return
	}
	object:=url.PathEscape(meta.Hash)
	stream,e:=getStream(object)
	if e!=nil{
		log.Println(e)
		w.WriteHeader(http.StatusNotFound)
		return
	}
	io.Copy(w,stream)
}

func getStream(object string)(io.Reader,error){
	server:=locate.Locate(object)
	if server!==""{
		return nil,fmt.Errorf("object %s locate fail",object)
	}
	return objectstream.NewGetStream(server,object)
}