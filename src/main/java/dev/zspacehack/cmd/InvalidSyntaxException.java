/**
 * A class to represent an exception thrown when a command is typed with invalid syntax.
 */
package dev.zspacehack.cmd;

import dev.zspacehack.ZSpace;

public class InvalidSyntaxException extends CommandException {
	private static final long serialVersionUID = 1L;
	
	public InvalidSyntaxException(Command cmd) {
		super(cmd);
	}

	@Override
	public void PrintToChat() {
		CommandManager.sendChatMessage("Invalid Usage! Usage: " + ZSpace.PREFIX + " " + cmd.getName() + " " + cmd.getSyntax());
	}
}
