package com.luizq.sincronizacaoreceita.config;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.luizq.sincronizacaoreceita.SincronizacaoReceitaApplication;
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
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * @author Luiz Quirino
 * DESCRIÇÃO DA CLASSE
 *  - ConfiguraBatch.java é a classe para a configuração do Spring Batch.
 */

@Configuration
@EnableBatchProcessing
public class ConfiguraBatch {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    Date date = new Date();
    Format formataData = new SimpleDateFormat("YYYYMMdd");
    
  
    /**Leitura do Arquivo CSV
    TODO: Implementar tratamento de erro da seguinte forma:
        if(fileName != null){
          try{
              reader()
            }
          }Exceção{
            "Arquivo não encontrado"
          }
        }
    */
    @Bean
    public FlatFileItemReader<ContaCorrente> reader(){
        return new FlatFileItemReaderBuilder<ContaCorrente>()
                    .name("contaCSVReader")
                    .resource( new FileSystemResource(SincronizacaoReceitaApplication.fileName))
                    .delimited()
                    .delimiter(";")
                    .names(new String[] {"agencia","conta","saldo","status"})
                    .linesToSkip(1)
                    .fieldSetMapper(new BeanWrapperFieldSetMapper<ContaCorrente>(){{
                        setTargetType(ContaCorrente.class);
                    }})
                    .build();
    }

    //Processa os dados do arquivo CSV, passa os dados para ReceitaService e Devolve os dados modificados para writer() 
    @Bean
    public ContaProcessor process(){
        return new ContaProcessor();
    }

    //Escreve o arquivo com os dados processados, adicionando a coluna resultados.
    //Todos os dados dos arquivos de entrada ".csv" processados no mesmo dia serão adicionados ao fim do processamento do arquivo".csv" processado antes. 
    @Bean
    public ItemWriter<ContaCorrente> writer() {
        Resource outputResource = new FileSystemResource("enviados\\output_"+formataData.format(date)+".csv");
        FlatFileItemWriter<ContaCorrente> writer = new FlatFileItemWriter<>();
        writer.setResource(outputResource);
        writer.setAppendAllowed(true);
        writer.setLineAggregator(
            new DelimitedLineAggregator<>() {
              {
                setDelimiter(";");
                setFieldExtractor(new BeanWrapperFieldExtractor<>() {
                      {
                        setNames(new String[] {"agencia", "conta","saldo","status","resultado"});
                      }
                    });
              }
            });
        return writer;
      }
    
    //Configura Job do Batch com o Step como parâmetro
    @Bean
    public Job lerContaCSV(Step step1){
        return jobBuilderFactory.get("lerContaCSV")
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }

    //Configura Step do Batch.
    @Bean
    public Step step1(){
        return stepBuilderFactory.get("step1").<ContaCorrente,ContaCorrente>chunk(5)
                .reader(reader())
                .processor(process())
                .writer(writer())
                .build();
    }
}
