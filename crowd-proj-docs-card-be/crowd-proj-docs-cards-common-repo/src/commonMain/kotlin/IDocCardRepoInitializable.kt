package crowd.proj.docs.cards.common.repo

import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCard
import ru.otus.crowd.proj.docs.cards.common.repo.IRepoDocCard

interface IDocCardRepoInitializable : IRepoDocCard {
    fun save(docCards: Collection<MkPlcDocCard>): Collection<MkPlcDocCard>
}