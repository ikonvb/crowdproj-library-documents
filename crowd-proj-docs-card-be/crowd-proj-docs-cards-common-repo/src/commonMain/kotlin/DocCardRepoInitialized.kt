package crowd.proj.docs.cards.common.repo

import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCard

class DocCardRepoInitialized(
    val repo: IDocCardRepoInitializable,
    initObjects: Collection<MkPlcDocCard> = emptyList(),
) : IDocCardRepoInitializable by repo {
    @Suppress("unused")
    val initializedObjects: List<MkPlcDocCard> = save(initObjects).toList()
}