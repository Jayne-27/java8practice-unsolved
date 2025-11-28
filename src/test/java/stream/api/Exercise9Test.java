package stream.api;

import common.test.tool.annotation.Difficult;
import common.test.tool.annotation.Easy;
import common.test.tool.dataset.ClassicOnlineStore;
import common.test.tool.entity.Customer;

import org.junit.Test;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collector;

import common.test.tool.util.CollectorImpl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;

public class Exercise9Test extends ClassicOnlineStore {

    @Easy
    @Test
    public void simplestStringJoin() {
        List<Customer> customerList = this.mall.getCustomerList();

        /**
         * Implement a {@link Collector} which can create a String with comma separated names shown in the assertion.
         * The collector will be used by serial stream.
         */
        Collector<String, ?, String> toCsv = new CollectorImpl<>(
            () -> new StringJoiner(","),
            (sj, s) -> sj.add(s),
            (sj1, sj2) -> { sj1.merge(sj2); return sj1; },
            StringJoiner::toString,
            EnumSet.noneOf(Collector.Characteristics.class)
        );
        String nameAsCsv = customerList.stream().map(Customer::getName).collect(toCsv);
        assertEquals("Joe,Steven,Patrick,Diana,Chris,Kathy,Alice,Andrew,Martin,Amy", nameAsCsv);
    }

    @Difficult
    @Test
    public void mapKeyedByItems() {
        List<Customer> customerList = this.mall.getCustomerList();

        /**
         * Implement a {@link Collector} which can create a {@link Map} with keys as item and
         * values as {@link Set} of customers who are wanting to buy that item.
         * The collector will be used by parallel stream.
         */



        Collector<Customer, ?, Map<String, Set<String>>> toItemAsKey = new common.test.tool.util.CollectorImpl<>(
                () -> new HashMap<String, Set<String>>(),
                (map, customer) -> {
                    customer.getWantToBuy().forEach(i -> {
                        map.computeIfAbsent(i.getName(), k -> new HashSet<String>()).add(customer.getName());
                    });
                },
                (m1, m2) -> {
                    m2.forEach((k, v) -> m1.merge(k, v, (s1, s2) -> { s1.addAll(s2); return s1; }));
                    return m1;
                },
                Function.identity(),
                EnumSet.of(Collector.Characteristics.UNORDERED)
        );

        Map<String, Set<String>> itemMap =
                customerList.stream().parallel().collect(toItemAsKey);


        assertThat(itemMap.get("plane"), containsInAnyOrder("Chris"));
        assertEquals(new HashSet<>(Arrays.asList("Patrick", "Amy")), itemMap.get("onion"));
        assertThat(itemMap.get("ice cream"), containsInAnyOrder("Patrick", "Steven"));
        assertThat(itemMap.get("earphone"), containsInAnyOrder("Steven"));
        assertThat(itemMap.get("plate"), containsInAnyOrder("Joe", "Martin"));
        assertThat(itemMap.get("fork"), containsInAnyOrder("Joe", "Martin"));
        assertThat(itemMap.get("cable"), containsInAnyOrder("Diana", "Steven"));
        assertThat(itemMap.get("desk"), containsInAnyOrder("Alice"));
    }

}
