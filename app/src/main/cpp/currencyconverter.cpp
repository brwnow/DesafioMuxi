//
// Created by Bruno on 18/08/2017.
//

#include <jni.h>

extern "C" {
    JNIEXPORT jdouble JNICALL Java_com_bruno_desafiomuxi_core_FruitDetailsActivity_convertCurrency(JNIEnv *env, jobject, jdouble baseCurrency, jdouble conversionRatio) {
        return baseCurrency * conversionRatio;
    }
}