package locate

import(
    "encoding/json"
	"net/http"
	"strings"
)

func Handler(w http.ResponseWriter,r *http.Request){
	m:=r.Method
	if m!=http.MethodGet{
		w.WriteHeader(http.StatusMethodNotAllowed)
		return
	}
	info:=Locate(strings.Split(r.URL.EscapedPath(),"/")[2])//locate(<name of object>),and get info of the target object
	if len(info)==0{
		w.WriteHeader(http.StatusNotFound)//not found the object
		return
	}
	b,_:=json.Marshal(info)//marsh the object to slice of byte,then response to request 
	w.Write(b)
}