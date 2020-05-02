package eu.octanne.mi5agent.sanctions;

import java.util.Calendar;
import java.util.UUID;

import javax.annotation.Nullable;

public class Warning extends Sanction {
	
	public Warning(UUID playerID, @Nullable UUID sanctionerID, String reason, @Nullable UUID id, @Nullable Calendar date) {
		super(playerID, sanctionerID, reason, id, date);
	}
	
}
