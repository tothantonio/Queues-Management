package business_logic;

import model.Server;
import model.Task;
import util.SelectionPolicy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class SimulationManager implements Runnable {
    private Integer currentTime = 0;
    private final Scheduler scheduler;
    private Boolean noWork = false;
    private File stateFile;
    private BlockingQueue<Task> tasks;
    private float totalServiceTime;
    private Integer peakHour;
    private Integer clientsPeakHour;
    private final Consumer<String> logConsumer;
    private final Consumer<String> statsConsumer;
    private final Integer timeLimit;
    private final Integer maxProcessingTime;
    private final Integer minProcessingTime;
    private final Integer minArrivalTime;
    private final Integer maxArrivalTime;
    private final Integer numberOfServers;
    private final Integer numberOfClients;

    public SimulationManager(ArrayList<Integer> inputData, SelectionPolicy selectionPolicy, Consumer<String> logConsumer, Consumer<String> statsConsumer) {
        this.logConsumer = logConsumer;
        this.statsConsumer = statsConsumer;
        this.peakHour = this.clientsPeakHour = 0;
        this.totalServiceTime = 0;
        this.numberOfClients = inputData.get(0);
        this.numberOfServers = inputData.get(1);
        this.timeLimit = inputData.get(2);
        this.minArrivalTime = inputData.get(3);
        this.maxArrivalTime = inputData.get(4);
        this.minProcessingTime = inputData.get(5);
        this.maxProcessingTime = inputData.get(6);
        this.scheduler = new Scheduler(numberOfServers);
        this.scheduler.changeStrategy(selectionPolicy);
        this.stateFile = initializeOutputFile();
    }

    private File initializeOutputFile() {
        if (this.stateFile != null && !this.stateFile.delete()) {
            System.out.println("<ERROR> stateOfServers.txt was not recreated");
        }
        this.stateFile = new File("stateOfServers.txt");
        return stateFile;
    }

    @Override
    public synchronized void run() {
        this.stateFile = initializeOutputFile();
        tasks = this.generateRandomTasks(this.numberOfClients);
        this.printTasks(tasks);
        while (currentTime <= timeLimit && !noWork) {
            Integer numberOfLeftServers = numberOfServers;
            for (Task task : tasks) {
                if (task.getArrivalTime().compareTo(currentTime) <= 0) {
                    this.scheduler.dispatchTask(task);
                    if (!tasks.remove(task)) {
                        logConsumer.accept("<ERROR>Did not remove task: " + task);
                    }
                    numberOfLeftServers--;
                    if (numberOfLeftServers == 0) {
                        break;
                    }
                }
            }
            printStateOfServers(currentTime);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logConsumer.accept("Simulation interrupted");
            }
            currentTime++;
            if (currentTime > timeLimit) {
                this.scheduler.forceStopServers();
            }
            if (tasks.isEmpty()) {
                noWork = this.scheduler.stopServers();
            }
        }
        this.printMetadata();
        logConsumer.accept("<DEBUG>Simulation finished");
    }

    private void printMetadata() {
        try {
            FileWriter fileOut = new FileWriter(stateFile, true);
            double x = 0.0;
            for (Server server : this.scheduler.getServerList()) {
                x += server.getWaitingPeriod().doubleValue();
            }
            String stats = "Average overall waiting time: " + x / this.numberOfClients + "\n" +
                    "Average overall service time: " + (this.totalServiceTime / this.numberOfClients) + "\n" +
                    "Peak hour: " + this.peakHour + "\n";
            fileOut.write(stats);
            fileOut.flush();
            fileOut.close();
            statsConsumer.accept(stats);
        } catch (IOException e) {
            logConsumer.accept("WHERE FILE?");
        }
    }

    private void printTasks(BlockingQueue<Task> tasks) {
        File path = new File("tasks.txt");
        try {
            FileWriter fileOut = new FileWriter(path);
            fileOut.write(tasks.size() + " generated tasks:\n");
            for (Task task : tasks) {
                fileOut.write(task.toString() + "\n");
            }
            fileOut.flush();
            fileOut.close();
        } catch (IOException e) {
            logConsumer.accept("WHERE FILE?");
        }
    }

    private void printStateOfServers(Integer currentTime) {
        try {
            FileWriter fileOut = new FileWriter(stateFile, true);
            StringBuilder logBuilder = new StringBuilder();
            logBuilder.append("Current time: ").append(currentTime).append("\nWaiting people:\n");
            for (Task task : tasks) {
                logBuilder.append(">>").append(task.toString()).append("\n");
            }
            logBuilder.append("\nState of servers:\n");
            int clientsPerHour = 0;
            for (Server server : this.scheduler.getServerList()) {
                clientsPerHour += server.getTaskQ().size();
                if (server.getTaskQ().isEmpty()) {
                    logBuilder.append("server: <").append(server.getServerName()).append("> is closed\n");
                } else {
                    logBuilder.append("server: <").append(server.getServerName()).append("> has amount of work: ").append(server.getTaskQ().size()).append("\n");
                }
                for (Task task : server.getTaskQ()) {
                    logBuilder.append("\t").append(task.toString()).append("\n");
                }
            }
            if (clientsPerHour > clientsPeakHour) {
                this.clientsPeakHour = clientsPerHour;
                this.peakHour = currentTime;
            }
            logBuilder.append("\n\n");
            fileOut.write(logBuilder.toString());
            fileOut.flush();
            fileOut.close();
            logConsumer.accept(logBuilder.toString());
        } catch (IOException e) {
            logConsumer.accept("WHERE FILE?");
        }
    }

    private BlockingQueue<Task> generateRandomTasks(Integer N) {
        Random random = new Random();
        LinkedBlockingQueue<Task> arrayList = new LinkedBlockingQueue<>();
        for (int i = 0; i < N; i++) {
            Integer aTime = random.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime;
            Integer sTime = random.nextInt(maxProcessingTime - minProcessingTime + 1) + minProcessingTime;
            arrayList.add(new Task(i + 1, aTime, sTime));
            this.totalServiceTime += sTime;
        }
        return arrayList;
    }
}