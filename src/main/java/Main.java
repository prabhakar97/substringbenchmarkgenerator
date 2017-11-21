import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class Main {
    public static void main(String... args) {
        final Callable<List<String>> emailGenerator = EmailGenerator.builder().numEmails(10000).build();
        final ExecutorService executorService = Executors.newFixedThreadPool(8);
        final List<Future<List<String>>> futures = new ArrayList<>();
        IntStream.range(0, 10000).forEach(x -> futures.add(executorService.submit(emailGenerator)));
        IntStream.range(0, 10000).forEach(i -> {
            try (final FileWriter fileWriter = new FileWriter("/tmp/database.txt", true)) {
                final List<String> result = futures.get(i).get();
                final StringJoiner sj = new StringJoiner("\n");
                result.forEach(email -> sj.add(email));
                fileWriter.write(sj.toString());
                System.out.println("Wrote " + i * 10000 + " emails");
            } catch (InterruptedException | ExecutionException | IOException e) {
                System.err.println("Something is dead.");
                System.err.println(e.getMessage());
            }

        });
    }
}
