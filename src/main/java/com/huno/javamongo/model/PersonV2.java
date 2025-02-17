package com.huno.javamongo.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "people")
public class PersonV2 {
	private String id;
	private String name;
	private int age;
	private List<OccupationV2> occupations;

	public PersonV2(String name, int age, List<OccupationV2> occupations) {
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

	public List<OccupationV2> getOccupations() {
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
