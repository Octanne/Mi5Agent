package eu.squadev.mi5agent.sanctions;

public class Ban extends Sanction{

	public Ban(String playerName, String executorName) {
		super(playerName, executorName);
		setType("Ban");
	}
	
}