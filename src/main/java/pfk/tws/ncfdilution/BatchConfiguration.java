package pfk.tws.ncfdilution;

import com.ib.client.Contract;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.BindException;

@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public NoOpItemWriter itemWriter;

    @Bean
    public FlatFileItemReader<Contract> reader() {
        return  new FlatFileItemReaderBuilder<Contract>()
                .name("contractItemReader")
                .resource(new ClassPathResource("ncf-dilution-contracts.csv"))
                .delimited()
                .names(new String[] {"right", "symbol"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Contract>() {
                    {
                    setTargetType(Contract.class);
                    }
                    @Override
                    public Contract mapFieldSet(FieldSet fs) throws BindException {
                        Contract contract = new Contract();
                        contract.right(fs.readString("right"));
                        contract.symbol(fs.readString("symbol"));
                        return contract;
                    }
                })
                .build();
    }

    @Bean
    public LoadContractItemProcessor loadContractProcessor() { return new LoadContractItemProcessor(); }


    @Bean
    public Job negativeCashFlowDilutionJob(JobCompletionNotificationListener listener, Step loadContracts) {
        return jobBuilderFactory.get("negativeCashFlowDilutionJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(loadContracts)
                .end()
                .build();
    }

    @Bean
    public Step loadContracts() {
        return stepBuilderFactory.get("loadContractsStep")
                .<Contract, Contract>chunk(10)
                .reader(reader())
                .processor(loadContractProcessor())
                .writer(itemWriter)
                .build();
    }

}
