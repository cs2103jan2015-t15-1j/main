# TaskWaltz
**A command line based to-do manager**  
CS2103 project by group t15-1j

## About
TaskWaltz is a command line based to-do manager developed as a project in the course CS2103 Software Engineering at National University of Singapore.

## User Guide

### Requirements
Java 1.8

### Usage
java -jar TaskWaltz.jar

### Commands

####Add Task
**Event:** add Breakfast at Redhill start 8 end 11:30  
**To-do:** add Make hairdresser appointment  
**Deadline:** add Submit paper due 23/9 17:30

####Display Task
**Display tasks in the next 7 days:** display  
**Display today’s tasks:** display today  
**Display all tasks:** display all

####Edit Task
**Edit description of task #3:** edit 3 desc Execute plan B not A.  
**Edit start of task #11:** edit 11 start 1/5/2016  
**Edit end of task #11:** edit 11 end 3/5/2016

####Search Task
**Search tasks by description:** search foodRecipe  
**Search tasks before:** search before 5/1 20:00  
**Search tasks after:** search after 4/1 22:00

####Mark as done
**Mark task #4 as done:** done 4  
**Mark tasks #4, 6, 9 as done:** done 4 6 9

####Delete Task
**Delete task #4 as done:** delete 4  
**Delete tasks #4, 6, 9 as done:** delete 4 6 9

####Exit TaskWaltz
**Exit program:** exit
