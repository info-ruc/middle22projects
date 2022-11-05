package es
//http to get es server

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
	"net/url"
	"os"
	"strings"
	"log"
)

type MetaData struct{
	Name string
	Version int
	Size int64
	Hash string
}

type hit struct{
	Source MetaData `json:"_source"`
}

type searchResult struct{
	Hits struct{
		Total int
		Hits []hit
	}
}

func getMetaData(name string,versionId int) (meta MetaData,e error){
	client:=http.Client{}
	url:=fmt.Sprintf("http://%s/metadata/_doc/%s_%d/_source",os.Getenv("ES_SERVER"),name,versionId)
	request,_:=http.NewRequest("GET",url,nil)
	request.Header.Add("Content-Type","application/json")
	response,e:=client.Do(request)
	if e != nil {
		return
	}
	if response.StatusCode != http.StatusOK {
		e = fmt.Errorf("fail to get %s_%d: %d", name, versionId, response.StatusCode)
		return
	}
	result,_:=ioutil.ReadAll(response.Body)
	json.Unmarshal(result,&meta)
	return
}

func SearchLatestVersion(name string) (meta MetaData,e error){
	client:=http.Client{}
	url:=fmt.Sprintf("http://%s/metadata/_search?q=name:%s&size=1&sort=version:desc",os.Getenv("ES_SERVER"), url.PathEscape(name))
	request,_:=http.NewRequest("GET",url,nil)
	request.Header.Add("Content-Type","application/json")
	response,e:=client.Do(request)
	if e != nil {
		return
	}
	if response.StatusCode != http.StatusOK {
		e = fmt.Errorf("fail to search latest metadata: %d", response.StatusCode)
		return
	}
	result,_:=ioutil.ReadAll(response.Body)
	var sr searchResult
	json.Unmarshal(result,&sr)
	if len(sr.Hits.Hits)!=0{
		meta=sr.Hits.Hits[0].Source
	}
	return
}

func GetMetaData(name string,version int) (MetaData,error){
	if version==0{
		return SearchLatestVersion(name)
	}
	return getMetaData(name,version)
}

func PutMetaData(name string,version int,size int64,hash string) error{
	doc:=fmt.Sprintf(`{"name":"%s","version":%d,"size":%d,"hash":"%s"}`,name,version,size,hash)
	client:=http.Client{}
	url:=fmt.Sprintf("http://%s/metadata/_doc/%s_%d?op_type=create",os.Getenv("ES_SERVER"),name,version)
	request,_:=http.NewRequest("PUT",url,strings.NewReader(doc))
	request.Header.Add("Content-Type","application/json")
	response,e:=client.Do(request)
	log.Println(url,response.StatusCode)
	if e!=nil{
		log.Println(e)
		return e
	}
	if response.StatusCode==http.StatusConflict{
		return PutMetaData(name,version+1,size,hash)
	}
	if response.StatusCode!=http.StatusCreated{
		result,_:=ioutil.ReadAll(response.Body)
		return fmt.Errorf("fail to put metadata:%d %s",response.StatusCode,string(result))
	}
	return nil
}

func AddVersion(name,hash string,size int64) error{
	version,e:=SearchLatestVersion(name)
	if e!=nil{
		return e
	}
	return PutMetaData(name,version.Version+1,size,hash)
}

func SearchAllVersions(name string,from,size int) ([]MetaData,error){
	client:=http.Client{}
	url:=fmt.Sprintf("http://%s/metadata/_search?from=%d&size=%d",os.Getenv("ES_SERVER"),from,size)
	if name!=""{
		url+="&q=name:"+name
	}
	log.Println(url)
	request,_:=http.NewRequest("GET",url,nil)
	request.Header.Add("Content-Type","application/json")
	response,e:=client.Do(request)
	if e!=nil{
		return nil,e
	}
	metas:=make([]MetaData,0)
	result,_:=ioutil.ReadAll(response.Body)
	var sr searchResult
	json.Unmarshal(result,&sr)
	log.Println(len(sr.Hits.Hits))
	for i:=range sr.Hits.Hits{
		metas=append(metas,sr.Hits.Hits[i].Source)
	}
	return metas,nil
}

func DelMetaData(name string,version int){
	client:=http.Client{}
	url:=fmt.Sprintf("http://%s/metadata/_doc/%s_%d",os.Getenv("ES_SERVER"),name,version)
	request,_:=http.NewRequest("DELETE",url,nil)
	request.Header.Add("Content-Type","application/json")
	client.Do(request)
}

type Bucket struct {
	Key string
	Doc_count int
	Min_version struct{
		Value float32
	}
}

type aggregateResult struct {
	Aggregations struct {
		Group_by_name struct {
			Buckets []Bucket
		}
	}
}

func SearchVersionStatus(min_doc_count int) ([]Bucket,error) {
	client:=http.Client{}
	url:=fmt.Sprintf("http://%s/metadata/_search",os.Getenv("ES_SERVER"))
	body:=fmt.Sprintf(`
	{
		"size": 0,
		"aggs": {
		  "group_by_name": {
			"terms": {
			  "field": "name",
			  "min_doc_count": %d
			},
			"aggs": {
			  "min_version": {
				"min": {
				  "field": "version"
				}
			  }
			}
		  }
		}
	}
	`,min_doc_count)
	request,_:=http.NewRequest("GET",url,strings.NewReader(body))
	request.Header.Add("Content-Type","application/json")
	response,e:=client.Do(request)
	if e!=nil {
		return nil,e
	}
	b,_:=ioutil.ReadAll(response.Body)
	var ar aggregateResult
	json.Unmarshal(b,&ar)
	return ar.Aggregations.Group_by_name.Buckets,nil
}

func HasHash(hash string) (bool,error) {
	client:=http.Client{}
	url:=fmt.Sprintf("http://%s/metadata/_search?q=hash:%s&size=0",os.Getenv("ES_SERVER"),hash)
	request,_:=http.NewRequest("GET",url,nil)
	request.Header.Add("Content-Type","application/json")
	response,e:=client.Do(request)
	if e!=nil{
		return false,e
	}
	b,_:=ioutil.ReadAll(response.Body)
	var sr searchResult
	json.Unmarshal(b,&sr)
	return sr.Hits.Total!=0,nil
}

func SearchHashSize(hash string) (size int64, e error) {
	client:=http.Client{}
	url := fmt.Sprintf("http://%s/metadata/_search?q=hash:%s&size=1",os.Getenv("ES_SERVER"), hash)
	request,_:=http.NewRequest("GET",url,nil)
	request.Header.Add("Content-Type","application/json")
	response,e:=client.Do(request)
	if e != nil {
		return
	}
	if response.StatusCode != http.StatusOK {
		e = fmt.Errorf("fail to search hash size: %d", response.StatusCode)
		return
	}
	result, _ := ioutil.ReadAll(response.Body)
	var sr searchResult
	json.Unmarshal(result, &sr)
	if len(sr.Hits.Hits) != 0 {
		size = sr.Hits.Hits[0].Source.Size
	}
	return
}