import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.sber.kafkaconsumer.KafkaConsumer;
import com.sber.kafkaconsumer.clientservice.ClientService;
import com.sber.kafkaconsumer.exception.ClientException;
import com.sber.kafkaconsumer.listener.MyKafkaListener;
import com.sber.kafkaconsumer.model.MessageDto;
import io.restassured.response.Response;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.*;
import org.junit.runner.RunWith;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KafkaConsumer.class)
@TestPropertySource(properties = {"spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.consumer.auto-offset-reset=earliest", "spring.kafka.consumer.group-id=msgConsumer"})
@DirtiesContext
public class KafkaConsumerTest {

    private static final String RECEIVER_TOPIC = "Message1";

    @Autowired
    private MyKafkaListener kafkaListener;

    @Autowired
    private ClientService clientService;

    private KafkaTemplate<String, Object> template;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka =
            new EmbeddedKafkaRule(1, true, RECEIVER_TOPIC);

    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule();

    @Rule
    public WireMockClassRule instanceRule = wireMockRule;


    @Before
    public void setUp() throws Exception {
        //устанавливаем свойства продюсера
        Map<String, Object> senderProperties =
                KafkaTestUtils.producerProps(embeddedKafka.getEmbeddedKafka().getBrokersAsString());
        senderProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        senderProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        //создаем фабрику продюсера Кафки
        ProducerFactory<String, Object> producerFactory =
                new DefaultKafkaProducerFactory<>(senderProperties);

        //создаем шаблон сообщения Кафки
        template = new KafkaTemplate<>(producerFactory);
        //устанавливаем тему по умолчанию для отправки сообщения
        template.setDefaultTopic(RECEIVER_TOPIC);

        //ожидаем завершения объявления всех разделов
        for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry
                .getListenerContainers()) {
            ContainerTestUtils.waitForAssignment(messageListenerContainer,
                    embeddedKafka.getEmbeddedKafka().getPartitionsPerTopic());
        }
    }

    @Test
    public void testConsumer() throws Exception {
        //создаем тестовое сообщение
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Language", "ru, en");

        MessageDto msgTest = new MessageDto();
        msgTest.setMethod("GET");
        msgTest.setBody("");
        msgTest.setHeaders(headers);
        msgTest.setParameters("");
        msgTest.setUrl("/authors/all");

        //отправляем тестовое сообщение в Кафку
        ListenableFuture<SendResult<String, Object>> msgFuture = template.send("Message1", "msg", msgTest);
        msgFuture.addCallback(System.out::println, System.err::println);
        System.out.println("Сообщение отправлено");

        //ждем, когда сообщение будет принято
        kafkaListener.getLatch().await(15000, TimeUnit.MILLISECONDS);
        Assert.assertEquals(kafkaListener.getLatch().getCount(), 0L);
        System.out.println("Сообщение получено");

        MessageDto msgResult = kafkaListener.getMsgDTO();
        System.out.println(msgResult.getUrl());

        //задаем параметры WireMock-запроса
        stubFor(get(urlEqualTo("/authors/all"))
                .withHeader("Content-Language", equalTo("ru, en"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("[\"Стивен Кинг\",\"Роберт М. Вегнер\"," +
                                "\"Анджей Сапковский\",\"Надежда Попова\"]")));

        //пытаемся через клиент отправить полученный из Кафки запрос и получить ответ
        try {
            Response response = clientService.chooseMethod(msgResult);
            System.out.println(response.getStatusCode());
            System.out.println(response.body());
            Assert.assertEquals(response.statusCode(), 200);
        } catch (ClientException e) {
            System.err.println(e.getMessage());
        }


    }

    @Test
    public void testConsumerWithoutMocking() throws Exception {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Language", "ru, en");

        MessageDto msgTest = new MessageDto();
        msgTest.setMethod("GET");
        msgTest.setBody("");
        msgTest.setHeaders(headers);
        msgTest.setParameters("author=Стивен Кинг&title=Колдун и кристалл");
        msgTest.setUrl("/authors");

        ListenableFuture<SendResult<String, Object>> msgFuture = template.send("Message1", "msg", msgTest);
        msgFuture.addCallback(System.out::println, System.err::println);
        System.out.println("Сообщение отправлено");

        kafkaListener.getLatch().await(15000, TimeUnit.MILLISECONDS);
        Assert.assertEquals(kafkaListener.getLatch().getCount(), 0L);
        System.out.println("Сообщение получено");

        MessageDto msgResult = kafkaListener.getMsgDTO();
        System.out.println(msgResult.getUrl());

        try {
            Response response = clientService.chooseMethod(msgResult);
            System.out.println(response.getStatusCode());
            System.out.println(response.body());
            Assert.assertEquals(response.statusCode(), 200);
        } catch (ClientException e) {
            System.err.println(e.getMessage());
        }

    }

}
