/*
 * NativeObserver.h
 *
 *  Created on: Mar 7, 2016
 *      Author: yzhou
 */

#ifndef SRC_MAIN_NATIVE_NATIVEOBSERVER_H_
#define SRC_MAIN_NATIVE_NATIVEOBSERVER_H_

class NativeObserver {
private:
	const int bufferSize = 128;
	JNIEnv *pEnv;
	jclass clz;
	jmethodID methodID;
	jobject thisObj;
	jbyteArray retBuf;

public:
	NativeObserver(JNIEnv *pEnv, jclass clz, jmethodID methodID, jobject thisObj);
	virtual ~NativeObserver();

	void setByteArray(const signed char *pArray);
};

#endif /* SRC_MAIN_NATIVE_NATIVEOBSERVER_H_ */
