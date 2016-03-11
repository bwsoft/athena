/*
 * NativeObservable.h
 *
 *  Created on: Mar 9, 2016
 *      Author: yzhou
 */
#include <thread>
#include <jni.h>

#ifndef SRC_MAIN_NATIVE_NATIVEOBSERVABLE_H_
#define SRC_MAIN_NATIVE_NATIVEOBSERVABLE_H_
/**
 * This code is on C++ 11 version. To make eclipse to support c++ 11, go to
 *
 *     Project -> Properties -> C/C++ General -> Path and Symbols -> Tab [Symbols].
 *
 * and add the symbol "__cplusplus" with the value "201103L".
 *
 */
class NativeObservable {
	const int bufferSize = 128;
	JavaVM *pJvm;
	jobject pObj;

	std::string eventContent = "event from native";

	bool isRunning = false;
	std::thread *pThread = NULL;

	void run();

public:
	NativeObservable(JavaVM *pJvm, jobject thisObj);
	virtual ~NativeObservable();

	void startObservable();
	void stopObservable();
};

#endif /* SRC_MAIN_NATIVE_NATIVEOBSERVABLE_H_ */
