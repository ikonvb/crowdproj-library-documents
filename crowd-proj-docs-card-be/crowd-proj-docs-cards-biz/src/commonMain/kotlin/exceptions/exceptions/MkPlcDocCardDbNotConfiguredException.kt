package crowd.proj.docs.cards.biz.exceptions.exceptions

import ru.otus.crowd.proj.docs.cards.common.models.MkPlcDocCardWorkMode

class MkPlcDocCardDbNotConfiguredException(val workMode: MkPlcDocCardWorkMode): Exception(
    "Database is not configured properly to work with work mode: = $workMode"
)
