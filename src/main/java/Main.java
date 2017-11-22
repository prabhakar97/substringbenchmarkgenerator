import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class Main {
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors() * 2;
    public static void main(String... args) {
        final CompletionService<Long> completionService = new ExecutorCompletionService<>(Executors.newFixedThreadPool(NUM_THREADS));
        System.out.println("Running tasks on " + NUM_THREADS + " threads");

        // Split the 100 mil email generation into thousand tasks each generating hundred thousand
        final List<Future<Long>> futures = new ArrayList<>();
        IntStream.range(0, 1000).forEach(taskId -> futures.add(completionService.submit(
                EmailGenerator.builder().numEmails(100000).taskId(taskId).numThreads(NUM_THREADS).build()
        )));
        IntStream.range(0, 1000).forEach(i -> {
            try {
                // Block till next thread is back with result
                completionService.take();
            } catch (InterruptedException e) {
                System.err.println("Problem occured in multithreading. Error: " + e.getMessage());
            }
        });
    }
}
