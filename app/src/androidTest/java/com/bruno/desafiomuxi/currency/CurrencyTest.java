package com.bruno.desafiomuxi.currency;

import com.bruno.desafiomuxi.currency.Currency;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Bruno on 20/08/2017.
 */
public class CurrencyTest {
    // Verify if Currency is correctly mapping pairs to their conversion ratio
    @Test
    public void getConversionRatio() throws Exception {
        Currency currency = new Currency();

        assertEquals("Wrong conversion ratio between USD and BRL returned", currency.getConversionRatio(Currency.CurrencyTag.USD, Currency.CurrencyTag.BRL), 3.5, 0.2);
        assertEquals("Wrong conversion ratio between BRL and USD returned", currency.getConversionRatio(Currency.CurrencyTag.BRL, Currency.CurrencyTag.USD), 1.0 / 3.5, 0.2);
    }

    // Verify if Currency is correctly formating values given a currency format
    @Test
    public void format() throws Exception {
        Currency currency = new Currency();

        assertEquals("U$ 0,00", currency.format(0.0, Currency.CurrencyTag.USD));
        assertEquals("U$ 999,99", currency.format(999.99, Currency.CurrencyTag.USD));
        assertEquals("U$ 1,00", currency.format((int)1, Currency.CurrencyTag.USD));
        assertEquals("R$ 15,49", currency.format(15.49, Currency.CurrencyTag.BRL));
        assertEquals("R$ 15,50", currency.format(15.5, Currency.CurrencyTag.BRL));
    }

}