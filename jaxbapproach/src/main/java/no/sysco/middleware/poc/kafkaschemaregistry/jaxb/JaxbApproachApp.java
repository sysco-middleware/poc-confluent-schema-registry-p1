package no.sysco.middleware.poc.kafkaschemaregistry.jaxb;


import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectData;

public class JaxbApproachApp {
    public static void main(String[] args) {
        Schema schemaFromJaxbPojo = ReflectData.get().getSchema(Wager.class);
        System.out.println(schemaFromJaxbPojo);
    }
}
