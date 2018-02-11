package xyz.sluggard.transmatch.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Token {
	
	private final String Name;
	
	private BigDecimal value;

}
