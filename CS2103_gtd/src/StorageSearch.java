import java.time.LocalDateTime;
import java.util.Map;

//@author A0135280M
public class StorageSearch {
    
    public String search(Map<Integer, Task> tasks, Task searchObj, int lastIdNo) {
        int[] foundTasks = new int[lastIdNo];
        foundTasks = searchOnKeyword(tasks, searchObj, foundTasks);
        foundTasks = searchOnDate(tasks, searchObj, foundTasks);

        String searchResult = "";
        for (int i=0; i<foundTasks.length; i++) {
            if (foundTasks[i] == Constants.INCLUDED_IN_SEARCH) {
                Task task = tasks.get(i+1);
                searchResult += "\n" + task.getUserFormat();
            }
        }
        return searchResult;
    }
    
    private int[] searchOnKeyword(Map<Integer, Task> tasks, Task searchObj, 
            int[] foundTasks) {
        String keyword = searchObj.getDescription();
        for (Task task : tasks.values()) {
            String taskDesc = task.getDescription();
            if (taskDesc.toLowerCase().contains(keyword.toLowerCase())) {
                int index = task.getId()-1;
                foundTasks[index] = Constants.INCLUDED_IN_SEARCH;
            }
        }
        return foundTasks;
    }
    
    private int[] searchOnDate(Map<Integer, Task> tasks, Task searchObj, int[] foundTasks) {
        LocalDateTime startDate = searchObj.getStartDateTime();
        LocalDateTime endDate = searchObj.getEndDateTime();
        if (isDateSearch(startDate, endDate)) {
            for (Task task : tasks.values()) {
                int index = task.getId()-1;
                foundTasks[index] = isTaskInInterval(task, startDate, 
                        endDate, foundTasks[index]);
            }
        }
        return foundTasks;
    }
    
    private int isTaskInInterval(Task task, LocalDateTime searchStartDate, 
            LocalDateTime searchEndDate, int originalValue) {
        LocalDateTime taskStartDate = task.getStartDateTime();
        LocalDateTime taskEndDate = task.getEndDateTime();
        boolean startIsAfter = false;
        boolean startIsOn = false;
        boolean endIsBefore = false;
        boolean endIsOn = false;
        
        if (task.isFloatingTask()) {
            return Constants.NOT_INCLUDED_IN_SEARCH;
        } else if (task.isDeadlineTask()) {
            startIsAfter = taskEndDate.isAfter(searchStartDate);
            startIsOn = taskEndDate.equals(searchStartDate);
        } else if (task.isEventTask()) {
            startIsAfter = taskStartDate.isAfter(searchStartDate);
            startIsOn = taskStartDate.equals(searchStartDate);
        } else {
            System.err.println(Constants.MESSAGE_GENERAL_ERROR);
        }
        endIsBefore = taskEndDate.isBefore(searchEndDate);
        endIsOn = taskEndDate.equals(searchEndDate);
        
        if (isNotInInterval(startIsAfter, startIsOn, endIsBefore, endIsOn)) {
            return Constants.NOT_INCLUDED_IN_SEARCH;
        }
        return originalValue;
    }
    
    private boolean isDateSearch(LocalDateTime startDate, LocalDateTime endDate) {
        return !(startDate.equals(LocalDateTime.MIN) && endDate.equals(LocalDateTime.MAX));
    }
    
    private boolean isFloatingTask(LocalDateTime taskEndDate) {
        return taskEndDate == null;
    }
    
    private boolean isDeadlineTask(LocalDateTime taskStartDate) {
        return taskStartDate == null;
    }
    
    private boolean isNotInInterval(boolean startIsAfter, boolean startIsOn, 
            boolean endIsBefore, boolean endIsOn) {
        return !((startIsAfter || startIsOn) && (endIsBefore || endIsOn));
    }

}
