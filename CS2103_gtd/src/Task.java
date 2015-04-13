import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

//@author A0135280M
public class Task implements Comparable<Task> {
	
	private static final int ID_NEW_EMPTY_TASK = -1;
	private static final int CHAR_LENGTH_DAY_OF_WEEK_DISPLAY = 3;
	private static final String EMPTY_STRING = "";
    private int id;
	private String description;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private boolean done;
	
	//@author A0135295B
	public Task() {
		id = ID_NEW_EMPTY_TASK;
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
	
	//@author A0135295B
    private static String getDateTimeInString(LocalDateTime dateTime) {
        String dateTimeString = EMPTY_STRING;
        if (dateTime != null) {
            dateTimeString = dateTime.format(Constants.FORMAT_STORAGE_DATETIME);
        }
        return dateTimeString;
    }
    
	public String getStartDateTimeInString() {
		return getDateTimeInString(getStartDateTime());
	}
	
	public String getEndDateTimeInString() {
		return getDateTimeInString(getEndDateTime());
	}
    
    private String getDateTimeForDisplay(LocalDateTime dateTime) {
        String dateTimeString = EMPTY_STRING;
        if (dateTime != null) {
            dateTimeString = dateTime.getDayOfWeek().toString().substring(0, CHAR_LENGTH_DAY_OF_WEEK_DISPLAY) + " ";
            dateTimeString += dateTime.format(Constants.FORMAT_STORAGE_DATETIME);
        }
        return dateTimeString;
    }
	
	public String getStartDateForDisplay() {
	    return getDateTimeForDisplay(getStartDateTime());
	}
	
	public String getEndDateForDisplay() {
        return getDateTimeForDisplay(getEndDateTime());
    }
	
	//@author A0135280M
	public boolean isDone() {
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
    
    public boolean isEventTask() {
        if ((startDateTime != null && !startDateTime.equals(LocalDateTime.MIN)) && 
                (endDateTime != null && !endDateTime.equals(LocalDateTime.MAX))) {
            return true;
        }
        return false;
    }
    
    public boolean isDeadlineTask() {
        if ((startDateTime == null || startDateTime.equals(LocalDateTime.MIN)) && 
                (endDateTime != null && !endDateTime.equals(LocalDateTime.MAX))) {
            return true;
        }
        return false;
    }
    
    public boolean isFloatingTask() {
        if ((startDateTime == null || startDateTime.equals(LocalDateTime.MIN)) && 
                (endDateTime == null || endDateTime.equals(LocalDateTime.MAX))) {
            return true;
        }
        return false;
    }
    
    /**
     * Returns a string in tabular format with all task information formatted 
     * to be displayed to the user
     */
    public String getUserFormat() {
    	String doneImage = Constants.DISPLAY_UNDONE;
        String statusImage = EMPTY_STRING;
        if (isDone()) {
            doneImage = Constants.DISPLAY_DONE;
        }
        if (isEventTask() && isOverdue(startDateTime) && !isDone()) {
            statusImage = Constants.DISPLAY_OVERDUE;
        } else if (isDeadlineTask() && isOverdue(endDateTime) && !isDone()) {
            statusImage = Constants.DISPLAY_OVERDUE;
        }
        String feedback = String.format(Constants.DISPLAY_TASK_FORMAT, 
                getId(), statusImage, doneImage, getDateTimeForDisplay(this.getStartDateTime()), 
                getDateTimeForDisplay(this.getEndDateTime()), getDescription());
        return feedback;
    }
    
    private boolean isOverdue(LocalDateTime timeToCheck) {
        LocalDateTime timeNow = LocalDateTime.now();
        return timeToCheck.isBefore(timeNow);
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
