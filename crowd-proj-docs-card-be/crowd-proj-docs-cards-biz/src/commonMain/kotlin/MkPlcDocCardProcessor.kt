import chains.chainOperation
import chains.chainStubs
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.kotlin.cor.rootChain
import ru.otus.crowd.proj.docs.cards.common.MkPlcCorSettings
import ru.otus.crowd.proj.docs.cards.common.MkPlcDocCardContext
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardCommand
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardId
import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardLock
import validation.fields.*
import validation.startValidation
import workers.init.initStatus
import workers.stub.*

@Suppress("unused")
class MkPlcDocCardProcessor(val corSettings: MkPlcCorSettings = MkPlcCorSettings.Companion.NONE) {

    suspend fun exec(ctx: MkPlcDocCardContext) = chain.exec(ctx.also { it.corSettings = corSettings })

    val chain = rootChain<MkPlcDocCardContext> {

        initStatus("Инициализация статуса запроса")

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
        }
    }.build()
}