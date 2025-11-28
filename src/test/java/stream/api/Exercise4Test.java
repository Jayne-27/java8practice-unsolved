package stream.api;

import common.test.tool.annotation.Easy;
import common.test.tool.dataset.ClassicOnlineStore;
import common.test.tool.entity.Customer;

import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class Exercise4Test extends ClassicOnlineStore {

    @Easy
    @Test
    public void firstRegistrant() {
        List<Customer> customerList = this.mall.getCustomerList();

        /**
         * Find the first customer who registered this online store by using {@link Stream#findFirst}
         * The customerList are ascending ordered by registered timing.
         */
        Optional<Customer> firstCustomer = customerList.stream().findFirst();

        assertEquals(customerList.get(0), firstCustomer.get());
    }

    @Easy
    @Test
    public void isThereAnyoneOlderThan40() {
        List<Customer> customerList = this.mall.getCustomerList();

        /**
         * Check whether any customer older than 40 exists or not, by using {@link Stream#anyMatch}
         */
        boolean olderThan40Exists = customerList.stream().anyMatch(c -> c.getAge() > 40);

        assertFalse(olderThan40Exists);
    }

    @Easy
    @Test
    public void isEverybodyOlderThan20() {
        List<Customer> customerList = this.mall.getCustomerList();

        /**
         * Check whether all customer are older than 20 or not, by using {@link Stream#allMatch}
         */
        boolean allOlderThan20 = customerList.stream().allMatch(c -> c.getAge() > 20);

        assertTrue(allOlderThan20);
    }

    @Easy
    @Test
    public void everyoneWantsSomething() {
        List<Customer> customerList = this.mall.getCustomerList();

        /**
         * Confirm that none of the customer has empty list for their {@link Customer.wantToBuy}
         * by using {@link Stream#noneMatch}
         */
        boolean everyoneWantsSomething = customerList.stream().noneMatch(c -> c.getWantToBuy() == null || c.getWantToBuy().isEmpty());

        assertTrue(everyoneWantsSomething);
    }
}
