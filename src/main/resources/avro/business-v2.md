{
   "type": "record",
   "namespace": "no.sysco.middleware.poc.kafkaschemaregistry.avro",
   "name": "Business",
   "version": "2",
   "doc":"Business record contains name of company and list of customers",
   "fields": [
     { "name": "company_name", "type": "string", "doc": "Name of company" },
     { "name": "company_telephone", "type": "string", "doc": "Company telephone", "default":"123123" },
     {
        "name": "customers",
        "doc": "List of customers",
        "type": {
          "type": "array",
          "items": {
            "type": "record",
            "name":"Customer",
            "fields":[
              { "name": "first_name", "type": "string", "doc":"Customer name" },
              { "name": "last_name", "type": "string", "doc": "Customer last name" },
              { "name": "middle_name", type":["null", "string"] "doc": "Customer middle name" }
            ]
          }
        }
     }
   ]
}