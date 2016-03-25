package com.bwsoft.athena.cucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CucumberStepDefs {
	@Given("I have (\\d+) cukes in my belly")
	public void firstStep(int cukes) {
		System.out.format("Cukes %d\n", cukes);
	}
	
	@Given("I have (\\d+) cups of orange juice in my belly")
	public void secondStep(int cukes) {
		System.out.format("Cups: %d of orange juice\n", cukes);
	}
	
	@When("Afternoon comes after (\\d+) pm")
	public void thridStep(int t) {
		System.out.format("Afternoon at %d\n", t);
	}
	
	@When("before (\\d+) pm")
	public void fourthStep(int t) {
		System.out.format("But before %d\n", t);
	}
	
	@Then("I drink (\\d+) cups of coffee")
	public void fifthStep(int n) {
		System.out.format("I drink %d cups of coffee\n", n);
	}
	
	@Given("This is a tagged scenario with tag ([^\"]*)$")
	public void anotherScenario(String s) {
		System.out.println(s);
	}
}
