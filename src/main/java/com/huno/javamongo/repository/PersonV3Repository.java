package com.huno.javamongo.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.huno.javamongo.model.v3.PersonV3;

public interface PersonV3Repository extends MongoRepository<PersonV3, ObjectId> {
	List<PersonV3> findByName(String name);

	void deleteAllByName(String name);
}
