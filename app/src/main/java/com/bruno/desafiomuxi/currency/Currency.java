package com.bruno.desafiomuxi.currency;

import android.util.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bruno on 18/08/2017.
 */

//
public class Currency {
    public enum CurrencyTag {USD, BRL};

    // Each pair can be read like the conversion ratio from first currency to second currency
    private Map<Pair<CurrencyTag, CurrencyTag>, Double> ratioMap = new HashMap<>();

    // Associate a string TAG with each currency TAG
    private Map<CurrencyTag, String> currencyTagMap = new HashMap<>();

    public Currency() {
        initializeRatioMap();
        initializeCurrencyTag();
    }

    // This method fill up the map of ratios
    // It can be extended in further versions to get ratios
    // from a web service
    private void initializeRatioMap() {
        // Initializes with fixed values instead of reading from a file or getting values
        // from a web service
        ratioMap.put(new Pair<CurrencyTag, CurrencyTag>(CurrencyTag.USD, CurrencyTag.BRL), 3.5);
        ratioMap.put(new Pair<CurrencyTag, CurrencyTag>(CurrencyTag.BRL, CurrencyTag.USD), 1.0 / 3.5);
    }

    // This method fill up the currencies tags
    // It can be extended to get tags from a web service
    private void initializeCurrencyTag() {
        currencyTagMap.put(CurrencyTag.USD, "U$");
        currencyTagMap.put(CurrencyTag.BRL, "R$");
    }

    // Returns the conversion ratio from first currency to second currency
    // If the map of ratios does not contain this conversion, the method return
    // zero, because zero is an invalid ratio value
    public double getConversionRatio(CurrencyTag fromCurrency, CurrencyTag toCurrency) {
        Pair<CurrencyTag, CurrencyTag> ratioKey = new Pair<>(fromCurrency, toCurrency);

        if(ratioMap.containsKey(ratioKey)) {
            return ratioMap.get(ratioKey); // Conversion ratio found
        } else {
            return 0.0; // Conversion ratio is not registered
        }
    }

    public String format(double value, CurrencyTag currencyTag) {
        String tag;

        // Verify if there is an available tag for the given currency
        if(currencyTagMap.containsKey(currencyTag)) {
            tag = currencyTagMap.get(currencyTag);
        } else {
            tag = ""; // No available tag
        }

        return tag + String.format(" %.2f", value);
    }
}
