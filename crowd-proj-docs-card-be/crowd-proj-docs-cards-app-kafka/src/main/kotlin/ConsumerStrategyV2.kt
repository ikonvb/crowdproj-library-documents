package ru.otus.crowd.proj.docs.be

import ru.otus.crowd.proj.docs.api.v2.apiV2RequestDeserialize
import ru.otus.crowd.proj.docs.api.v2.apiV2ResponseSerialize
import ru.otus.crowd.proj.docs.api.v2.mappers.fromTransport
import ru.otus.crowd.proj.docs.api.v2.mappers.toTransportDocCard
import ru.otus.crowd.proj.docs.be.api.v2.models.IRequest
import ru.otus.crowd.proj.docs.be.api.v2.models.IResponse
import crowd.proj.docs.cards.common.MkPlcDocCardContext

class ConsumerStrategyV2 : IConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV2, config.kafkaTopicOutV2)
    }

    override fun serialize(source: MkPlcDocCardContext): String {
        val response: IResponse = source.toTransportDocCard()
        return apiV2ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: MkPlcDocCardContext) {
        val request: IRequest = apiV2RequestDeserialize(value)
        target.fromTransport(request)
    }
}