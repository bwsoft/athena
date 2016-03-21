/*
 * RValue.h
 *
 *  Created on: Mar 21, 2016
 *      Author: yzhou
 */

#ifndef SRC_MAIN_NATIVE_RVALUE_H_
#define SRC_MAIN_NATIVE_RVALUE_H_

#include <string>

using namespace std;

class RValue {
	string *content;
public:
	RValue(const string &str);
	virtual ~RValue();

	// move operator
	RValue & operator=(RValue&& temp);

	string & getMsg();
};

#endif /* SRC_MAIN_NATIVE_RVALUE_H_ */
