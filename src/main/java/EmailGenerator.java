import com.github.javafaker.Faker;
import lombok.Builder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;

@Builder
public class EmailGenerator implements Callable<Long> {
    private static final Faker faker = new Faker();
    private Integer numEmails;
    private Integer numThreads;
    private Integer taskId;

    // Generates numEmails or 100,000 fake email addresses and writes them to a text file with prefix database_ in tmpdir
    @Override
    public Long call() throws Exception {
        if (numEmails == 0) {
            numEmails = 100000;
        }
        final long currentThreadId = Thread.currentThread().getId() % numThreads;
        try (final FileWriter fileWriter = new FileWriter(new File(System.getProperty("java.io.tmpdir"),
                "database_" + currentThreadId + ".txt"), true)) {
            final StringBuilder sb = new StringBuilder();
            IntStream.range(0, numEmails).mapToObj((i) -> faker.internet().emailAddress()).forEach(current -> sb.append(current).append("\n"));
            fileWriter.write(sb.toString());
        } catch (IOException e) {
            System.err.println("IO failure due to " + e.getMessage() + ". Ignoring anyway.");
        }
        System.out.println("Completed task ID " + taskId + " in thread id " + currentThreadId);
        return currentThreadId;
    }
}
