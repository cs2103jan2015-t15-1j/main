import java.time.LocalDateTime;


public class Task {
	
	
	private int id;
	private String description;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private String location;
	private boolean done;

	public Task() {
		id = -1;
		description = null;
		startDateTime = null;
		endDateTime = null;
		location = null;
		done = false;
	}
	
	public Task(int i, String desc, LocalDateTime start, LocalDateTime end, boolean d) {
	    id = i;
	    description = desc;
	    startDateTime = start;
	    endDateTime = end;
	    done = d;
	}
	
	public String getStorageFormat() {
		return null;
	}
	public String getUserFormat() {
		//Should this include the task Index as well or should we have a separate method for that?
		//Brief information about id, desc, startdate, enddate ?? and maybe in the future, specify task type as well? 
		String feedback = String.format(Constants.FORMAT_DISPLAY_TASKINFO, this.getId(), this.getDescription(), this.getStartDateTimeInString(), this.getEndDateTimeInString()); 
		return feedback;
	}
	public int getId() {
		return id;
	}
	public String getDescription() {
		return description;
	}
	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}
	
	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}
	
	public String getStartDateTimeInString() {
		return getDateTimeInString(this.getStartDateTime());
	}
	
	public String getEndDateTimeInString() {
		return getDateTimeInString(this.getEndDateTime());
	}
	
	public static String getDateTimeInString(LocalDateTime dateTime) {
		String dateTimeString = Constants.STORAGE_ENTRY_DOES_NOT_EXIST;
        if (dateTime != null) {
        	dateTimeString = dateTime.format(Constants.FORMAT_STORAGE_DATETIME);
        }
        return dateTimeString;
	}
	
	public String getLocation() {
		return location;
	}
	public boolean getDone() {
		return done;
	}
	public String setId(int id) {
		this.id = id;
		return null; // feedback.
	}
	public String setDescription(String desc) {
		this.description = desc;
		return null; // feedback.
	}
	public String setStartDateTime(LocalDateTime dateTime) {
		this.startDateTime = dateTime;
		return null; // feedback.
	}
	public String setEndDateTime(LocalDateTime dateTime) {
		this.endDateTime = dateTime;
		return null; // feedback.
	}
	public String setLocation(String location) {
		this.location = location;
		return null; // feedback
	}
	public String setDone(boolean done) {
		this.done = done;
		return null; // feedback.
	}
	
	@Override
	public boolean equals(Object other) {
		if (this.id == ((Task)other).id &&
		this.description.equals(((Task)other).description) &&
		this.startDateTime.equals(((Task)other).startDateTime) &&
		this.endDateTime.equals(((Task)other).endDateTime) &&
		this.location.equals(((Task)other).location) &&
		this.done == ((Task)other).done) {
			return true;
		} else {
			return false;
		}
	}
	
	public Task(String descr, LocalDateTime start, LocalDateTime end){
		description = descr;
		startDateTime = start;
		endDateTime = end;
	}
	
}
