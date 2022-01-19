package pfk.tws.ncfdilution;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NoOpItemWriter implements ItemWriter<Object> {
    @Override
    public void write(List<?> items) throws Exception {

    }
}
