//
// Created by Bruno on 19/08/2017.
//

#ifndef DESAFIOMUXI_ASYNCCURRENCYCONVERTER_H
#define DESAFIOMUXI_ASYNCCURRENCYCONVERTER_H

#include <jni.h>
#include <thread>

class AsyncCurrencyConverter {
public:
    // Converts the give value using the given ratio and then calls the
    // callback method passing the resulting value
    // This function is called asynconous by the AsyncCurrencyConverter
    static void convertCurrencyCaller(AsyncCurrencyConverter *asyncCurConverter, jdouble baseValue, jdouble ratio);

    AsyncCurrencyConverter(JNIEnv *env, jobject obj);
    ~AsyncCurrencyConverter();

    JavaVM* getJavaVM();
    jobject getGlobalObjectRef();

    // Calls the converter method in another thread.
    void asyncConvertCurrency(jdouble baseValue, jdouble ratio);

private:
    JavaVM *jvm; // JVM reference
    JNIEnv *env; // JNI enviroment reference
    jobject globalObjRef; // Global ref of the instance of FruitDetailsActivity
};

#endif DESAFIOMUXI_ASYNCCURRENCYCONVERTER_H
