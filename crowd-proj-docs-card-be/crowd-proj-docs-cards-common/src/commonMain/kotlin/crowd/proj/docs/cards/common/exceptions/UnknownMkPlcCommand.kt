package crowd.proj.docs.cards.common.exceptions

import crowd.proj.docs.cards.common.models.MkPlcDocCardCommand


class UnknownMkPlcCommand(command: MkPlcDocCardCommand) : Throwable("Wrong command $command at mapping toTransport stage")
