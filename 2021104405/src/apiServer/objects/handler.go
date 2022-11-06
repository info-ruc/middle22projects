package objects

import (
	"net/http"
	"strings"
	"../../lib/es"
	"log"
)

func Handler(w http.ResponseWriter,r *http.Request){
	m:=r.Method
	if m==http.MethodGet{
		get(w,r)
		return
	}else if m==http.MethodPut{
		put(w,r)
		return
	}else if m==http.MethodDelete{
		del(w,r)
		return
	}
	w.WriteHeader(http.StatusMethodNotAllowed)//write http error status
}

func del(w http.ResponseWriter,r *http.Request){
	name:=strings.Split(r.URL.EscapedPath(),"/")[2]
	version,e:=es.SearchLatestVersion(name)//get the lastest version
	if e!=nil{
		log.Println(e)
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
	e=es.PutMetaData(name,version.Version+1,0,"")//size is 0 and hash is empty which means it's a delete flag
	if e!=nil{
		log.Println(e)
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
}