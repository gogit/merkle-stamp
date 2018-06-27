package uk.gov.homeoffice.toolkit.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class BenchMark {

    public Duration execute(Consumer consumer){
        LocalDateTime start = LocalDateTime.now();
        consumer.accept(null);
        LocalDateTime end = LocalDateTime.now();
        return Duration.between(start, end);
    }
}
