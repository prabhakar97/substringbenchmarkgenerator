import com.github.javafaker.Faker;
import lombok.Builder;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Setter
@Builder
public class EmailGenerator implements Callable<List<String>> {
    private static final Faker faker = new Faker();
    private int numEmails = 10000;
    @Override
    public List<String> call() throws Exception {
        return IntStream.range(0, numEmails).mapToObj((i) -> faker.internet().emailAddress()).collect(Collectors.toList());
    }
}
