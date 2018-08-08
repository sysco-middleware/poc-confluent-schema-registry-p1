# Confluent schema registry p.2

![img](./kakfa_REST_proxy_workflow.png)

## Plan
`Part A`
1. Schema registry
* Subjects

`Part B`  
1. Role of Kafka REST Proxy
2. Operations: 
* Topic operations
* Produce: binary, json, avro
* Consume: binary, json, avro
 
 
## Part A

Schema registry
todo: REST subjects -> ref to [K_GO](https://github.com/sysco-middleware/KGO) project

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

Produce & Consume operations: 

| Type        | Format            |
| ------------- |:-------------:  |
| binary        | bytes (base64)  |
| json          | json            |
| avro          | encoded json    |

`!NB` Produce request supports batching

### Binary

#### Produce

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

#### Consume

1. Create consumer (once)
2. Use returned by REST Proxy URL for consuming
3. Get records
4. Process (depends on application)
5. Commit offset

`NB!`  
If step on step 4 processing time will be longer than `max.poll.interval.ms` timeout, client will receive `500`.  
If REST Proxy fail, it will try to close all consumers.


### Json

Produce & Consume operations with json are similar to binaries, the main differences are headers.

```
Content-Type: application/vnd.kafka.json.v2+json
Accept: application/vnd.kafka.v2+json, application/vnd.kafka+json, application/json

```



## References 

[Kafka REST Proxy](https://docs.confluent.io/current/kafka-rest/docs/index.html)

