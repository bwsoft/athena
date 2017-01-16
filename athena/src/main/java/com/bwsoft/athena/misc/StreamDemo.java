package com.bwsoft.athena.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StreamDemo {
	private enum Gender {
		M,
		F
	};
	
	private class Person {
		private final String firstName;
		private final String lastName;
		private final int age;
		private final boolean isSingle;
		private final Gender gender;
		
		Person(String firstName, String lastName, int age, boolean isSingle, Gender gender) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.age = age;
			this.isSingle = isSingle;
			this.gender = gender;
		}
		
		public Gender getGender() {
			return gender;
		}
		
		public int getAge() {
			return age;
		}
		
		public String getName() {
			return firstName+" "+lastName;
		}
		
		public void displayPerson() {
			System.out.println("   Name: "+firstName+" "+lastName);
			System.out.println("    Age:" + age);
			System.out.println("Married: "+isSingle);
			System.out.println(" Gender: "+gender.name());
		}
	}
	
	private final List<Person> people;
	
	public StreamDemo() {
		people = new ArrayList<>();
		people.add(new Person("Mary","Woodland", 32, true, Gender.F));
		people.add(new Person("Peter","Nowhere", 52, false, Gender.M));
		people.add(new Person("Wayne","Wright", 65, false, Gender.M));
		people.add(new Person("Larry","Pond", 28, true, Gender.M));
	}
	
	public List<Person> getFemale() {
		return people.stream().filter((t)-> {return t.gender == Gender.F;} ).collect(Collectors.toList());
	}
	
	public void displayMale() {
		people.stream().filter(t->t.gender == Gender.M).forEach(t->t.displayPerson());
	}
	
	public Map<Gender, List<Person>> groupByGender() {
		return people.stream().collect(Collectors.groupingBy(p-> p.gender));
	}
	
	public List<String> getNames() {
		return people.stream().map(Person::getName).sorted((t1,t2)->t1.compareTo(t2)).collect(Collectors.toCollection(ArrayList::new));
	}

	public Set<String> getUnsortedNames() {
		return people.stream().map(Person::getName).sorted((t1,t2)->t1.compareTo(t2)).collect(Collectors.toCollection(HashSet::new));
	}

	public int averageAge() {
		return people.stream().mapToInt(Person::getAge).sum()/people.size();
	}
	
	public static void main(String[] args){
		StreamDemo demo = new StreamDemo();
		for(Person femal : demo.getFemale()) {
			femal.displayPerson();
		}
		
		System.out.println();
		demo.displayMale();
		
		for(String name : demo.getNames() ) {
			System.out.println(name);
		}

		for(String name : demo.getUnsortedNames() ) {
			System.out.println(name);
		}
		
		System.out.println("Average age: "+demo.averageAge());
	}
}
