package com.huno.javamongo.model.v1;

import java.util.List;

public record Person(String name, int age, List<Occupation> occupations) {

	@Override
	public String toString() {
		return "Person{" +
			"name='" + name + '\'' +
			", age=" + age +
			", occupations=" + occupations +
			'}';
	}
}
