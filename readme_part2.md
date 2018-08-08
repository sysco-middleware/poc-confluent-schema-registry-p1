# Confluent schema registry p.2

## Plan

Rest proxy
* Topic operations
* Produce: binary, json, avro
* Consume: binary, json, avro
* Scaling  
 
Schema registry
* Subjects

[Kafka REST Proxy](https://docs.confluent.io/current/kafka-rest/docs/index.html)


### Request

#### Content-Type

`application/kafka.[.embedded_format].[api_version]+[serialization_format]`


Embedded format types:  `binary, json, avro`  
Api version is:         `v2`  
Serialization format:   `json`  


### Topic operations (only_read)

Topics operations are read-only.   
`GET /topics`  
`GET /topics/{name}`

### Produce

| Type        | Format            |
| ------------- |:-------------:  |
| binary        | bytes (base64)  |
| json          | json            |
| avro          | encoded json    |

`!NB` Produce request supports batching

#### Binary

[base64 encoder/decoder](http://www.utilities-online.info/base64),  data can be send in base64 encoded format. 

Partition can be specified. 

```
POST /topics/topic-binary-records HTTP/1.1
Host: kafkaproxy.example.com
Content-Type: application/vnd.kafka.binary.v2+json
Accept: application/vnd.kafka.v2+json, application/vnd.kafka+json, application/json

{
  "records": [
    {
      "key": "U3lzY28tbWlkZGxld2FyZQ==",
      "value": "aHR0cHM6Ly9zeXNjby5uby8="
    },
    {
      "value": "aHR0cHM6Ly9zeXNjby5uby8="
    },
    {
      "value": "aHR0cHM6Ly9zeXNjby5uby8=",
      "partition": 0
    }
  ]

```

