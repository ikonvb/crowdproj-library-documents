package ru.otus.crowd.proj.cor.icor

import ru.otus.crowd.proj.cor.annotations.CorDslMarker

@CorDslMarker
interface ICorChainDsl<T> : ICorExecDsl<T> {
    fun add(worker: ICorExecDsl<T>)
}