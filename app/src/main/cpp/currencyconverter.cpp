//
// Created by Bruno on 18/08/2017.
//

#include<jni.h>
#include "AsyncCurrencyConverter.h"

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_com_bruno_desafiomuxi_core_FruitDetailsActivity_asyncConvertCurrency(
        JNIEnv *env,
        jobject obj,
        jdouble baseCurrency,
        jdouble conversionRatio)
{
    AsyncCurrencyConverter asyncCurrencyConverter(env, obj);

    asyncCurrencyConverter.asyncConvertCurrency(baseCurrency, conversionRatio);
}

#ifdef __cplusplus
}
#endif
