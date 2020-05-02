package eu.squadev.mi5agent.sanctions;

import java.util.Calendar;

public class MuteTempory extends Mute{
	
	protected Calendar untilDate = Calendar.getInstance();
	
	public MuteTempory(String playerName, String executorName, Calendar untilDate) {
		super(playerName, executorName);
		setType("MuteTempory");
		setUntilDate(untilDate);
	}
	public MuteTempory(String playerName, String executorName, String untilDate) {
		super(playerName, executorName);
		setType("MuteTempory");
		setUntilDate(untilDate);
	}
	

	//Sanction getters
	public Calendar getUntilDate() {
		return this.untilDate;
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
	
	//CONTITIONS
	public boolean isActive() {
		if(!Calendar.getInstance().after(untilDate)) {
			return true;
		}else {
			return false;
		}
	}
	
	//Sanction setters
	public void setUntilDate(Calendar cal) {
		this.untilDate = cal;
	}
	//Sanction setters
	public void setUntilDate(String cal) {
		String[] sDate = cal.split(" ");
		int day = Integer.parseInt(sDate[0]);
		int month = Integer.parseInt(sDate[1]);
		int year = Integer.parseInt(sDate[2]);
		int hour = Integer.parseInt(sDate[3]);
		int minutes = Integer.parseInt(sDate[4]);
		int secondes = Integer.parseInt(sDate[5]);
		untilDate.set(year, month, day, hour, minutes, secondes);
	}
}
