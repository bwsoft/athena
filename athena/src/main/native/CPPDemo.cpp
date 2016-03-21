/*
 * CPPDemo.cpp
 *
 *  Created on: Mar 21, 2016
 *      Author: yzhou
 */

#include <iostream>
#include "CPPDemo.h"

/**
 * This code contains C++ 11 syntax and has to be compiled with -std=c++0x
 */

using namespace std;

int add(int a, int b) {
	return a+b;
}

int sub( int a, int b ) {
	return a-b;
}

CPPDemo::CPPDemo() {
	// TODO Auto-generated constructor stub
	x = 0;
	y = 0;
}

CPPDemo::CPPDemo(int x, int y) {
	this->x = x;
	this->y = y;
}

CPPDemo::~CPPDemo() {
	// TODO Auto-generated destructor stub
}

// constant template function
template<class T> T CPPDemo::sum(T a1, T a2) const {
	return a1 + a2;
}

int CPPDemo::oper(int a, int b, int (*func)(int, int)) {
	return func(a,b);
}

// initialize static member
int CPPDemo::snum = 1;
const double CPPDemo::pi = 3.1415926;

// operator overload
CPPDemo CPPDemo::operator +(CPPDemo &other) {
	CPPDemo temp;
	temp.x = this->x + other.x;
	temp.y = this->y + other.y;
	return temp;
}

CPPDemo & CPPDemo::operator =(CPPDemo &other) {
	this->x = other.x;
	this->y = other.y;
	return *this;
}

CPPDemo & CPPDemo::operator ++() {
	this->x ++;
	this->y ++;
	return *this;
}

int main() {
	CPPDemo demo1(10,20);
	CPPDemo demo2(4,5);
	CPPDemo demo3 = demo1 + demo2;
	cout << "Result of demo1 + demo2: " << demo3.toString() << endl;

	++demo1;
	cout << "Result of ++demo1: " << demo1.toString() << endl;

	demo1 = demo2;
	cout << "Result of demo1=demo2: " << demo1.toString() << endl;

	const CPPDemo demo4(2,5);
	cout << "++demo4 is not allowed since it is a constant. But toString is ok since it is a constant member function: " << demo4.toString() << endl;

	cout << "constant member function sum(5,6) can be called and return: " << demo4.sum(5,6) << endl;

	// rvalue vs lvalue
	// one cannot use reference to a temporary name
	// string &name = demo1.getName();
	// one has to use move operator (C++ 11 or beyond)
	string name = demo1.getName();
	// or one can use a rvalue
	string && rname = demo1.getName();
	cout << "rvalue of the name is: " << rname << endl;

	// function pointer
	int (*func) (int, int) = add;
	cout << "invoke add of operation: " << demo1.oper(5,6,func) << endl;
}
