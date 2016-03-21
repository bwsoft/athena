/*
 * Stack.cpp
 *
 *  Created on: Mar 21, 2016
 *      Author: yzhou
 */

#include <iostream>
#include <string>
#include <stdexcept>
#include "Stack.h"

template <class T>
Stack<T>::Stack() {
	// TODO Auto-generated constructor stub

}

template <class T>
Stack<T>::~Stack() {
	// TODO Auto-generated destructor stub
}

template <class T>
void Stack<T>::push(const T & elm) {
	elms.push_back(elm);
}

template <class T>
void Stack<T>::pop() {
	if( elms.empty() ) {
		throw out_of_range("Stack<>::pop(): empty stack");
	}

	elms.pop_back();
}

template <class T>
T Stack<T>::top() const {
	if( elms.empty() ) {
		throw out_of_range("Stack<>::pop(): empty stack");
	}

	return elms.back();
}

int main() {
	try {
		Stack<int> intStack;
		Stack<string> stringStack;

		intStack.push(7);
		cout << intStack.top() << endl;

		stringStack.push("hello");
		cout << stringStack.top() << endl;
		stringStack.pop();
		stringStack.pop();

	} catch( exception const & ex ) {
		cerr << "Exception: " << ex.what() << endl;
		return -1;
	}
}
