package eu.octanne.mi5agent.sanctions;

import java.util.Calendar;
import java.util.UUID;

import javax.annotation.Nullable;

public class Mute extends Sanction {
	private Calendar untilDate; 
	
	private boolean enable;
	
	public Mute(UUID playerID, @Nullable UUID sanctionerID, String reason, @Nullable UUID id, @Nullable Calendar date, @Nullable Calendar untilDate, boolean enable) {
		super(playerID, sanctionerID, reason, id, date);
		this.untilDate = untilDate;
		this.enable = enable;
	}
	
	public Calendar getUntilDate() {
		return untilDate;
	}
	
	public boolean isActive() {
		if(untilDate != null && enable)return Calendar.getInstance().after(untilDate);
		else {
			if(!enable)return false;
			else return true;
		}
	}
	
	public boolean hisEnable() {
		return enable;
	}
	
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
	public String getUntilDateToString() {
		String day = untilDate.get(Calendar.DAY_OF_MONTH)+"";
		String month = untilDate.get(Calendar.MONTH)+"";
		String year = untilDate.get(Calendar.YEAR)+"";
		String hour = untilDate.get(Calendar.HOUR_OF_DAY)+"";
		String minutes = untilDate.get(Calendar.MINUTE)+"";
		String secondes = untilDate.get(Calendar.SECOND)+"";
		StringBuilder date = new StringBuilder();
		date.append(day);
		date.append(" ");
		date.append(month);
		date.append(" ");
		date.append(year);
		date.append(" ");
		date.append(hour);
		date.append(" ");
		date.append(minutes);
		date.append(" ");
		date.append(secondes);
		return date.toString();
	}
}
