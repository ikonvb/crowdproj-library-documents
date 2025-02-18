import crowd.proj.docs.cards.biz.MkPlcDocCardProcessor
import crowd.proj.docs.cards.common.MkPlcDocCardCorSettings

interface IMkPlcAppSettings {
    val processor: MkPlcDocCardProcessor
    val corSettings: MkPlcDocCardCorSettings
}