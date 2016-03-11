#include <iostream>
#include "ObservableOfNativeByteArray.h"
#include "NativeObservable.h"

/**
 * Main entry of all JNI functions in athena.
 *
 * Use g++ to create the dynamically linked library:
 *    g++ -I$JAVA_HOME/include -I$JAVA_HOME/include/linux -std=c++0x -o libathena.so -shared *.cpp
 */

// native observable that monitors a native event using a native thread.
NativeObservable *pObservable = NULL;

JNIEXPORT void JNICALL Java_com_bwsoft_athena_jni_ObservableOfNativeByteArray_startNativeObservable
  (JNIEnv * pEnv, jobject thisObj) {
	if( NULL == pObservable ) {
		JavaVM *pJvm;
		pEnv->GetJavaVM(&pJvm);
		pObservable = new NativeObservable(pJvm, pEnv->NewGlobalRef(thisObj));
		pObservable->startObservable();
	}
}

JNIEXPORT void JNICALL Java_com_bwsoft_athena_jni_ObservableOfNativeByteArray_stopNativeObservable
  (JNIEnv *, jobject) {
	if( NULL != pObservable ) {
		pObservable->stopObservable();

		// TOD: change this brutal waiting for thread to exit
		std::this_thread::sleep_for(std::chrono::seconds(2));
		delete pObservable;
		pObservable = NULL;
	}
}

JNIEXPORT jbyteArray JNICALL Java_com_bwsoft_athena_jni_ObservableOfNativeByteArray_getNativeEvent
  (JNIEnv *pEnv, jobject thisObj) {
	std::this_thread::sleep_for(std::chrono::seconds(1));
	jbyteArray retBuf = pEnv->NewByteArray(1024);
	char content[128] = {'t','h','i','s',' ','i','s',' ','f','r','o','m',' ','n','a','t','i','v','e'};

	// set a region to be mapped and returned
	pEnv->SetByteArrayRegion(retBuf,0,10,(jbyte *)content);
	return retBuf;
}
