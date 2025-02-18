package crowd.proj.docs.cards.biz

import chains.chainOperation
import chains.chainStubs
import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.kotlin.cor.rootChain
import crowd.proj.docs.cards.biz.repo.*
import crowd.proj.docs.cards.common.MkPlcDocCardContext
import crowd.proj.docs.cards.common.MkPlcDocCardCorSettings
import crowd.proj.docs.cards.common.models.MkPlcDocCardCommand
import crowd.proj.docs.cards.common.models.MkPlcDocCardId
import crowd.proj.docs.cards.common.models.MkPlcDocCardLock
import crowd.proj.docs.cards.common.models.MkPlcDocCardState
import validation.fields.*
import validation.startValidation
import workers.init.initStatus
import workers.stub.*

@Suppress("unused")
class MkPlcDocCardProcessor(val corSettings: MkPlcDocCardCorSettings = MkPlcDocCardCorSettings.NONE) {

    suspend fun exec(ctx: MkPlcDocCardContext) = chain.exec(ctx.also { it.corSettings = corSettings })

    val chain = rootChain<MkPlcDocCardContext> {

        initStatus("Инициализация статуса запроса")
        initRepo("Инициализация репозитория")

        chainOperation("Создание документа", MkPlcDocCardCommand.CREATE) {

            chainStubs("Создание документа в режиме заглушек") {
                successCreateStub("Успешная обработка", corSettings)
                stubValidationBadTitle("Имитация ошибки валидации названия")
                stubValidationBadDescription("Имитация ошибки валидации описания")
                stubDbError("Имитация ошибки базы данных")
            }

            startValidation {
                worker("Копируем поля в объект docCardValidating") {
                    docCardValidating = mkPlcDocCardRequest.deepCopy()
                }
                worker("Очистка id") { docCardValidating.id = MkPlcDocCardId.Companion.NONE }
                worker("Очистка названия от пробелов") { docCardValidating.title = docCardValidating.title.trim() }
                worker("Очистка описания от пробелов") {
                    docCardValidating.description = docCardValidating.description.trim()
                }
                validateTitleNotEmpty("Проверка названия")
                validateTitleHasContent("Проверка символов")
                validateDescriptionNotEmpty("Проверка описания")
                validateDescriptionHasContent("Проверка символов")
            }

            chain {
                title = "Логика сохранения"
                repoPrepareCreate("Подготовка объекта для сохранения")
                repoCreate("Создание документа в БД")
            }
            prepareResult("Подготовка ответа")
        }

        chainOperation("Получение документа", MkPlcDocCardCommand.READ) {

            chainStubs("Получение документа в режиме заглушек") {
                stubReadSuccess("Успешная обработка", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubValidationBadDescription("Имитация ошибки валидации описания")
            }

            startValidation {
                worker("Копируем поля в docCardValidating") { docCardValidating = mkPlcDocCardRequest.deepCopy() }
                worker("Очистка id") { docCardValidating.id = MkPlcDocCardId(docCardValidating.id.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
            }

            chain {
                title = "Логика чтения"
                repoRead("Чтение документа из БД")
                worker {
                    title = "Подготовка ответа для Read"
                    on { state == MkPlcDocCardState.RUNNING }
                    handle { docCardRepoDone = docCardRepoRead }
                }
            }
            prepareResult("Подготовка ответа")
        }

        chainOperation("Изменить документ", MkPlcDocCardCommand.UPDATE) {

            chainStubs("Обработка заглушек") {
                stubUpdateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadTitle("Имитация ошибки валидации заголовка")
                stubValidationBadDescription("Имитация ошибки валидации описания")
                stubDbError("Имитация ошибки работы с БД")

            }

            startValidation {
                worker("Копируем поля в docCardValidating") { docCardValidating = mkPlcDocCardRequest.deepCopy() }
                worker("Очистка id") { docCardValidating.id = MkPlcDocCardId(docCardValidating.id.asString().trim()) }
                worker("Очистка lock") {
                    docCardValidating.lock = MkPlcDocCardLock(docCardValidating.lock.asString().trim())
                }
                worker("Очистка заголовка") { docCardValidating.title = docCardValidating.title.trim() }
                worker("Очистка описания") { docCardValidating.description = docCardValidating.description.trim() }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
                validateTitleNotEmpty("Проверка на непустой заголовок")
                validateTitleHasContent("Проверка на наличие содержания в заголовке")
                validateDescriptionNotEmpty("Проверка на непустое описание")
                validateDescriptionHasContent("Проверка на наличие содержания в описании")
            }

            chain {
                title = "Логика сохранения"
                repoRead("Чтение документа из БД")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareUpdate("Подготовка объекта для обновления")
                repoUpdate("Обновление документа в БД")
            }
            prepareResult("Подготовка ответа")
        }

        chainOperation("Удалить документ", MkPlcDocCardCommand.DELETE) {

            chainStubs("Обработка заглушек") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
            }

            startValidation {

                worker("Копируем поля в docCardValidating") {
                    docCardValidating = mkPlcDocCardRequest.deepCopy()
                }
                worker("Очистка id") { docCardValidating.id = MkPlcDocCardId(docCardValidating.id.asString().trim()) }
                worker("Очистка lock") {
                    docCardValidating.lock = MkPlcDocCardLock(docCardValidating.lock.asString().trim())
                }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
            }

            chain {
                title = "Логика удаления"
                repoRead("Чтение документа из БД")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareDelete("Подготовка объекта для удаления")
                repoDelete("Удаление документа из БД")
            }
            prepareResult("Подготовка ответа")
        }

        chainOperation("Поиск документов", MkPlcDocCardCommand.SEARCH) {

            chainStubs("Обработка заглушек") {
                stubSearchSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")

            }

            startValidation {
                worker("Копируем поля в docCardFilterValidating") {
                    docCardFilterValidating = mkPlcDocCardFilterRequest.deepCopy()
                }
                validateSearchStringLength("Валидация длины строки поиска в фильтре")
            }

            repoSearch("Поиск документа в БД по фильтру")
            prepareResult("Подготовка ответа")
        }

        chainOperation("Поиск подходящих предложений для документов", MkPlcDocCardCommand.OFFERS) {

            chainStubs("Обработка заглушек") {
                stubOffersSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
            }

            startValidation {
                worker("Копируем поля в docCardValidating") { docCardValidating = mkPlcDocCardRequest.deepCopy() }
                worker("Очистка id") { docCardValidating.id = MkPlcDocCardId(docCardValidating.id.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
            }

            chain {
                title = "Логика поиска в БД"
                repoRead("Чтение документа из БД")
                repoPrepareOffers("Подготовка данных для поиска предложений")
                repoOffers("Поиск предложений для документа в БД")
            }
            prepareResult("Подготовка ответа")

        }
    }.build()
}