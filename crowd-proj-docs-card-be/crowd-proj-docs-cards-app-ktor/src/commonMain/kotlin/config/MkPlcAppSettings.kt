package config

import IMkPlcAppSettings
import crowd.proj.docs.cards.biz.MkPlcDocCardProcessor


import ru.otus.crowd.proj.docs.cards.common.MkPlcCorSettings

data class MkPlcAppSettings(
    val appUrls: List<String> = emptyList(),
    override val corSettings: MkPlcCorSettings = MkPlcCorSettings(),
    override val processor: MkPlcDocCardProcessor = MkPlcDocCardProcessor(corSettings),
) : IMkPlcAppSettings