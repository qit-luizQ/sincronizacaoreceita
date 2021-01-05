package com.luizq.sincronizacaoreceita.config;

import java.time.LocalDateTime;

import com.luizq.sincronizacaoreceita.model.ContaCorrente;
import com.luizq.sincronizacaoreceita.processor.ContaProcessor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Configuration
@EnableBatchProcessing
public class ConfiguraBatch {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    

    @Bean
    public FlatFileItemReader<ContaCorrente> reader(){
        return new FlatFileItemReaderBuilder<ContaCorrente>()
                    .name("contaCSVReader")
                    .resource( new ClassPathResource("dados_conta.csv"))
                    .delimited()
                    .names(new String[] {"agencia","conta","saldo","status"})
                    .fieldSetMapper(new BeanWrapperFieldSetMapper<ContaCorrente>(){{
                        setTargetType(ContaCorrente.class);
                    }})
                    .build();
    }

    @Bean
    public ContaProcessor process(){
        return new ContaProcessor();
    }

    @Bean
    public ItemWriter<ContaCorrente> writer() {
        Resource outputResource =
            new FileSystemResource("output-" + LocalDateTime.now() + ".csv");
        // Create writer instance
        FlatFileItemWriter<ContaCorrente> writer = new FlatFileItemWriter<>();
    
        // Set output file location
        writer.setResource(outputResource);
    
        // All job repetitions should "append" to same output file
        writer.setAppendAllowed(true);
    
        // Name field values sequence based on object properties
        writer.setLineAggregator(
            new DelimitedLineAggregator<>() {
              {
                setDelimiter(";");
                setFieldExtractor(
                    new BeanWrapperFieldExtractor<>() {
                      {
                        setNames(new String[] {"agencia", "conta","saldo","status"});
                      }
                    });
              }
            });
        return writer;
      }

    @Bean
    public Job lerContaCSV(Step step1){
        return jobBuilderFactory.get("lerContaCSV")
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(ItemWriter<ContaCorrente> writer){
        return stepBuilderFactory.get("step1").<ContaCorrente,ContaCorrente>chunk(5)
                .reader(reader())
                .processor(process())
                .writer(writer())
                .build();
    }
}
