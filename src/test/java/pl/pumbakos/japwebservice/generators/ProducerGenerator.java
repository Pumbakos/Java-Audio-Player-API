package pl.pumbakos.japwebservice.generators;

import pl.pumbakos.japwebservice.producermodule.models.Producer;

public class ProducerGenerator {
    public static Producer createCompleteProducer(){
        Producer producer = new Producer();
        producer.setName("Amuse");
        producer.setNickname("Pour");
        producer.setSurname("Inn");

        return producer;
    }

    public static Producer createAnotherCompleteProducer(){
        Producer producer = new Producer();
        producer.setName("Explode");
        producer.setNickname("Dinner");
        producer.setSurname("Sweat");

        return producer;
    }

    public static Producer createBlankProducer(){
        return new Producer();
    }

    public static Producer createEmptyProducer(){
        Producer producer = new Producer();
        producer.setName("");
        producer.setNickname("");
        producer.setSurname("");

        return producer;
    }
}
