package converter.command

import java.lang.RuntimeException

class UnknownCommandException(argument: String) : RuntimeException("Unknown command $argument")