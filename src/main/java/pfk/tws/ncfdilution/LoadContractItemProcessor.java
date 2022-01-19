package pfk.tws.ncfdilution;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import com.ib.client.Contract;


@Slf4j
public class LoadContractItemProcessor implements ItemProcessor<Contract, Contract> {
    @Override
    public Contract process(Contract item) throws Exception {
        log.info("**** Implement ContractItemProcessor*****");


        return null;
    }
}
