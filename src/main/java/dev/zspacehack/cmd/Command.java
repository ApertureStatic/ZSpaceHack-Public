/**
 * A class to represent a Command
 */
package dev.zspacehack.cmd;

import dev.zspacehack.utils.Wrapper;

import java.util.Objects;

public abstract class Command implements Wrapper {
	protected final String name;
	protected final String description;
	protected final String syntax;
	
	public Command(String name, String description, String syntax) {
		this.name = Objects.requireNonNull(name);
		this.description = Objects.requireNonNull(description);
		this.syntax = Objects.requireNonNull(syntax);
	}
	
	/**
	 * Gets the name of the command.
	 * @return The name of the command.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets the description of the command.
	 * @return The description of the command.
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * Gets the syntax of the command.
	 * @return The syntax of the command.
	 */
	public String getSyntax() {
		return this.syntax;
	}
	
	/**
	 * Runs the intended action of the command.
	 * @param parameters The parameters being passed.
	 */
	public abstract void runCommand(String[] parameters) throws InvalidSyntaxException;
	
	/**
	 * Gets the next Autocorrect suggestions given the last typed parameter.
	 * @param previousParameter
	 */
	public abstract String[] getAutocorrect(int count);
}
