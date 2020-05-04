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
			if(enable)return true;
			else return false;
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
	
	public String getUntilTime() {
		Calendar now = Calendar.getInstance();
		if(untilDate != null && now.before(untilDate)) {
			long millis = untilDate.getTimeInMillis()-now.getTimeInMillis();
			int sec = (int) (millis/1000);
			int min = sec/60;
			sec %= 60;
			int hour = min/60;
			min %= 60;
			int day = hour/24;
			hour %= 24;
			int month = day/30;
			day %= 30;
			int year = month/12;
			month %= 12;
			
			String format = "";
			if(year != 0) {
				format +="§c"+year+" §7an(s) ";
			}if(month != 0) {
				format +="§c"+month+" §7mois ";
			}if(day != 0) {
				format +="§c"+day+" §7jour(s) ";
			}if(hour != 0) {
				format +="§c"+year+" §7heure(s) ";
			}if(min != 0){
				format +="§c"+min+" §7minute(s) ";
			}if(sec != 0) {
				format +="§c"+sec+" §7sec(s) ";
			}
			return format.endsWith(" ") ? format.substring(0, format.length()-1) : format;
		}else if(untilDate == null){
			return "A vie";
		}else {
			return "Terminé";
		}
	}
}
