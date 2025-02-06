import MkPlcDocCardProcessor
import ru.otus.crowd.proj.docs.cards.common.MkPlcCorSettings

interface IMkPlcAppSettings {
    val processor: MkPlcDocCardProcessor
    val corSettings: MkPlcCorSettings
}