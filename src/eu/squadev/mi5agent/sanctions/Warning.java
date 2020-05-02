package eu.squadev.mi5agent.sanctions;

public class Warning extends Sanction{
	
	public Warning(String playerName, String executorName) {
		super(playerName, executorName);
		setType("Warning");
	}
}
