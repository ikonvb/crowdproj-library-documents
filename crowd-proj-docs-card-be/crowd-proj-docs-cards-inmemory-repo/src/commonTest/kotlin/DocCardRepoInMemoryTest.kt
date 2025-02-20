import crowd.proj.docs.cards.common.repo.DocCardRepoInitialized
import crowd.proj.docs.cards.inmemory.repo.DocCardRepoInMemory
import crowd.proj.docs.cards.tests.repo.*


class DocCardRepoInMemoryCreateTest : RepoDocCardCreateTest() {
    override val repo = DocCardRepoInitialized(
        DocCardRepoInMemory(randomUuid = { uuidNew.asString() }),
        initObjects = initObjects,
    )
}

class DocCardRepoInMemoryDeleteTest : RepoDocCardDeleteTest() {
    override val repo = DocCardRepoInitialized(
        DocCardRepoInMemory(),
        initObjects = initObjects,
    )
}

class DocCardRepoInMemoryReadTest : RepoDocCardReadTest() {
    override val repo = DocCardRepoInitialized(
        DocCardRepoInMemory(),
        initObjects = initObjects,
    )
}

class DocCardRepoInMemorySearchTest : RepoDocCardSearchTest() {
    override val repo = DocCardRepoInitialized(
        DocCardRepoInMemory(),
        initObjects = initObjects,
    )
}

class DocCardRepoInMemoryUpdateTest : RepoDocCardUpdateTest() {
    override val repo = DocCardRepoInitialized(
        DocCardRepoInMemory(randomUuid = { lockNew.asString() }),
        initObjects = initObjects,
    )
}
