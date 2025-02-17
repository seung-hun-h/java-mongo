package com.huno.javamongo.job;

import static org.springframework.data.mongodb.core.query.Query.*;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.Document;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.transaction.PlatformTransactionManager;

import com.huno.javamongo.model.OccupationV2;
import com.huno.javamongo.model.PersonV2;

@Configuration
public class MongoTestJobV2Configuration {
	private final JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;
	private final MongoOperations mongoOperations;

	public MongoTestJobV2Configuration(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, MongoOperations mongoOperations) {
		this.jobRepository = jobRepository;
		this.platformTransactionManager = platformTransactionManager;
		this.mongoOperations = mongoOperations;
	}

	@Bean
	public Job mongoTestJobV2() {
		return new JobBuilder("mongoTestJobV2", jobRepository)
			.start(new StepBuilder("mongoTestStepV2", jobRepository)
				.tasklet(
					(contribution, chunkContext) -> {
						System.out.println("===mongoTestJobV2===");

						PersonV2 person = new PersonV2("seunghun", 30, List.of(new OccupationV2("developer", "google", 10, LocalDateTime.now())));

						mongoOperations.insert(person);
						System.out.printf("%s inserted\n", person);

						mongoOperations.find(query(Criteria.where("name").is("seunghun")), PersonV2.class).forEach(p -> {
							System.out.printf("%s found\n", p);
						});

						mongoOperations.remove(query(Criteria.where("name").is("seunghun")), PersonV2.class);

						return RepeatStatus.FINISHED;
					}, platformTransactionManager
				)
				.build())
			.incrementer(new RunIdIncrementer())
			.build();
	}


}
