package com.filterisasi.filterisasi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilterisasiApplication  implements CommandLineRunner {

	private static Logger LOG = LoggerFactory
			.getLogger(FilterisasiApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(FilterisasiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		LOG.info("EXECUTING : command line runner");

		for (int i = 0; i < args.length; ++i) {
			LOG.info("args[{}]: {}", i, args[i]);
		}
	}
}
