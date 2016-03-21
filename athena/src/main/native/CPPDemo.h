/*
 * CPPDemo.h
 *
 *  Created on: Mar 21, 2016
 *      Author: yzhou
 */

#ifndef SRC_MAIN_NATIVE_CPPDEMO_H_
#define SRC_MAIN_NATIVE_CPPDEMO_H_

#include <string>
#include <sstream>

class CPPDemo {
	const static double pi;
	static int snum;
	int x, y;

public:
	CPPDemo();
	CPPDemo(int x, int y);

	virtual ~CPPDemo();

	template <class T> T sum(T a1, T a2) const;

	// function pointer
	int oper(int a, int b, int (*func)(int, int));

	// demonstration of operator overload
	virtual CPPDemo operator + (CPPDemo & other);
	virtual CPPDemo & operator = (CPPDemo & other);
	virtual CPPDemo & operator ++ ();

	// return a temporary string
	std::string getName() {
		return "alex";
	}


	std::string toString() const {
		// modify static member field is ok
		snum ++;

		std::stringstream ss;
		ss << "{x=" << x << ",y=" << y << ",snum=" << snum << "}";
		return ss.str();
	}
};

#endif /* SRC_MAIN_NATIVE_CPPDEMO_H_ */
