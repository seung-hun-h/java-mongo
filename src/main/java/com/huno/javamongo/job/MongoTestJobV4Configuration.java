package com.huno.javamongo.job;

import static org.springframework.data.mongodb.core.query.Query.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoCursorItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.transaction.PlatformTransactionManager;

import com.huno.javamongo.model.OccupationV4;
import com.huno.javamongo.model.PersonV4;

@Configuration
public class MongoTestJobV4Configuration {
	private final JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;
	private final MongoOperations mongoOperations;

	public MongoTestJobV4Configuration(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, MongoOperations mongoOperations) {
		this.jobRepository = jobRepository;
		this.platformTransactionManager = platformTransactionManager;
		this.mongoOperations = mongoOperations;
	}

	@Bean
	public Job mongoTestJobV3() {
		return new JobBuilder("mongoTestJobV3", jobRepository)
			.start(saveStep())
			.start(deleteStep())
			.incrementer(new RunIdIncrementer())
			.build();
	}

	private Step saveStep() {
		return new StepBuilder("saveStep", jobRepository)
			.<PersonV4, PersonV4>chunk(1, platformTransactionManager)
			.reader(listItemReader())
			.writer(mongoItemWriter())
			.build();
	}

	private ListItemReader<PersonV4> listItemReader() {
		PersonV4 person = new PersonV4("seunghun", 30, List.of(new OccupationV4("developer", "google", 10, LocalDateTime.now())));
		return new ListItemReader<>(List.of(person));
	}

	private MongoItemWriter<PersonV4> mongoItemWriter() {
		MongoItemWriter<PersonV4> writer = new MongoItemWriter<>();
		writer.setTemplate(mongoOperations);
		writer.setCollection("people");
		return writer;
	}

	private Step deleteStep() {
		return new StepBuilder("deleteStep", jobRepository)
			.<PersonV4, PersonV4>chunk(1, platformTransactionManager)
			.reader(mongoItemReader())
			.writer(mongoDeleteItemWriter())
			.build();
	}

	private MongoCursorItemReader<PersonV4> mongoItemReader() {
		MongoCursorItemReader<PersonV4> reader = new MongoCursorItemReader<>();
		reader.setQuery(query(Criteria.where("name").is("seunghun")));
		reader.setTemplate(mongoOperations);
		reader.setTargetType(PersonV4.class);
		return reader;
	}

	private ItemWriter<PersonV4> mongoDeleteItemWriter() {
		return items -> {
			items.forEach(item -> {
				System.out.printf("%s deleted\n", item);
				mongoOperations.remove(query(Criteria.where("name").is(item.getName())), PersonV4.class);
			});
		};
	}
}
