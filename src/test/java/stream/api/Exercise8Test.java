package stream.api;

import common.test.tool.annotation.Difficult;
import common.test.tool.dataset.ClassicOnlineStore;
import common.test.tool.entity.Customer;
import common.test.tool.entity.Item;
import common.test.tool.entity.Shop;


import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

public class Exercise8Test extends ClassicOnlineStore {

    @Difficult
    @Test
    public void itemsNotOnSale() {
        // Removed unused variable customerStream
        // Removed unused variable shopStream

        /**
         * Create a set of item names that are in {@link Customer.wantToBuy} but not on sale in any shop.
         */
        List<String> itemListOnSale = this.mall.getShopList().stream()
            .flatMap(s -> s.getItemList().stream())
            .map(Item::getName)
            .distinct()
            .collect(Collectors.toList());

        Set<String> itemSetNotOnSale = this.mall.getCustomerList().stream()
            .flatMap(c -> c.getWantToBuy().stream())
            .map(Item::getName)
            .filter(n -> !itemListOnSale.contains(n))
            .collect(Collectors.toSet());

        assertThat(itemSetNotOnSale, hasSize(3));
        assertThat(itemSetNotOnSale, hasItems("bag", "pants", "coat"));

    }

    @Difficult
    @Test
    public void havingEnoughMoney() {
        /**
         * Create a customer's name list including who are having enough money to buy all items they want which is on sale.
         * Items that are not on sale can be counted as 0 money cost.
         * If there is several same items with different prices, customer can choose the cheapest one.
         */
        // build cheapest price map for available items
        final java.util.Map<String, Integer> cheapest = this.mall.getShopList().stream()
            .flatMap(s -> s.getItemList().stream())
            .collect(Collectors.toMap(Item::getName, Item::getPrice, Integer::min));

        List<String> customerNameList = this.mall.getCustomerList().stream()
            .filter(c -> {
                long total = c.getWantToBuy().stream()
                    .mapToLong(i -> cheapest.getOrDefault(i.getName(), 0))
                    .sum();
                return c.getBudget() >= total;
            })
            .map(Customer::getName)
            .collect(Collectors.toList());

        assertThat(customerNameList, hasSize(7));
        assertThat(customerNameList, hasItems("Joe", "Patrick", "Chris", "Kathy", "Alice", "Andrew", "Amy"));
    }
}
