Feature: A cucumber test feature file
	Feature file extension has to be .feature
	All keywords are case sensitive
	All feature files have to contain Feature and Scenario
	
Scenario: a simple given.
Given I have 5 cukes in my belly
	And I have 3 cups of orange juice in my belly
	
When Afternoon comes after 2 pm
	But before 3 pm
	
Then I drink 6 cups of coffee

@TaggedScenario
Scenario: this is a tagged scenario
Given This is a tagged scenario with tag TaggedScenario
