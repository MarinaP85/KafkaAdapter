import com.sber.kafkaconsumer.listener.MyKafkaListener;
import com.sber.kafkaconsumer.model.MessageDTO;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class KafkaConsumerTest {

    private static final String RECEIVER_TOPIC = "Message1";

    @Autowired
    private MyKafkaListener kafkaListener;

    private KafkaTemplate<String, MessageDTO> template;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka =
            new EmbeddedKafkaRule(1, true, RECEIVER_TOPIC);

    @Before
    public void setUp() throws Exception {
        // set up the Kafka producer properties
        Map<String, Object> senderProperties =
                KafkaTestUtils.producerProps(embeddedKafka.getEmbeddedKafka().getBrokersAsString());
        senderProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        senderProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        // create a Kafka producer factory
        ProducerFactory<String, MessageDTO> producerFactory =
                new DefaultKafkaProducerFactory<String, MessageDTO>(senderProperties);

        // create a Kafka template
        template = new KafkaTemplate<>(producerFactory);
        // set the default topic to send to
        template.setDefaultTopic(RECEIVER_TOPIC);

        // wait until the partitions are assigned
        for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry
                .getListenerContainers()) {
            ContainerTestUtils.waitForAssignment(messageListenerContainer,
                    embeddedKafka.getEmbeddedKafka().getPartitionsPerTopic());
        }
    }

    @Test
    public void testReceive() throws Exception {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("h1", "test1");
        headers.put("h2", "test2");

        MessageDTO msgTest = new MessageDTO();
        msgTest.setMethod("GET");
        msgTest.setBody("test");
        msgTest.setHeaders(headers);
        msgTest.setUrl("/test");
        msgTest.setParameters("param");

        template.sendDefault("msg", msgTest);

        kafkaListener.getLatch().await(10000, TimeUnit.MILLISECONDS);
        Assert.assertEquals(kafkaListener.getLatch().getCount(), 0L);

    }
}
