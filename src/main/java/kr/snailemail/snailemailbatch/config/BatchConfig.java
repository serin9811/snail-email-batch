package kr.snailemail.snailemailbatch.config;

import kr.snailemail.snailemailbatch.entity.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job exampleJob() throws Exception {
        return jobBuilderFactory.get("exampleJob")
                .start(exampleStep()).build();
    }

    @Bean
    @JobScope
    public Step exampleStep() throws Exception {
        return stepBuilderFactory.get("exampleStep")
                .<Email, Email> chunk(10)
                .reader(reader(null))
                .processor(processor(null))
                .writer(writer(null))
                .build();
    }

    @Bean
    @JobScope
    public JpaPagingItemReader<Email> reader(@Value("#{jobParameters[requestDate]}") String requestDate) throws Exception {
        log.info("==> reader value : " + requestDate);
        Map<String, Object> parameterValues = new HashMap<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(requestDate, fmt);
        parameterValues.put("sendDate", dateTime);

        return new JpaPagingItemReaderBuilder<Email>()
                .pageSize(10)
                .parameterValues(parameterValues)
                .queryString("SELECT e FROM Email e WHERE e.sendDate >= :sendDate")
                .entityManagerFactory(entityManagerFactory)
                .name("JpaPagingItemReader")
                .build();
    }
    @Bean
    @StepScope
    public ItemProcessor<Email, Email> processor(@Value("#{jobParameters[requestDate]}") String requestDate) throws Exception {
        return new ItemProcessor<Email, Email>() {
            @Override
            public Email process(Email item) throws Exception {
                log.info("==> processor Email : " + item);
                log.info("==> processor value : " + requestDate);

                // 가공할 데이터가 있으면 여기서 가공하면 된다.
                return item;
            }
        };
    }

    @Bean
    @StepScope
    public JpaItemWriter<Email> writer(@Value("#{jobParameters[requestDate]}") String requestDate) {
        log.info("==> writer value: " + requestDate);

        return new JpaItemWriterBuilder<Email>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

}
