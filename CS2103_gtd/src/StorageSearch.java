import java.time.LocalDateTime;
import java.util.Map;


public class StorageSearch {
    
    public String search(Map<Integer, Task> tasks, Task searchObj, int lastIdNo) {
        String keyword = searchObj.getDescription();
        LocalDateTime startDate = searchObj.getStartDateTime();
        LocalDateTime endDate = searchObj.getEndDateTime();
        
        int[] foundTasks = new int[lastIdNo];
        foundTasks = searchOnKeyword(tasks, keyword, foundTasks);
        foundTasks = searchOnDate(tasks, startDate, endDate, foundTasks);

        String searchResult = "";
        for (int i=0; i<foundTasks.length; i++) {
            if (foundTasks[i] == Constants.INCLUDED_IN_SEARCH) {
                Task task = tasks.get(i+1);
                searchResult += "\n" + task.getUserFormat();
            }
        }
        
        if (searchResult.equals("")) {
            return Constants.MESSAGE_SEARCH_UNSUCCESSFUL;
        }
        return Constants.DISPLAY_TABLE_HEADERS+"\n"+searchResult;
    }
    
    private int[] searchOnKeyword(Map<Integer, Task> tasks, String keyword, 
            int[] foundTasks) {
        for (Task task : tasks.values()) {
            if (!task.getDone()) { 
                String taskDesc = task.getDescription();
                if (taskDesc.toLowerCase().contains(keyword.toLowerCase())) {
                    int index = task.getId()-1;
                    foundTasks[index] = Constants.INCLUDED_IN_SEARCH;
                }
            }
        }
        return foundTasks;
    }
    
    private int[] searchOnDate(Map<Integer, Task> tasks, LocalDateTime startDate, 
            LocalDateTime endDate, 
            int[] foundTasks) {
        if (isDateSearch(startDate, endDate)) {
            for (Task task : tasks.values()) {
                if (!task.getDone()) {
                    int index = task.getId()-1;
                    foundTasks[index] = isTaskInInterval(task, startDate, 
                            endDate, foundTasks[index]);
                }
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
        
        if (isFloatingTask(taskEndDate)) {
            return Constants.NOT_INCLUDED_IN_SEARCH;
        } else if (isDeadlineTask(taskStartDate)) {
            startIsAfter = taskEndDate.isAfter(searchStartDate);
            startIsOn = taskEndDate.equals(searchStartDate);
        } else {
            startIsAfter = taskStartDate.isAfter(searchStartDate);
            startIsOn = taskStartDate.equals(searchStartDate);
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
