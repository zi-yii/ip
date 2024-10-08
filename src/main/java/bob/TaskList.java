package bob;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import bob.task.Task;

/**
 * Manages a list of tasks.
 * The TaskList class provides functionality to manage a collection of Task objects,
 * such as adding and deleting tasks, retrieving tasks, and printing the list of tasks.
 */
public class TaskList {

    /**
     * A DateTimeFormatter to format dates in a readable format for the user.
     */
    private static final DateTimeFormatter FRIENDLY_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy");
    private final List<Task> tasks;

    /**
     * Initialises an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Initialises a TaskList with a given list of tasks.
     *
     * @param tasks A list of Task objects.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Checks if the task list is empty.
     *
     * @return true if the task list is empty, false otherwise.
     */
    boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Retrieves the list of tasks.
     *
     * @return A List of Task objects.
     */
    List<Task> getTasks() {
        return tasks;
    }

    /**
     * Gets the number of tasks in the task list.
     *
     * @return Number of tasks in the task list.
     */
    int getNumTasks() {
        return tasks.size();
    }

    /**
     * Adds a task to the task list.
     *
     * @param task The task to be added to the task list.
     */
    void addTask(Task task) {
        assert task != null : "Task to be added should not be null";
        tasks.add(task);
    }

    /**
     * Deletes a task from the task list using task number.
     *
     * @param taskNum The 1-based index of the task to be deleted.
     * @throws BobException If task number is invalid.
     */
    void delTask(int taskNum) throws BobException {
        try {
            tasks.remove(taskNum - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new BobException("The task number provided is invalid.");
        }
    }

    /**
     * Gets a Task in the task list using task number.
     *
     * @param taskNum The 1-based index of the task to retrieve.
     * @return Task object at the specified index.
     * @throws IndexOutOfBoundsException If the index is out of range.
     */
    Task getTask(int taskNum) throws BobException {
        try {
            return tasks.get(taskNum - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new BobException("The task number provided is invalid.");
        }
    }

    /**
     * Builds a formatted string of tasks from a given list of Task objects.
     *
     * @param tasks The list of Task objects to format.
     * @return A formatted string representation of the tasks.
     */
    private String buildTaskListString(List<Task> tasks) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i + 1).append(". ").append(tasks.get(i)).append("\n");
        }
        return sb.toString();
    }

    /**
     * Prints all tasks in the task list.
     *
     * @return A formatted string of all tasks.
     */
    String printTasks() {
        return buildTaskListString(tasks);
    }

    /**
     * Prints tasks that occur on a specific date.
     *
     * @param dateStr The date to filter relevant tasks, following "yyyy-MM-dd" format
     * @return A formatted string of relevant tasks.
     * @throws BobException If the date format is invalid.
     */
    String printRelevantTasksByDate(String dateStr) throws BobException {
        LocalDate date = Parser.parseDate(dateStr);

        List<Task> relevantTasks = tasks.stream()
                .filter(task -> task.isRelevant(date))
                .collect(Collectors.toList());

        String taskListStr = buildTaskListString(relevantTasks);
        String summary = "Total number of relevant tasks for " + date.format(FRIENDLY_DATE_FORMATTER) + ": "
                + relevantTasks.size();

        return taskListStr + summary;
    }

    /**
     * Prints tasks that contain a keyword or phrase within its task description.
     *
     * @param keyword The keyword or phrase to filter matching tasks.
     * @return A formatted string of matching tasks.
     * @throws BobException If the keyword is empty.
     */
    String printTasksByKeyword(String keyword) throws BobException {
        if (keyword.trim().isEmpty()) {
            throw new BobException("Please provide a keyword or a phrase.");
        }

        List<Task> matchingTasks = tasks.stream()
                .filter(task -> task.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());

        String taskListStr = buildTaskListString(matchingTasks);
        String summary = "Total number of tasks containing \"" + keyword.toLowerCase() + "\": " + matchingTasks.size();

        return taskListStr + summary;
    }

    /**
     * Sorts the task list using the provided comparator.
     *
     * @param comparator The comparator to use for sorting.
     * @return A new sorted TaskList.
     */
    public TaskList sort(Comparator<Task> comparator) {
        // Create a copy of the task list to avoid modifying the original
        List<Task> sortedTasks = new ArrayList<>(tasks);
        sortedTasks.sort(comparator);
        return new TaskList(sortedTasks);
    }

    /**
     * Sorts tasks by description in alphabetical order.
     *
     * @return A new sorted TaskList by description.
     */
    public TaskList sortTasksByDescription() {
        return sort(BobComparator.DESCRIPTION_COMPARATOR);
    }

    /**
     * Sorts tasks by date.
     *
     * @return A new sorted TaskList by date.
     */
    public TaskList sortTasksByDate() {
        return sort(BobComparator.DATE_COMPARATOR);
    }

}

