package com.luizq.sincronizacaoreceita;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author Luiz Quirino
 * DESCRIÇÃO DA CLASSE
 *  - ConfiguraBatch.java é a classe main.
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class SincronizacaoReceitaApplication {

	//TODO: Automatizar a execução do programa para dias uteis e horários pré-definidos.

	public static String fileName = null;

	public static void main(String[] args) {
		try {
			fileName = args[0];
			System.exit(SpringApplication.exit(SpringApplication.run(SincronizacaoReceitaApplication.class, args)));
		} catch (Exception e) {
			System.out.println("\n\n[ERRO] Você deve inserir Nome do Arquivo como argumento:\n*** java -jar .\\target\\sincronizacaoreceita-0.0.1-SNAPSHOT.jar pathCompleto\\filename.csv ***\n");
			System.exit(0);
		}

	}

}
