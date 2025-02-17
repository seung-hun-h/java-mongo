package com.huno.javamongo.configuration;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

@Profile("v1")
@Configuration
public class MongoConfigurationV1 {
	private final String uri;
	private final String database;

	public MongoConfigurationV1(
		@Value("${mongo.uri}")
		String uri,
		@Value("${mongo.database}")
		String database) {
		this.uri = uri;
		this.database = database;
	}

	@Bean
	public MongoClient mongoClient() {
		CodecRegistry customRegistry = CodecRegistries.fromRegistries(
			CodecRegistries.fromCodecs(new OccupationCodec(), new PersonCodec(new OccupationCodec())),
			MongoClientSettings.getDefaultCodecRegistry()
		);

		MongoClientSettings settings = MongoClientSettings.builder()
			.codecRegistry(customRegistry)
			.applyConnectionString(new ConnectionString(uri))
			.build();

		return MongoClients.create(settings);
	}

	@Bean
	public MongoDatabase mongoDatabase(MongoClient mongoClient) {
		// 사용하려는 데이터베이스 이름 지정
		return mongoClient.getDatabase(database);
	}
}
