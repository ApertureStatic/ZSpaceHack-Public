/**
 * A class to represent a system to manage Commands.
 */
package dev.zspacehack.cmd;

import dev.zspacehack.ZSpace;
import dev.zspacehack.cmd.commands.CmdHelp;
import dev.zspacehack.cmd.commands.CmdTP;
import dev.zspacehack.cmd.commands.CmdBind;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.lang.reflect.Field;
import java.util.HashMap;

public class CommandManager {
	private final HashMap<String, Command> commands = new HashMap<>();
	public final CmdHelp help = new CmdHelp();
	public final CmdTP tp = new CmdTP();
	public final CmdBind bind = new CmdBind();
	
	/**
	 * Constructor for Command Manager. Initializes all commands.
	 */
	public CommandManager() {
		try
		{
			for(Field field : CommandManager.class.getDeclaredFields())
			{
				if (!Command.class.isAssignableFrom(field.getType())) 
					continue;
				Command cmd = (Command)field.get(this);
				commands.put(cmd.getName(), cmd);
			}
		}catch(Exception e)
		{
			System.out.println("Error initializing " + ZSpace.NAME + " commands.");
			System.out.println(e.getStackTrace().toString());
		}
	}

	/**
	 * Gets the command by a given syntax.
	 * 
	 * @param string The syntax (command) as a string.
	 * @return The Command Object associated with that syntax.
	 */
	public Command getCommandBySyntax(String string) {
		return this.commands.get(string);
	}

	/**
	 * Gets all of the Commands currently registered.
	 * 
	 * @return List of registered Command Objects.
	 */
	public HashMap<String, Command> getCommands() {
		return this.commands;
	}

	/**
	 * Gets the total number of Commands.
	 * @return The number of registered Commands.
	 */
	public int getNumOfCommands() {
		return this.commands.size();
	}

	/**
	 * Runs a command.
	 * @param commandIn A list of Command Parameters given by a "split" message.
	 */
	public void command(String[] commandIn) {
		try {
			
			// Get the command from the user's message. (Index 0 is Username)
			Command command = commands.get(commandIn[1]);

			// If the command does not exist, throw an error.
			if (command == null)
				sendChatMessage("Invalid Command! Type " + ZSpace.PREFIX + " help for a list of commands.");
			else {
				// Otherwise, create a new parameter list.
				String[] parameterList = new String[commandIn.length - 2];
				if (commandIn.length > 1) {
					for (int i = 2; i < commandIn.length; i++) {
						parameterList[i - 2] = commandIn[i];
					}
				}

				// Runs the command.
				command.runCommand(parameterList);
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			sendChatMessage("Invalid Command! Type " + ZSpace.PREFIX + " help for a list of commands.");
		} catch (InvalidSyntaxException e) {
			e.PrintToChat();
		}
	}

	/**
	 * Prints a message into the Minecraft Chat.
	 * @param message The message to be printed.
	 */
	public static void sendChatMessage(String message) {
		MinecraftClient mc = MinecraftClient.getInstance();
		mc.inGameHud.getChatHud().addMessage(Text.of("§b[" + ZSpace.NAME + "]" + message));
	}
}
