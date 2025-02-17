package com.huno.javamongo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

@Profile("v3")
@Configuration
public class MongoConfigurationV3 {
	private final String uri;
	private final String database;

	public MongoConfigurationV3(
		@Value("${mongo.uri}")
		String uri,
		@Value("${mongo.database}")
		String database) {
		this.uri = uri;
		this.database = database;
	}

	@Bean
	public MongoClient mongoClient() {
		return MongoClients.create(uri);
	}

	@Bean
	public MongoDatabase mongoDatabase(MongoClient mongoClient) {
		return mongoClient.getDatabase(database);
	}

	@Bean
	public MongoTemplate mongoTemplate() {
		return new MongoTemplate(mongoClient(), database);
	}
}
