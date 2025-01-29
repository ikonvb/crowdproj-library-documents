import kotlinx.datetime.Clock
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.helpers.asMkPlcError
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardCommand
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardState
import kotlin.reflect.KClass


suspend inline fun <T> IMkPlcAppSettings.controllerHelper(
    crossinline getRequest: suspend MkPlcDocCardContext.() -> Unit,
    crossinline toResponse: suspend MkPlcDocCardContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = MkPlcDocCardContext(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        processor.exec(ctx)
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.state = MkPlcDocCardState.FAILING
        ctx.errors.add(e.asMkPlcError())
        processor.exec(ctx)
        if (ctx.command == MkPlcDocCardCommand.NONE) {
            ctx.command = MkPlcDocCardCommand.READ
        }
        ctx.toResponse()
    }
}