package com.troila.cloud.profanity;

public interface ProfanityFilterConfiguration {
	public void configuration(ProfanityPolicy profanityPolicy);
	public ProfanityPolicy getProfanityPolicy();
}
