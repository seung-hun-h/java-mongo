package com.huno.javamongo.job;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.PlatformTransactionManager;

import com.huno.javamongo.model.v3.OccupationV3;
import com.huno.javamongo.model.v3.PersonV3;
import com.huno.javamongo.repository.PersonV3Repository;

@Profile("v3")
@Configuration
public class MongoTestJobV3Configuration {
	private final JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;
	private final PersonV3Repository personV3Repository;

	public MongoTestJobV3Configuration(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, PersonV3Repository personV3Repository) {
		this.jobRepository = jobRepository;
		this.platformTransactionManager = platformTransactionManager;
		this.personV3Repository = personV3Repository;
	}

	@Bean
	public Job mongoTestJobV3() {
		return new JobBuilder("mongoTestJobV3", jobRepository)
			.start(new StepBuilder("mongoTestStepV3", jobRepository)
				.tasklet(
					(contribution, chunkContext) -> {
						System.out.println("===mongoTestJobV3===");

						PersonV3 person = new PersonV3("seunghun", 30, List.of(new OccupationV3("developer", "google", 10, LocalDateTime.now())));

						personV3Repository.save(person);
						System.out.printf("%s inserted\n", person);


						personV3Repository.findByName("seunghun").forEach(p -> {
							System.out.printf("%s found\n", p);
						});

						personV3Repository.deleteAllByName("seunghun");

						return RepeatStatus.FINISHED;
					}, platformTransactionManager
				)
				.build())
			.incrementer(new RunIdIncrementer())
			.build();
	}


}
