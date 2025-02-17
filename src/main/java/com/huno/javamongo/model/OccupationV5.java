package com.huno.javamongo.model;

import java.time.LocalDateTime;

public class OccupationV5 {
	private String id;
	private String name;
	private String company;
	private int salary;
	private LocalDateTime joinTime;

	public OccupationV5(String name, String company, int salary, LocalDateTime joinTime) {
		this.name = name;
		this.company = company;
		this.salary = salary;
		this.joinTime = joinTime;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCompany() {
		return company;
	}

	public int getSalary() {
		return salary;
	}

	public LocalDateTime getJoinTime() {
		return joinTime;
	}

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
