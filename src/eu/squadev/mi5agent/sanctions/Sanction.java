package eu.squadev.mi5agent.sanctions;

import java.util.Calendar;

public abstract class Sanction {
	//Saction info
	protected String type = "";
	protected Calendar createDate = Calendar.getInstance();
	protected String reason = "";
	
	//Players info
	private String playerName;
	private String executorName;
	
	public Sanction(String playerName, String executorName) {
		this.playerName = playerName;
		this.executorName = executorName;
	}
	
	//Player getters
	public String getPlayerName() {return this.playerName;}
	public String getExecutorName() {return this.executorName;}
	
	//Sanction getters
	public String getType() {return this.type;}
	public Calendar getCreationDate() {return this.createDate;}
	public String getCreationDateToString() {
		String day = createDate.get(Calendar.DAY_OF_MONTH)+"";
		String month = createDate.get(Calendar.MONTH)+"";
		String year = createDate.get(Calendar.YEAR)+"";
		String hour = createDate.get(Calendar.HOUR_OF_DAY)+"";
		String minutes = createDate.get(Calendar.MINUTE)+"";
		String secondes = createDate.get(Calendar.SECOND)+"";
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
	public String getReason() {return this.reason;}
	
	//Sanction setters
	protected void setType(String type) {this.type = type;}
	public void setReason(String reason) {this.reason = reason;}
	public void setDate(String date) {
		String[] sDate = date.split(" ");
		int day = Integer.parseInt(sDate[0]);
		int month = Integer.parseInt(sDate[1]);
		int year = Integer.parseInt(sDate[2]);;
		int hour = Integer.parseInt(sDate[3]);
		int minutes = Integer.parseInt(sDate[4]);
		int secondes = Integer.parseInt(sDate[5]);
		createDate.set(year, month, day, hour, minutes, secondes);
	}
}
