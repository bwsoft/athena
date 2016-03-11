/*
 * NativeObservable.cpp
 *
 *  Created on: Mar 9, 2016
 *      Author: yzhou
 */
#include <iostream>
#include <chrono>
#include "NativeObservable.h"

using namespace std;

// Note: thisObj has to be a global reference.
NativeObservable::NativeObservable(JavaVM *pJvm, jobject thisObj) : pJvm(pJvm), pObj(thisObj) {
}

NativeObservable::~NativeObservable() {
	isRunning = false;
}

void NativeObservable::run() {
	cout << "native observable thread started" << endl;

	// attach to JVM to obtain JNIEnv
	JNIEnv *pEnv;
	pJvm->AttachCurrentThread((void **)&pEnv, NULL);

	jclass pClass = pEnv->GetObjectClass(pObj);
	jmethodID pMethodID = pEnv->GetMethodID(pClass, "setByteArray", "([B)V");
	if( NULL == pMethodID ) {
		// TODO: throw exception back to JVM
		cout << "Cannot find method" << endl;
		return;
	}

	// create an event buffer for returning events
	jbyteArray eventBuffer = pEnv->NewByteArray(bufferSize);

	while(isRunning) {
		pEnv->SetByteArrayRegion(eventBuffer, 0, bufferSize, (jbyte *) eventContent.c_str());
		pEnv->CallVoidMethod(pObj,pMethodID,eventBuffer,0,bufferSize);

		this_thread::sleep_for(chrono::seconds(1));
	}
	pEnv->DeleteGlobalRef(pObj);
	pJvm->DetachCurrentThread();
	cout << "native observable thread ended" << endl;
}

void NativeObservable::startObservable() {
	isRunning = true;
	pThread = new thread(&NativeObservable::run, this);
}

void NativeObservable::stopObservable() {
	isRunning = false;
}
