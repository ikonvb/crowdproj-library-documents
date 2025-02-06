package ru.otus.crowd.proj.cor.chains

import ru.otus.crowd.proj.cor.icor.ICorChainDsl
import ru.otus.crowd.proj.cor.icor.ICorWorkerDsl

fun <T> rootChain(function: ICorChainDsl<T>.() -> Unit): ICorChainDsl<T> = CorChainDsl<T>().apply(function)

fun <T> ICorChainDsl<T>.chain(function: ICorChainDsl<T>.() -> Unit) {
    add(CorChainDsl<T>().apply(function))
}

fun <T> ICorChainDsl<T>.worker(function: ICorWorkerDsl<T>.() -> Unit) {
    add(CorWorkerDsl<T>().apply(function))
}

fun <T> ICorChainDsl<T>.worker(
    title: String,
    description: String = "",
    blockHandle: T.() -> Unit
) {
    add(CorWorkerDsl<T>().also {
        it.title = title
        it.description = description
        it.handle(blockHandle)
    })
}
