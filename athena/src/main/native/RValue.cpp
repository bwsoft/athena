/*
 * RValue.cpp
 *
 *  Created on: Mar 21, 2016
 *      Author: yzhou
 */
#include <iostream>
#include "RValue.h"

RValue::RValue(const string &str) : content(new string(str)) {
	// TODO Auto-generated constructor stub

}

RValue::~RValue() {
//	if( content != NULL ) delete content;
}

string & RValue::getMsg() {
	return *content;
}

RValue & RValue::operator =(RValue&& temp) {
	delete content;
	content = temp.content;
	temp.content = nullptr;
	return *this;
}


int main() {
	RValue value1("original message");

	//assign a temporary value to value1
	value1 = RValue("temporary msg");

	cout << "value1 is: " << value1.getMsg() << endl;
}
