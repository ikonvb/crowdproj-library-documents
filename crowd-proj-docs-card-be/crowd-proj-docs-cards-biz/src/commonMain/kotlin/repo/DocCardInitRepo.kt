package crowd.proj.docs.cards.biz.repo

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import crowd.proj.docs.cards.biz.exceptions.MkPlcDocCardDbNotConfiguredException
import  crowd.proj.docs.cards.common.MkPlcDocCardContext
import  crowd.proj.docs.cards.common.helpers.errorSystem
import  crowd.proj.docs.cards.common.helpers.fail
import  crowd.proj.docs.cards.common.models.MkPlcDocCardWorkMode
import  crowd.proj.docs.cards.common.repo.IRepoDocCard


fun CorChainDsl<MkPlcDocCardContext, Unit>.initRepo(title: String) = worker {

    println("initRepo called")

    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от запрошенного режима работы        
    """.trimIndent()
    handle {

        docCardRepo = when (workMode) {

            MkPlcDocCardWorkMode.TEST -> corSettings.repoTest
            MkPlcDocCardWorkMode.STUB -> corSettings.repoStub
            else -> corSettings.repoProd
        }

        if (workMode != MkPlcDocCardWorkMode.STUB && docCardRepo == IRepoDocCard.NONE) {

            println("ERRORRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR = $workMode")

            fail(

                errorSystem(
                    violationCode = "dbNotConfigured",
                    e = MkPlcDocCardDbNotConfiguredException(workMode)
                )
            )
        }
    }
}
