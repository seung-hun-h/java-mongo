package com.huno.javamongo.configuration;

import java.util.ArrayList;
import java.util.List;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import com.huno.javamongo.model.v1.Occupation;
import com.huno.javamongo.model.v1.Person;

public class PersonCodec implements Codec<Person> {

	// Occupation 처리를 위한 Codec
	private final Codec<Occupation> occupationCodec;

	public PersonCodec(Codec<Occupation> occupationCodec) {
		this.occupationCodec = occupationCodec;
	}

	@Override
	public void encode(BsonWriter writer, Person person, EncoderContext encoderContext) {
		writer.writeStartDocument();
		writer.writeString("name", person.name());
		writer.writeInt32("age", person.age());

		// occupations 배열 작성
		writer.writeName("occupations");
		writer.writeStartArray();
		if (person.occupations() != null) {
			for (Occupation occupation : person.occupations()) {
				occupationCodec.encode(writer, occupation, encoderContext);
			}
		}
		writer.writeEndArray();
		writer.writeEndDocument();
	}

	@Override
	public Person decode(BsonReader reader, DecoderContext decoderContext) {
		reader.readStartDocument();
		String name = null;
		int age = 0;
		List<Occupation> occupations = new ArrayList<>();

		while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
			String fieldName = reader.readName();
			switch (fieldName) {
				case "name":
					name = reader.readString();
					break;
				case "age":
					age = reader.readInt32();
					break;
				case "occupations":
					reader.readStartArray();
					while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
						Occupation occupation = occupationCodec.decode(reader, decoderContext);
						occupations.add(occupation);
					}
					reader.readEndArray();
					break;
				default:
					reader.skipValue();
					break;
			}
		}
		reader.readEndDocument();
		return new Person(name, age, occupations);
	}

	@Override
	public Class<Person> getEncoderClass() {
		return Person.class;
	}
}

