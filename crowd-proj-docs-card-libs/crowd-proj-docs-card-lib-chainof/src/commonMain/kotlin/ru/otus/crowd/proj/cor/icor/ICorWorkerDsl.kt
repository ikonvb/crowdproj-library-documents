package ru.otus.crowd.proj.cor.icor

import ru.otus.crowd.proj.cor.annotations.CorDslMarker

@CorDslMarker
interface ICorWorkerDsl<T> : ICorExecDsl<T> {
    fun handle(function: suspend T.() -> Unit)
}