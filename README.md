# Kafka Schema registry poc
## How to run
Run docker with image: [landoop/fast-data-dev](https://github.com/Landoop/fast-data-dev) 

`docker-compose up -d`  

Image contains Kafka, Zookeeper, Schema Registry, Kafka-Connect, Landoop Tools, 20+ connectors.
## Data types
Primitive types

| Type          | Description | 
| ------------- |:-------------:| 
| null    | no value | 
| boolean | binary value | 
| int     | 32-bit| 
| long    | 64-bit |  
| float   | floating-point 32-bit precision |  
| double  | floating-point 64-bit precision |  
| bytes   | seq of 8-bit |  
| string  | unicode char seq |  

Complex types  

| Type          | Example | 
| ------------- |:-------------:| 
| Enum    | {"type":"enum","name":"Status","symbols":["CREATED","UPDATED"]} | 
| Array | {"type":"array","items":"string"} | 
| Map     | {"type":"map", "values":"string"}| 
| Union    | {"name":"childs", "type":["null", "int"], "default":null} |  
| Other Schema as a type   | {"name":"employee", "type":"Employee"} |  

## How to create schema
1. Directly in code from
```java
  Schema.Parser parser = new Schema.Parser();
  Schema schema = parser.parse("{\n"
    + "   \"type\": \"record\",\n"
    + "   \"namespace\": \"no.sysco.middleware.poc.kafkaschemaregistry.avro\",\n"
    + "   \"name\": \"Business\",\n"
    + "   \"version\": \"1\",\n"
    + "   \"doc\":\"Business record contains name of company and list of customers\",\n"
    + "   \"fields\": [\n"
    + "     { \"name\": \"company_name\", \"type\": \"string\", \"doc\": \"Name of company\" },\n"
    + "     {\n"
    + "        \"name\": \"customers\",\n"
    + "        \"doc\": \"List of customers\",\n"
    + "        \"type\": {\n"
    + "          \"type\": \"array\",\n"
    + "          \"items\": {\n"
    + "            \"type\": \"record\",\n"
    + "            \"name\":\"Customer\",\n"
    + "            \"fields\":[\n"
    + "              { \"name\": \"first_name\", \"type\": \"string\", \"doc\":\"Customer name\" },\n"
    + "              { \"name\": \"last_name\", \"type\": \"string\", \"doc\": \"Customer last name\" }\n"
    + "            ]\n"
    + "          }\n"
    + "        }\n"
    + "     }\n"
    + "   ]\n"
    + "}");
``` 

2. Reflection - from POJO
```java
  Schema schema = ReflectData.get().getSchema(Business.class);
```  
Business class
```java
public class Business {
    private String companyName;
    public Business(){}
    public Business(String companyName) {this.companyName = companyName; }
    // getters & setters
}
```
3. From AVSC file (json) via plugin
Define schema in `resources/avro` and generate via `avro-maven-plugin`

### Schema evolution
| Type          | Description | 
| ------------- |:-------------:| 
| Backward | Old schema can be used to read New data | 
| Forward | New schema can be used to read Old data | 
| Full compatibility (Forward and Backward)  | Both (Old & New) schemas can be used to read old & new data | 
| Breaking | None of those |  
Target is type `Full compatibility`. 

## Important notes
1. Make your primary key required
2. Give default values to all the fields that could be removed in the future
3. Be very careful when using ENUM as the can not evolve over time
4. Do not rename fields. You can add aliases instead
5. When evolving schema, ALWAYS give default values
6. When evolving schema, NEVER remove, rename of the required field or change the type  

## Refs:  
* [Confluent docs](https://docs.confluent.io/current/schema-registry/docs/api.html#overview)
* [Stephane Maarek](https://www.udemy.com/confluent-schema-registry/learn/v4/content)

 
