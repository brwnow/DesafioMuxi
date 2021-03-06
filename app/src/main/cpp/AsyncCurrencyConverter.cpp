//
// Created by Bruno on 19/08/2017.
//

#include "AsyncCurrencyConverter.h"

#include<android/log.h>

void AsyncCurrencyConverter::convertCurrencyCaller(AsyncCurrencyConverter *asyncCurConverter,
                                                   jdouble baseValue, jdouble ratio)
{
    JNIEnv *env;

    // Attach the current thread to a new JNI enviroment
    if(asyncCurConverter->getJavaVM()->AttachCurrentThread(&env, NULL) == 0) {
        // Get the class of FruitDetailsActivity
        jclass fruitDetailsActivityClass = env->GetObjectClass(asyncCurConverter->getGlobalObjectRef());

        // Get the ID of the callback method to pass the convertion result
        jmethodID callbackMethodId = env->GetMethodID(fruitDetailsActivityClass, "conversionCallback", "(D)V");

        // Conversion result from base value using given ratio
        jdouble conversionResult = baseValue * ratio;

        // Pass the result of the conversion to the callback method
        env->CallVoidMethod(asyncCurConverter->getGlobalObjectRef(), callbackMethodId, conversionResult);


        // Free memory alocated by this global reference of the jobject
        env->DeleteGlobalRef(asyncCurConverter->getGlobalObjectRef());

        // Detach the current thread from JVM
        asyncCurConverter->getJavaVM()->DetachCurrentThread();
    } else {
        __android_log_write(ANDROID_LOG_ERROR, "ASYNC_CONVERTER", "Failed to attach a thread to the JVM");
    }
        delete asyncCurConverter;
}

AsyncCurrencyConverter::AsyncCurrencyConverter(JNIEnv *env, jobject obj) {
    // Global reference of the instance of FruitDetailsActivity that called the lib
    globalObjRef = env->NewGlobalRef(obj);
}


void AsyncCurrencyConverter::asyncConvertCurrency(jdouble baseValue, jdouble ratio) {
    std::thread thread(AsyncCurrencyConverter::convertCurrencyCaller, this, baseValue, ratio);

    thread.detach();
}

JavaVM *AsyncCurrencyConverter::getJavaVM() {
    return jvm;
}

jobject AsyncCurrencyConverter::getGlobalObjectRef() {
    return globalObjRef;
}

void AsyncCurrencyConverter::setJVM(JavaVM *jvm) {
    this->jvm = jvm;
}


