package ru.otus.crowd.proj.cor.workers.corworker

import ru.otus.crowd.proj.cor.annotations.CorDslMarker
import ru.otus.crowd.proj.cor.icor.ICorExec
import ru.otus.crowd.proj.cor.icor.ICorWorkerDsl
import ru.otus.crowd.proj.cor.workers.common.CorExecDsl

@CorDslMarker
class CorWorkerDsl<T> : CorExecDsl<T>(), ICorWorkerDsl<T> {
    private var blockHandle: suspend T.() -> Unit = {}
    override fun handle(function: suspend T.() -> Unit) {
        blockHandle = function
    }

    override fun build(): ICorExec<T> = CorWorker(
        title = title,
        description = description,
        blockOn = blockOn,
        blockHandle = blockHandle,
        blockExcept = blockExcept
    )

}