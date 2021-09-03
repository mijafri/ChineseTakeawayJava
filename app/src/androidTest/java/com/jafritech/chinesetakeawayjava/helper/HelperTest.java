package com.jafritech.chinesetakeawayjava.helper;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import com.jafritech.chinesetakeawayjava.ItemsListDataSource;
import org.junit.Test;
import java.util.ArrayList;
import static com.google.common.truth.Truth.assertThat;

public class HelperTest {

    @Test
    public void  testDbOperations() {
        Context context = ApplicationProvider.getApplicationContext();
        Helper helper = new Helper();

        ArrayList<ItemsListDataSource> result = helper.getOrderList(context);
        assertThat(result).isEmpty();

        helper.insertOrder("Momos", "6", "33.0", context);
        result = helper.getOrderList(context);
        assertThat(result.size()).isEqualTo(1);

        helper.insertOrder("Onion Rings", "1", "3.20", context);
        result = helper.getOrderList(context);
        assertThat(result.size()).isEqualTo(2);

        helper.insertOrder("Won Ton Soup", "1", "3.60", context);
        result = helper.getOrderList(context);
        assertThat(result.size()).isEqualTo(3);

        helper.deleteAllRecords(context);

        result = helper.getOrderList(context);
        assertThat(result.size()).isEqualTo(0);
    }

}