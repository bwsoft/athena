#include "NativeUtil.h"

#ifdef __cplusplus
extern "C"
#endif
int getLXRand(int *);

JNIEXPORT jint JNICALL Java_com_bwsoft_athena_jni_NativeUtil_getLXRandom(JNIEnv * pEnv, jclass thisObj) {
	jint lxRand = 0;
	int result = getLXRand(&lxRand);
	if( result < 0 ) {
		jclass Exception = pEnv->FindClass("java/lang/Exception");
		pEnv->ThrowNew(Exception, "failed in getting system random number. error code="+result);
	}
	return lxRand;
}
