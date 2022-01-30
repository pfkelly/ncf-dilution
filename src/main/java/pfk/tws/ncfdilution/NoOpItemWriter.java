package pfk.tws.ncfdilution;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class NoOpItemWriter implements ItemWriter<Object> {
    @Override
    public void write(@NonNull List<?> items) throws Exception {
        log.info("how to write?");
        items.forEach(c -> log.info(c.toString()));
    }
}
