package com.os.backend.Schedule;

import com.os.backend.Process.*;
import com.os.backend.Process.Process;

import java.util.ArrayList;
import java.util.List;

public class Priority_Pree extends SchedulingAlgo {

    public Priority_Pree() {
    }

    @Override
    public ProcessTable execute() {
        ProcessTable processTable = new ProcessTable();

        int currentTime = 0;

        while (!processesList.isEmpty()) {
            // Find the process with the highest priority that has arrived
            Process currentProcess = getHighestPriorityProcess(currentTime);

            // Check if the current process has not arrived yet
            if (currentProcess.getArrivalTime() > currentTime) {
                // Update the current time to the arrival time of the next process
                currentTime = currentProcess.getArrivalTime();
            }

            // Add event for process arrival
            processTable.addExecutionEvent(currentTime, currentProcess.getProcessNumber(), ProcessState.ARRIVED);

            // Execute the current process
            executeProcess(currentProcess, processTable, currentTime);

            // Update current time
            currentTime += currentProcess.getBurstTime();
        }


        return processTable;
    }

    private Process getHighestPriorityProcess(int currentTime) {
        Process highestPriorityProcess = null;
        for (Process process : processesList) {
            if (process instanceof PriorityProcess && process.getArrivalTime() <= currentTime) {
                if (highestPriorityProcess == null || ((PriorityProcess) process).getPriority() < ((PriorityProcess) highestPriorityProcess).getPriority()) {
                    highestPriorityProcess = process;
                }
            }
        }
        return highestPriorityProcess;
    }

    private void executeProcess(Process process, ProcessTable processTable, int startTime) {
        // Add event for process start
        processTable.addExecutionEvent(startTime, process.getProcessNumber(), ProcessState.STARTED);

        // Simulate process execution
        int endTime = startTime + process.getBurstTime();
        for (int i = startTime + 1; i <= endTime; i++) {
            processTable.addExecutionEvent(i, process.getProcessNumber(), ProcessState.RUNNING);
        }

        // Add event for process completion
        processTable.addExecutionEvent(endTime, process.getProcessNumber(), ProcessState.COMPLETED);
    }
}
