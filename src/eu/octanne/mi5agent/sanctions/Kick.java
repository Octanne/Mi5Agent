package eu.octanne.mi5agent.sanctions;

import java.util.Calendar;
import java.util.UUID;

import javax.annotation.Nullable;

public class Kick extends Sanction {
	
	public Kick(UUID playerID, @Nullable UUID sanctionerID, String reason, @Nullable UUID id, @Nullable Calendar date) {
		super(playerID, sanctionerID, reason, id, date);
	}
	
}
