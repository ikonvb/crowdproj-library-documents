package ru.otus.crowd.proj.cor.icor

import ru.otus.crowd.proj.cor.annotations.CorDslMarker

@CorDslMarker
interface ICorExecDsl<T> {
    var title: String
    var description: String
    fun on(function: suspend T.() -> Boolean)
    fun except(function: suspend T.(e: Throwable) -> Unit)

    fun build(): ICorExec<T>
}