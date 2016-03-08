/*
 * NativeObserver.cpp
 *
 *  Created on: Mar 7, 2016
 *      Author: yzhou
 */
#include <jni.h>
#include "ObservableOfNativeByteArray.h"
#include "NativeObserver.h"

JNIEXPORT void JNICALL Java_com_bwsoft_athena_jni_ObservableOfNativeByteArray_startNativeObservable
  (JNIEnv * pEnv, jobject thisObject) {
	jclass clz = pEnv->GetObjectClass(thisObject);
	jmethodID methodID = pEnv->GetMethodID(clz,"setByteArray","([B)V");
	if( NULL == methodID ) {
		// do something
	}
	NativeObserver observer(pEnv, clz, methodID, thisObject);

	// start the thread that uses the observer
}

NativeObserver::NativeObserver(JNIEnv *pEnv, jclass clz, jmethodID methodID, jobject thisObj) {
	this->pEnv = pEnv;
	this->clz = clz;
	this->methodID = methodID;
	this->thisObj = thisObj;
	this->retBuf = pEnv->NewByteArray(bufferSize);
}

NativeObserver::~NativeObserver() {
	// TODO Auto-generated destructor stub
}

void NativeObserver::setByteArray(const signed char *pArray) {
	pEnv->SetByteArrayRegion(retBuf,0,bufferSize,pArray);
	pEnv->CallVoidMethod(thisObj, methodID, retBuf);
}
