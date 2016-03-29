#include "ThreadDemo.h"
#include <sched.h>
#include <unistd.h>
#include <iostream>
#include <sys/types.h>
#include <errno.h>
#include <string.h>

using namespace std;

JNIEXPORT void JNICALL Java_com_bwsoft_athena_jni_ThreadDemo_pinThreadNative
  (JNIEnv * pEnv, jobject thisObj, jint core) {
	cpu_set_t set;
	CPU_ZERO(&set);
	CPU_SET(core,&set);
	cout << "Pin thread to core: " << core << endl;
	if( sched_setaffinity(0,sizeof(set),&set) == -1 ) {
		cout << "Failed in setting affinity: " << strerror(errno) << " error code=" << errno << endl;
	}
}
