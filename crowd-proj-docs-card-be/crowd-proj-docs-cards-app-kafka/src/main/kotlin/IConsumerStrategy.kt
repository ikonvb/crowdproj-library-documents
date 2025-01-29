package ru.otus.crowd.proj.docs.be

import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext

/**
 * Интерфейс стратегии для обслуживания версии API
 */
interface IConsumerStrategy {
    /**
     * Топики, для которых применяется стратегия
     */
    fun topics(config: AppKafkaConfig): InputOutputTopics
    /**
     * Сериализатор для версии API
     */
    fun serialize(source: MkPlcDocCardContext): String
    /**
     * Десериализатор для версии API
     */
    fun deserialize(value: String, target: MkPlcDocCardContext)
}