package crowd.proj.docs.cards.common.repo

import crowd.proj.docs.cards.common.models.MkPlcDocCard


interface IDocCardRepoInitializable : IRepoDocCard {
    fun save(docCards: Collection<MkPlcDocCard>): Collection<MkPlcDocCard>
}