package com.huno.javamongo.configuration;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.huno.javamongo.model.v1.Occupation;

public class OccupationCodec implements Codec<Occupation> {
	@Override
	public void encode(BsonWriter writer, Occupation occupation, EncoderContext encoderContext) {
		writer.writeStartDocument();
		writer.writeString("name", occupation.name());
		writer.writeString("company", occupation.company());
		writer.writeInt32("salary", occupation.salary());

		// LocalDateTime을 시스템 타임존 기준 밀리초로 변환하여 저장
		long millis = occupation.joinTime()
			.atZone(ZoneId.systemDefault())
			.toInstant()
			.toEpochMilli();
		writer.writeDateTime("joinTime", millis);
		writer.writeEndDocument();
	}

	@Override
	public Occupation decode(BsonReader reader, DecoderContext decoderContext) {
		reader.readStartDocument();
		String name = null;
		String company = null;
		int salary = 0;
		LocalDateTime joinTime = null;

		while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
			String fieldName = reader.readName();
			switch (fieldName) {
				case "name":
					name = reader.readString();
					break;
				case "company":
					company = reader.readString();
					break;
				case "salary":
					salary = reader.readInt32();
					break;
				case "joinTime":
					long millis = reader.readDateTime();
					joinTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
					break;
				default:
					reader.skipValue();
					break;
			}
		}
		reader.readEndDocument();
		return new Occupation(name, company, salary, joinTime);
	}

	@Override
	public Class<Occupation> getEncoderClass() {
		return Occupation.class;
	}
}

