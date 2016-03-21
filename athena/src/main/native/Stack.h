/*
 * Stack.h
 *
 *  Created on: Mar 21, 2016
 *      Author: yzhou
 */

#ifndef SRC_MAIN_NATIVE_STACK_H_
#define SRC_MAIN_NATIVE_STACK_H_

#include <vector>

using namespace std;

template <class T>
class Stack {
	vector<T> elms;

public:
	Stack();
	virtual ~Stack();

	void push(const T &);
	void pop();
	T top() const;
	bool empty() const {
		return elms.empty();
	}
};

#endif /* SRC_MAIN_NATIVE_STACK_H_ */
