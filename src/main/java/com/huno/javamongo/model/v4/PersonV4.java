package com.huno.javamongo.model.v4;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "people")
public class PersonV4 {
	@Id
	private ObjectId id;
	private String name;
	private int age;
	private List<OccupationV4> occupations;

	public PersonV4(String name, int age, List<OccupationV4> occupations) {
		this.name = name;
		this.age = age;
		this.occupations = occupations;
	}

	public ObjectId getId() {
		return id;
	}
	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public List<OccupationV4> getOccupations() {
		return occupations;
	}

	@Override
	public String toString() {
		return "Person{" +
			"name='" + name + '\'' +
			", age=" + age +
			", occupations=" + occupations +
			'}';
	}
}
