package eu.octanne.mi5agent.sanctions;

import java.util.Calendar;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;

public abstract class Sanction {
	
	private UUID playerID;
	private UUID sanctionerID;
	
	private String reason;
	
	private Calendar sanctionDate;
	
	private UUID sanctionID;
	
	protected Sanction(UUID playerID, @Nullable UUID sanctionerID, String reason, @Nullable UUID id, @Nullable Calendar date){
		this.playerID = playerID;
		this.sanctionerID = sanctionerID;
		this.reason = reason;
		if(date != null)this.sanctionDate = date;
		else this.sanctionDate = Calendar.getInstance();
		if(id != null)this.sanctionID = id;
		else this.sanctionID = UUID.randomUUID();
	}
	
	public String getReason() {
		return reason;
	}
	
	public UUID getPlayerID() {
		return playerID;
	}
	
	public UUID getSanctionerID() {
		return sanctionerID;
	}
	
	public String getSanctionerName() {
		return sanctionerID != null ? Bukkit.getOfflinePlayer(sanctionerID).getName() : "Console";
	}
	
	public UUID getID() {
		return sanctionID;
	}
	
	public Calendar getDate() {
		return sanctionDate;
	}
	
	public String getDateToString() {
		String day = sanctionDate.get(Calendar.DAY_OF_MONTH)+"";
		String month = sanctionDate.get(Calendar.MONTH)+"";
		String year = sanctionDate.get(Calendar.YEAR)+"";
		String hour = sanctionDate.get(Calendar.HOUR_OF_DAY)+"";
		String minutes = sanctionDate.get(Calendar.MINUTE)+"";
		String secondes = sanctionDate.get(Calendar.SECOND)+"";
		StringBuilder date = new StringBuilder();
		date.append(day);
		date.append("/");
		date.append(month);
		date.append("/");
		date.append(year);
		date.append(" ");
		date.append(hour);
		date.append(":");
		date.append(minutes);
		date.append(":");
		date.append(secondes);
		return date.toString();
	}
}
