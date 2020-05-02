package eu.octanne.mi5agent.sanctions;

import java.util.Calendar;
import java.util.UUID;

import javax.annotation.Nullable;

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
	
	public UUID getID() {
		return sanctionID;
	}
	
	public Calendar getDate() {
		return sanctionDate;
	}
}
