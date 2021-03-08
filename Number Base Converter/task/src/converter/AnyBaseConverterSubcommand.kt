package converter

import converter.command.Command
import converter.command.CommandManager
import converter.command.CommandResult
import converter.service.NumeralSystem

class AnyBaseConverterSubcommand(val sourceBase: Int, val targetBase: Int) : CommandManager() {
    private val backCommand = object : Command {
        override fun isExit() = true
    }

    private val defaultSubCommand = object : Command {
        override fun isDefault() = true
        override fun runCommand(args: Array<String>): CommandResult {
            val numeralSystem = NumeralSystem(args[0], sourceBase)
            val converted = numeralSystem.calculateRepresentationInBase(targetBase)
            return CommandResult("Conversion result: $converted")
        }
    }

    override fun getCommand(command: String) = if (command.trim() == "/back") backCommand else null

    override fun getDefaultCommand() = defaultSubCommand

    override fun getMainCommand() =
        "Enter number in base $sourceBase to convert to base $targetBase (To go back type /back)"


}

