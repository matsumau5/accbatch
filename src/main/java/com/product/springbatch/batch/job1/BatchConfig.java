package com.product.springbatch.batch.job1;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<DocumentInfo> reader() {
        FlatFileItemReader<DocumentInfo> reader = new FlatFileItemReader<DocumentInfo>();
        reader.setResource(new ClassPathResource("input/input01.csv"));
        reader.setLineMapper(new DefaultLineMapper<DocumentInfo>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[]{"id", "year", "month", "documnetId", "amount"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<DocumentInfo>() {{
                setTargetType(DocumentInfo.class);
            }});
        }});
        return reader;
    }

    @Bean
    public DocumentItemProcessor processor() { return new DocumentItemProcessor();}

    @Bean
    public FlatFileItemWriter<DocumentInfo> csvWriter() {
        FlatFileItemWriter<DocumentInfo> csvWriter = new FlatFileItemWriter<>();
        csvWriter.setResource(new FileSystemResource("output/output01.csv"));
        csvWriter.setLineAggregator(item -> {
           StringBuilder sb = new StringBuilder();
           sb.append(item.getId());
           sb.append(",");
           sb.append(item.getYear());
           sb.append(",");
            sb.append(item.getMonth());
            sb.append(",");
            sb.append(item.getAmount().toString());
            return sb.toString();
        });
        return csvWriter;
    }

    @Bean
    public JobExecutionListener listener() { return new JobStartEndListener();}

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<DocumentInfo, DocumentInfo>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(csvWriter())
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .<DocumentInfo, DocumentInfo>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(csvWriter())
                .build();
    }

    @Bean
    public Job testJob() {
        return jobBuilderFactory.get("testJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .flow(step1())
                .next(step2())
                .end()
                .build();
    }
}
