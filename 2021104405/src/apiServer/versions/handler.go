package versions

import (
	"encoding/json"
	"../../lib/es"
	"log"
	"net/http"
	"strings"
)

func Handler(w http.ResponseWriter,r *http.Request){
	m:=r.Method
	if m!=http.MethodGet{
		w.WriteHeader(http.StatusMethodNotAllowed)
		return
	}
	from:=0
	size:=1000
	name:=strings.Split(r.URL.EscapedPath(),"/")[2]
	//page from from with size of size
	for {
		metas,e:=es.SearchAllVersions(name,from,size)//metas:objects array of all versions
		if e!=nil{
			log.Println(e)
			w.WriteHeader(http.StatusInternalServerError)
			return
		}
		for i := range metas {
			b, _ := json.Marshal(metas[i])
			w.Write(b)
			w.Write([]byte("\n"))//response
		}
		if len(metas) != size {
			return//no more data
		}
		from += size//0-1000,1000-(1000+size)
	}
}