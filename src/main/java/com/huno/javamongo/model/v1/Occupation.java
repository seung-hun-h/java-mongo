package com.huno.javamongo.model.v1;

import java.time.LocalDateTime;

public record Occupation(String name, String company, int salary, LocalDateTime joinTime) {

	@Override
	public String toString() {
		return "Occupation{" +
			"name='" + name + '\'' +
			", company='" + company + '\'' +
			", salary=" + salary +
			", joinTime=" + joinTime +
			'}';
	}
}
