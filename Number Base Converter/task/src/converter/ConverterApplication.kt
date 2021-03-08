package converter

import converter.command.Command
import converter.command.CommandManager
import converter.command.CommandResult

object ConverterApplication : CommandManager() {
    override fun getCommand(command: String): Command?
        = CommandType.fromCommand(command)

    override fun getMainCommand() = "Enter two numbers in format: {source base} {target base} (To quit type /exit)"

    override fun getDefaultCommand()= CommandType.TOW_NUMBERS

}

enum class CommandType(val command: String) : Command {
    TOW_NUMBERS("") {
        override fun isDefault() = true
        override fun runCommand(args: Array<String>): CommandResult {
            val split = args[0].split(" ")
            AnyBaseConverterSubcommand(split[0].toInt(), split[1].toInt()).executeProgram()
            return CommandResult("")
        }
    },
    QUIT("/exit") {
        override fun isExit() = true
    };

    companion object {
        fun fromCommand(command: String): CommandType? =
            values()
                .firstOrNull { value -> value.command == command }

    }

}