package com.zenika.hexagonal.domain;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Address {
	private String town;
	private String state;
	private String zipCode;
	private String details;
}
