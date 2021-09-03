package com.jafritech.chinesetakeawayjava.helper;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;

public class HelperTest {

    @Test
    public void testFormatPrice() {
        Helper helper = new Helper();
        double amount = 20;
        String expected = "£20.00";
        String result = helper.formatPrice(amount);
        assertThat(result).isEqualTo(expected);
    }
}