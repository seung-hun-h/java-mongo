package com.huno.javamongo.model.v5;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "people")
public class PersonV5 {
	private String id;
	private String name;
	private int age;
	private List<OccupationV5> occupations;

	public PersonV5(String name, int age, List<OccupationV5> occupations) {
		this.name = name;
		this.age = age;
		this.occupations = occupations;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public List<OccupationV5> getOccupations() {
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
