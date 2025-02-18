package com.huno.javamongo.repository;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.huno.javamongo.model.v4.PersonV4;

public interface PersonV4Repository extends MongoRepository<PersonV4, ObjectId> {
	Page<PersonV4> findByName(String name, PageRequest pageRequest);

	Page<PersonV4> findAllByName(String name, PageRequest pageRequest);

	void deleteAllByName(String name);
}
