package eu.octanne.mi5agent.sanctions;

import java.util.Calendar;
import java.util.UUID;

import javax.annotation.Nullable;

public class Ban extends Sanction{
	
	private Calendar untilDate; 
	
	public Ban(UUID playerID, @Nullable UUID sanctionerID, String reason, @Nullable UUID id, @Nullable Calendar date, @Nullable Calendar untilDate) {
		super(playerID, sanctionerID, reason, id, date);
		this.untilDate = untilDate;
	}
	
	public Calendar getUntilDate() {
		return untilDate;
	}
	
	public boolean isActive() {
		if(untilDate != null)return Calendar.getInstance().after(untilDate);
		else return true;
	}
}
