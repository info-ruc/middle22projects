package main

import (
	"log"
	"net/http"
	"os"

	"./objects"
)

func main() {
	http.HandleFunc("/objects/", objects.Handler)
	log.Fatal(http.ListenAndServe(os.Getenv("LISTEN_ADDRESS"), nil)) //get from linux system env config,handler for interface
}
