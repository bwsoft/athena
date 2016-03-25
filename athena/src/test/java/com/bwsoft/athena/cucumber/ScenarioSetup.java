package com.bwsoft.athena.cucumber;

import cucumber.api.java.After;
import cucumber.api.java.Before;

public class ScenarioSetup {
	@Before
	public void beforeScenario() {
		System.out.println("Before each scenario");
	}
	
	@After
	public void afterScenario() {
		System.out.println("after each scenario");
	}
	
	@Before("@TaggedScenario") 
	public void beforeTaggedScenario() {
		System.out.println("Before each tagged scenario");
	}
	
	// has to match the tag name
	@After("@TaggedScenario") 
	public void afterTaggedScenario() {
		System.out.println("after each tagged scenario");
	}
}
