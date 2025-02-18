package com.huno.javamongo.model.v1;

import java.time.LocalDateTime;

public record Occupation(String name, String company, int salary, LocalDateTime joinTime) {
}
