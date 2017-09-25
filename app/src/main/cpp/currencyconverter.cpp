//
// Created by Bruno on 18/08/2017.
//

#include<jni.h>
#include "AsyncCurrencyConverter.h"
#include <android/log.h>

static JavaVM* _jvm;

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_com_bruno_desafiomuxi_core_FruitDetailsActivity_asyncConvertCurrency(
        JNIEnv *env,
        jobject obj,
        jdouble baseCurrency,
        jdouble conversionRatio)
{
    AsyncCurrencyConverter *asyncCurrencyConverter = new AsyncCurrencyConverter(env, obj);
    asyncCurrencyConverter->setJVM(_jvm);
    asyncCurrencyConverter->asyncConvertCurrency(baseCurrency, conversionRatio);
}

jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    __android_log_write(ANDROID_LOG_VERBOSE, "ASYNC_CONVERTER", "JNI_OnLoad");
    JNIEnv* env;
    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
        __android_log_write(ANDROID_LOG_ERROR, "ASYNC_CONVERTER", "error in JNI_OnLoad");
        return -1;
    }

    _jvm = vm;

    return JNI_VERSION_1_6;
}

#ifdef __cplusplus
}
#endif
