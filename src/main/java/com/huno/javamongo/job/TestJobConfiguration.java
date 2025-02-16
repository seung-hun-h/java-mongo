package com.huno.javamongo.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TestJobConfiguration {
	private final JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;

	public TestJobConfiguration(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
		this.jobRepository = jobRepository;
		this.platformTransactionManager = platformTransactionManager;
	}

	@Bean
	public Job testJob() {
		return new JobBuilder("testJob", jobRepository)
			.start(new StepBuilder("testStep", jobRepository)
				.tasklet(
					(contribution, chunkContext) -> {
						System.out.println("Hello, World!");
						return RepeatStatus.FINISHED;
					}, platformTransactionManager
				)
				.build())
			.incrementer(new RunIdIncrementer())
			.build();
	}
}
