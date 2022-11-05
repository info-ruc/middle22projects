#!/bin/bash
sudo systemctl enable elasticsearch.service
sudo systemctl start elasticsearch.service
curl localhost:9200/metadata -H "Content-Type: application/json" -XDELETE

curl localhost:9200/metadata -H "Content-Type: application/json" -XPUT -d'{"mappings":{"properties":{"name":{"type":"text"},"version":{"type":"integer"},"size":{"type":"integer"},"hash":{"type":"text"}}}}'
