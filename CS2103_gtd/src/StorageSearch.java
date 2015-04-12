import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

//@author A0135280M
public class StorageSearch {
    
    public String search(ArrayList<Task> taskList, Task searchObj) {
        Collections.sort(taskList);
        int[] foundTasks = new int[taskList.size()];
        
        foundTasks = searchOnKeyword(taskList, searchObj, foundTasks);
        foundTasks = searchOnDate(taskList, searchObj, foundTasks);

        String searchResult = "";
        for (int i=0; i<foundTasks.length; i++) {
            if (foundTasks[i] == Constants.INCLUDED_IN_SEARCH) {
                Task newTask = taskList.get(i);
                searchResult += "\n" + newTask.getUserFormat();
            }
        }
        return searchResult;
    }
    
    private int[] searchOnKeyword(ArrayList<Task> tasks, Task searchObj, 
            int[] foundTasks) {
        String keyword = searchObj.getDescription();
        for (int i=0; i<tasks.size(); i++) {
            Task task = tasks.get(i);
            String taskDesc = task.getDescription();
            if (taskDesc.toLowerCase().contains(keyword.toLowerCase())) {
                foundTasks[i] = Constants.INCLUDED_IN_SEARCH;
            }
        }
        return foundTasks;
    }
    
    private int[] searchOnDate(ArrayList<Task> tasks, Task searchObj, int[] foundTasks) {
        LocalDateTime startDate = searchObj.getStartDateTime();
        LocalDateTime endDate = searchObj.getEndDateTime();
        if (isDateSearch(startDate, endDate)) {
            for (int i=0; i<tasks.size(); i++) {
                Task task = tasks.get(i);
                foundTasks[i] = isTaskInInterval(task, startDate, 
                        endDate, foundTasks[i]);
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
            return originalValue;
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
    
    private boolean isNotInInterval(boolean startIsAfter, boolean startIsOn, 
            boolean endIsBefore, boolean endIsOn) {
        return !((startIsAfter || startIsOn) && (endIsBefore || endIsOn));
    }

}
