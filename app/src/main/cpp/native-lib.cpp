#include <jni.h>
#include <string>

extern "C"

JNIEXPORT jstring JNICALL Java_com_bruno_desafiomuxi_MainActivity_stringFromJNI(JNIEnv *env, jobject) {
    return env->NewStringUTF(std::string("Hello from C++").c_str());
}