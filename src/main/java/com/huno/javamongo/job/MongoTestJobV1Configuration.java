package com.huno.javamongo.job;

import static com.mongodb.client.model.Filters.*;

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
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.PlatformTransactionManager;

import com.huno.javamongo.model.v1.Occupation;
import com.huno.javamongo.model.v1.Person;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Profile("v1")
@Configuration
public class MongoTestJobV1Configuration {
	private final JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;
	private final MongoDatabase mongoDatabase;

	public MongoTestJobV1Configuration(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, MongoDatabase mongoDatabase) {
		this.jobRepository = jobRepository;
		this.platformTransactionManager = platformTransactionManager;
		this.mongoDatabase = mongoDatabase;
	}

	@Bean
	public Job mongoTestJobV1() {
		return new JobBuilder("mongoTestJobV1", jobRepository)
			.start(new StepBuilder("mongoTestStepV1", jobRepository)
				.tasklet(
					(contribution, chunkContext) -> {
						System.out.println("===mongoTestJobV1===");

						Person person = new Person("seunghun", 30, List.of(new Occupation("developer", "google", 10, LocalDateTime.now())));

						MongoCollection<Person> peopleCollection = mongoDatabase.getCollection("people", Person.class);

						peopleCollection.insertOne(person);
						System.out.printf("%s inserted\n", person);

						peopleCollection.find(eq("name", "seunghun")).forEach(p -> {
							System.out.printf("%s found\n", p);
						});

						peopleCollection.deleteMany(eq("name", "seunghun"));

						return RepeatStatus.FINISHED;
					}, platformTransactionManager
				)
				.build())
			.incrementer(new RunIdIncrementer())
			.build();
	}
}
