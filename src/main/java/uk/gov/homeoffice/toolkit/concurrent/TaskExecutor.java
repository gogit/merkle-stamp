package uk.gov.homeoffice.toolkit.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TaskExecutor<R> {

    /**
     *
     * @param tasks
     * @param result
     * @throws Exception
     */
    public void execute(final List<Callable<R>> tasks, final Handler<R> result) throws Exception {
        Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        CompletionService<R> ecs = new ExecutorCompletionService<R>(executor);
        int n = tasks.size();
        List<Future<R>> futures = new ArrayList<>(n);
        for (Callable<R> task : tasks) {
            futures.add(ecs.submit(task));
        }
        for (int i = 0; i < n; ++i) {
            R r = ecs.take().get();
            if (r != null) {
                result.result(r);
            }
        }
    }
}
