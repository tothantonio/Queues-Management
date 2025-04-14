package business_logic;

import model.Server;
import model.Task;
import util.Strategy;

import java.util.List;

public class TimeStrategy implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task t) {
        Server bestServer = servers.getFirst();
        int bestWaitingPeriod = Integer.MAX_VALUE;
        Integer wait;
        for (Server server : servers) {
            wait = 0;
            for (Task task : server.getTaskQ())
                wait += task.getServiceTime();
            if (wait < bestWaitingPeriod) {
                bestWaitingPeriod = wait;
                bestServer = server;
            }
        }
        bestServer.addTask(t);
    }
}