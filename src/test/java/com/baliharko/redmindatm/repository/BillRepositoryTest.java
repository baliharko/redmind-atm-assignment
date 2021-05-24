package com.baliharko.redmindatm.repository;

import com.baliharko.redmindatm.model.Bill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class BillRepositoryTest {

    @Autowired
    BillRepository testRepository;

    @BeforeEach
    public void initTests() {
        testRepository.deleteAll();
        testRepository.saveAll(Arrays.asList(
                new Bill(1000),
                new Bill(1000),
                new Bill(500),
                new Bill(500),
                new Bill(500),
                new Bill(100),
                new Bill(100),
                new Bill(100),
                new Bill(100),
                new Bill(100)
        ));
    }

    @Test
    @DisplayName("removeBillByValue - check if bills are removed from repository")
    public void removeBillByValueTest() {
        assertEquals(testRepository.getBillsByValue(1000).size(), 2);
        assertEquals(testRepository.getBillsByValue(500).size(), 3);
        assertEquals(testRepository.getBillsByValue(100).size(), 5);

        assertEquals(testRepository.findAll().size(), 10);
        testRepository.removeBillByValue(1000);
        assertEquals(testRepository.findAll().size(), 9);

        assertEquals(testRepository.getBillsByValue(1000).size(), 1);
        assertEquals(testRepository.getBillsByValue(500).size(), 3);
        assertEquals(testRepository.getBillsByValue(100).size(), 5);
    }

    @Test
    @DisplayName("removeBillByValue - check if method returns removed Bill object")
    public void removeBillByValueCheckReturnedObjectTest() {
        assertEquals(testRepository.findAll().get(0), testRepository.removeBillByValue(1000));
        assertEquals(testRepository.findAll().get(0), testRepository.removeBillByValue(1000));

        assertNull(testRepository.removeBillByValue(1000));
    }

    @Test
    public void countBillsByValueTest() {
        assertEquals(2, testRepository.countBillsByValue(1000));
        assertEquals(3, testRepository.countBillsByValue(500));
        assertEquals(5, testRepository.countBillsByValue(100));
    }

    @Test
    public void existsBillByValueTest() {

        testRepository.deleteAll();
        testRepository.saveAll(Arrays.asList(new Bill(1000), new Bill(500)));

        assertTrue(testRepository.existsBillByValue(1000));
        assertTrue(testRepository.existsBillByValue(500));

        assertFalse(testRepository.existsBillByValue(100));
    }
}
