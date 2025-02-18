package ru.otus.crowd.proj.docs.be

import crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.api.v1.apiV1RequestDeserialize
import ru.otus.crowd.proj.docs.api.v1.apiV1ResponseSerialize
import ru.otus.crowd.proj.docs.be.api.v1.models.IRequest
import ru.otus.crowd.proj.docs.be.api.v1.models.IResponse
import ru.otus.crowd.proj.docs.cards.api.v1.mappers.fromTransport
import ru.otus.crowd.proj.docs.cards.api.v1.mappers.toTransportDocCard

class ConsumerStrategyV1 : IConsumerStrategy {

    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)
    }

    override fun serialize(source: MkPlcDocCardContext): String {
        val response: IResponse = source.toTransportDocCard()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: MkPlcDocCardContext) {
        val request: IRequest = apiV1RequestDeserialize(value)
        target.fromTransport(request)
    }
}