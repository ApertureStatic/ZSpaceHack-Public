package dev.zspacehack.cmd.commands;

import dev.zspacehack.ZSpace;
import dev.zspacehack.cmd.Command;
import dev.zspacehack.cmd.CommandManager;
import dev.zspacehack.module.Module;
import dev.zspacehack.module.ModuleManager;

public class CmdBind extends Command {

	public CmdBind() {
		super("bind", "Bind key", "[module] [key]");
	}

	@Override
	public void runCommand(String[] parameters) {
		if (parameters.length == 0) {
			CommandManager.sendChatMessage("Please specify a module.");
			return;
		}
		String moduleName = parameters[0];
		Module module = ZSpace.MODULE.getModuleByName(moduleName);
		if (module == null) {
			CommandManager.sendChatMessage("Unknown module!");
			return;
		}
		if (parameters.length == 1) {
			CommandManager.sendChatMessage("Please specify a key.");
			return;
		}
		String rkey = parameters[1];
		if (rkey == null) {
			CommandManager.sendChatMessage("\u00a7cerror");
			return;
		}
		if (module.setBind(rkey.toUpperCase())) {
			CommandManager.sendChatMessage("\u00a7a[√] \u00a7fBind for \u00a7a" + module.getName() + "\u00a7f set to \u00a77" + rkey.toUpperCase());
		}
	}

	@Override
	public String[] getAutocorrect(int count) {
		if (count == 2) {
			ModuleManager cm = ZSpace.MODULE;
			int numCmds = cm.modules.size();
			String[] commands = new String[numCmds];

			int i = 0;
			for (Module x : cm.modules) {
				commands[i++] = x.getName();
			}

			return commands;
		} else {
			return null;
		}
	}
}
