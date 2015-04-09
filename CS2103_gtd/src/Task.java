import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

//@author A0135280M
public class Task implements Comparable<Task> {
	
    private int id;
	private String description;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private boolean done;
	
	//@author A0135295B
	public Task() {
		id = -1;
		description = null;
		startDateTime = null;
		endDateTime = null;
		done = false;
	}
	
	//@author A0135280M
	public Task(int i, String desc, LocalDateTime start, LocalDateTime end, boolean d) {
	    id = i;
	    description = desc;
	    startDateTime = start;
	    endDateTime = end;
	    done = d;
	}
	
	public String getUserFormat() {
	    String doneImage = "";
	    if (this.getDone()) {
	        doneImage = Constants.DISPLAY_DONE;
	    } else {
	        doneImage = Constants.DISPLAY_UNFINISHED;
	    }
	    String feedback = String.format("%-4d%-6s%-19s%-19s%s", 
		        this.getId(), doneImage, this.getStartDateTimeInString(), 
		        this.getEndDateTimeInString(), this.getDescription());
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
		return getDateTimeInString(getStartDateTime());
	}
	
	public String getEndDateTimeInString() {
		return getDateTimeInString(getEndDateTime());
	}
	
	public static String getDateTimeInString(LocalDateTime dateTime) {
		String dateTimeString = "";
        if (dateTime != null) {
        	dateTimeString = dateTime.format(Constants.FORMAT_STORAGE_DATETIME);
        }
        return dateTimeString;
	}
	
	public boolean getDone() {
		return done;
	}
	
	public void setId(int _id) {
		id = _id;
	}
	
	public void setDescription(String _desc) {
		description = _desc;
	}

	public void setStartDateTime(LocalDateTime _startDateTime) {
		startDateTime = _startDateTime;
	}

	public void setEndDateTime(LocalDateTime _endDateTime) {
		endDateTime = _endDateTime;
	}
	
	public void setDone(boolean _done) {
		done = _done;
	}
	
	//@author A0135295B
	@Override
	public boolean equals(Object other) {
		if (this.id == ((Task)other).id &&
		this.description.equals(((Task)other).description) &&
		this.startDateTime.equals(((Task)other).startDateTime) &&
		this.endDateTime.equals(((Task)other).endDateTime) &&
		this.done == ((Task)other).done) {
			return true;
		} else {
			return false;
		}
	}
	
	//@author A0135280M
	@Override
	public int compareTo(Task compareTask) {
	    LocalDateTime compEndDateTime = compareTask.getEndDateTime();
	    
	    if (endDateTime != null && compEndDateTime != null) {
	        int diffMinutes = (int) ChronoUnit.MINUTES.between(endDateTime, compEndDateTime);
	        return diffMinutes;
	    } else if (endDateTime != null && compEndDateTime == null) {
	        return 1;
	    } else if (endDateTime == null && compEndDateTime != null) {
	        return -1;
	    } else {
	        return 0;
	    }
	}
	
}
