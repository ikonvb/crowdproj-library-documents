import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.Test
import ru.otus.crowd.proj.docs.api.v1.apiV1RequestSerialize
import ru.otus.crowd.proj.docs.api.v1.apiV1ResponseDeserialize
import ru.otus.crowd.proj.docs.be.AppKafkaConfig
import ru.otus.crowd.proj.docs.be.AppKafkaConsumer
import ru.otus.crowd.proj.docs.be.ConsumerStrategyV1
import ru.otus.crowd.proj.docs.be.api.v1.models.*
import java.util.*
import kotlin.test.assertEquals

class KafkaControllerTest {

    @Test
    fun runKafka() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = AppKafkaConsumer(config, listOf(ConsumerStrategyV1()), consumer = consumer, producer = producer)
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiV1RequestSerialize(
                        DocCardCreateRequest(
                            docCard = DocCardCreateObject(
                                title = "Документ №33",
                                description = "some testing docCard to check them all",
                                visibility = DocCardVisibility.OWNER_ONLY,
                                docType = DocType.APPLICATION_SLASH_PDF,
                            ),
                            debug = DocCardDebug(
                                mode = DocCardRequestDebugMode.STUB,
                                stub = DocCardRequestDebugStubs.SUCCESS,
                            ),
                        ),
                    )
                )
            )
            app.close()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.start()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize<DocCardCreateResponse>(message.value())
        assertEquals(outputTopic, message.topic())
        assertEquals("Документ №33", result.docCard?.title)
    }

    companion object {
        const val PARTITION = 0
    }
}