package nl.communitynetwerk.winakgames.commands;

public class DebugCMD extends BaseCMD {

	public DebugCMD() {
		forcePlayer = false;
		cmdName = "debug";
		forceInGame = false;
		argLength = 2;
		usage = "<game>";
	}

	@Override
	public boolean run() {
		gameManager.runDebugger(sender, args[1]);
		return true;
	}
}
