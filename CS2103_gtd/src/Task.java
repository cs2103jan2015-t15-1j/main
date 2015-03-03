import java.time.LocalDateTime;


public class Task {
	private int id;
	private String description;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private String location;
	private boolean done;

	public String getStorageFormat() {
		return null;
	}
	public String getUserFormat() {
		//Should this include the task Index as well or should we have a separate method for that?
		return null;
	}
	public int getId() {
		return -1;
	}
	public String getDescription() {
		return null;
	}
	public LocalDateTime getStartDateTime() {
		return null;
	}
	
	public LocalDateTime getEndDateTime() {
		return null;
	}
	public String getLocation() {
		return null;
	}
	public boolean getDone() {
		return false;
	}
	public String setId(int id) {
		return null; // feedback.
	}
	public String setDescription(String desc) {
		return null; // feedback.
	}
	public String setStartDateTime(LocalDateTime dateTIme) {
		return null; // feedback.
	}
	public String setEndDateTime(LocalDateTime dateTime) {
		return null; // feedback.
	}
	public String setLocation(String location) {
		return null; // feedback
	}
	public String setDone() {
		return null; // feedback.
	}

}
