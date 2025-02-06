package ru.otus.crowd.proj.cor.workers.corchain

import ru.otus.crowd.proj.cor.annotations.CorDslMarker
import ru.otus.crowd.proj.cor.icor.ICorChainDsl
import ru.otus.crowd.proj.cor.icor.ICorExec
import ru.otus.crowd.proj.cor.icor.ICorExecDsl
import ru.otus.crowd.proj.cor.workers.common.CorExecDsl

@CorDslMarker
class CorChainDsl<T>(
) : CorExecDsl<T>(), ICorChainDsl<T> {
    private val workers: MutableList<ICorExecDsl<T>> = mutableListOf()
    override fun add(worker: ICorExecDsl<T>) {
        workers.add(worker)
    }

    override fun build(): ICorExec<T> = CorChain(
        title = title,
        description = description,
        execs = workers.map { it.build() },
        blockOn = blockOn,
        blockExcept = blockExcept
    )
}