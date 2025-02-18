package com.huno.javamongo.job;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import com.huno.javamongo.model.v4.OccupationV4;
import com.huno.javamongo.model.v4.PersonV4;
import com.huno.javamongo.repository.PersonV4Repository;

@Profile("v4")
@Configuration
public class MongoTestJobV4Configuration {
	private final JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;
	private final PersonV4Repository personRepository;

	public MongoTestJobV4Configuration(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, PersonV4Repository personRepository) {
		this.jobRepository = jobRepository;
		this.platformTransactionManager = platformTransactionManager;
		this.personRepository = personRepository;
	}

	@Bean
	public Job mongoTestJobV4() {
		return new JobBuilder("mongoTestJobV4", jobRepository)
			.start(saveStep())
			.next(deleteStep())
			.incrementer(new RunIdIncrementer())
			.build();
	}

	private Step saveStep() {
		return new StepBuilder("saveStep", jobRepository)
			.<PersonV4, PersonV4>chunk(1, platformTransactionManager)
			.reader(listItemReader())
			.writer(repositoryItemWriter())
			.build();
	}

	private ListItemReader<PersonV4> listItemReader() {
		PersonV4 person1 = new PersonV4("seunghun", 30, List.of(new OccupationV4("developer", "google", 10, LocalDateTime.now())));
		PersonV4 person2 = new PersonV4("seunghun", 32, List.of(new OccupationV4("developer", "google", 10, LocalDateTime.now())));
		return new ListItemReader<>(List.of(person1, person2));
	}

	private RepositoryItemWriter<PersonV4> repositoryItemWriter() {
		RepositoryItemWriter<PersonV4> writer = new RepositoryItemWriter<>();
		writer.setRepository(personRepository);
		writer.setMethodName("save");
		return writer;
	}

	private Step deleteStep() {
		return new StepBuilder("deleteStep", jobRepository)
			.<PersonV4, PersonV4>chunk(1, platformTransactionManager)
			.reader(repositoryItemReader())
			.writer(repositoryDeleteItemWriter())
			.build();
	}

	private RepositoryItemReader<PersonV4> repositoryItemReader() {
		RepositoryItemReader<PersonV4> reader = new RepositoryItemReader<>();
		reader.setRepository(personRepository);
		reader.setMethodName("findByName");
		reader.setPageSize(10);
		reader.setArguments(List.of("seunghun"));
		reader.setSort(Collections.singletonMap("id", Sort.Direction.ASC));
		return reader;
	}

	private ItemWriter<PersonV4> repositoryDeleteItemWriter() {
		return items -> {
			items.forEach(item -> {
				personRepository.delete(item);
				System.out.println("deleted: " + item);
			});
		};
	}

}
