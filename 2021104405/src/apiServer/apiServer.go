package main

import(
	"./heartbeat"
	"./locate"
	"./objects"
	"./versions"
	"log"
	"net/http"
	"os"
)

func main(){
	go heartbeat.ListenHeartbeat()
	http.HandleFunc("/objects/",objects.Handler)//object request
	http.HandleFunc("/locate/",locate.Handler)//locate request
	http.HandleFunc("/versions/",versions.Handler)//versios request
	log.Fatal(http.ListenAndServe(os.Getenv("LISTEN_ADDRESS"),nil))
}