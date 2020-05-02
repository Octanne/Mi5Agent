package eu.squadev.mi5agent.sanctions;

public class Mute extends Sanction{

	public Mute(String playerName, String executorName) {
		super(playerName, executorName);
		setType("Mute");
	}

}
