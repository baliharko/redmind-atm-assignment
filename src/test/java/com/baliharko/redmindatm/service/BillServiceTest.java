package com.baliharko.redmindatm.service;

import com.baliharko.redmindatm.model.Bill;
import com.baliharko.redmindatm.repository.BillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BillServiceTest {

    public BillService billService;

    @Mock
    public BillRepository mockRepository;

    public List<Bill> testBills;

    @BeforeEach
    public void initTests() {
        billService = new BillService(mockRepository);
        testBills = Arrays.asList(
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
        );
    }

    @Test
    public void getAllBillsTest() {
        when(mockRepository.findAll()).thenReturn(testBills);
        assertEquals(testBills, billService.getAllBills());
    }

    @Test
    public void getBillDenominationsTest() {
        when(mockRepository.findAll()).thenReturn(testBills);

        List<Integer> expected = Arrays.asList(1000, 500, 100);
        List<Integer> actual = billService.getBillDenominations();

        assertEquals(expected, actual);
        assertNotEquals(Arrays.asList(1000, 100), actual);
        verify(mockRepository).findAll();
    }

    @Test
    @DisplayName("withdrawBills - get least amount of bills")
    public void getLeastAmountOfBillsTest() {
        when(mockRepository.findAll()).thenReturn(testBills);
        when(mockRepository.existsBillByValue(1000)).thenReturn(true, true, false);
        when(mockRepository.removeBillByValue(1000)).thenReturn(new Bill(1000), new Bill(1000), null);
        when(mockRepository.existsBillByValue(500)).thenReturn(true, true, true, false);
        when(mockRepository.removeBillByValue(500)).thenReturn(new Bill(500), new Bill(500), new Bill(500), null);
        when(mockRepository.existsBillByValue(100)).thenReturn(true, true, true, true, true, false);
        when(mockRepository.removeBillByValue(100))
                .thenReturn(new Bill(100), new Bill(100), new Bill(100), new Bill(100), new Bill(100), null);

        assertEquals(
                Arrays.asList(new Bill(1000), new Bill(1000)),
                billService.withdrawBills(2000)
        );

        when(mockRepository.findAll()).thenReturn(Arrays.asList(
                new Bill(500),
                new Bill(500),
                new Bill(500),
                new Bill(100),
                new Bill(100),
                new Bill(100),
                new Bill(100),
                new Bill(100)
        ));

        assertEquals(
                Arrays.asList(
                        new Bill(500),
                        new Bill(100),
                        new Bill(100),
                        new Bill(100),
                        new Bill(100)
                ), billService.withdrawBills(900)
        );

        when(mockRepository.findAll()).thenReturn(Arrays.asList(
                new Bill(500),
                new Bill(500),
                new Bill(100)
        ));

        assertEquals(
                Arrays.asList(
                        new Bill(500),
                        new Bill(500),
                        new Bill(100)
                ), billService.withdrawBills(1100)
        );

        verify(mockRepository, times(6)).findAll();

        verify(mockRepository, times(2)).existsBillByValue(1000);
        verify(mockRepository, times(3)).existsBillByValue(500);
        verify(mockRepository, times(5)).existsBillByValue(100);

        verify(mockRepository, times(2)).removeBillByValue(1000);
        verify(mockRepository, times(3)).removeBillByValue(500);
        verify(mockRepository, times(5)).removeBillByValue(100);
    }

    @Test
    public void getTotalBalanceTest() {
        when(mockRepository.findAll()).thenReturn(testBills);
        assertEquals(4000, billService.getTotalBalance());

        when(mockRepository.findAll()).thenReturn(
                Arrays.asList(new Bill(100), new Bill(500)));
        assertEquals(600, billService.getTotalBalance());
    }

    @Test
    public void getBillQuantityTest() {
        when(mockRepository.countBillsByValue(1000)).thenReturn(2);
        when(mockRepository.countBillsByValue(500)).thenReturn(3);
        when(mockRepository.countBillsByValue(100)).thenReturn(5);

        assertEquals(2, billService.getBillQuantity(1000));
        assertEquals(3, billService.getBillQuantity(500));
        assertEquals(5, billService.getBillQuantity(100));

        verify(mockRepository, times(3)).countBillsByValue(anyInt());
    }

    @Test
    @DisplayName("withdrawBills - throw error if insufficient funds")
    public void getBillsInsufficientFundsErrorTest() {
        when(mockRepository.findAll()).thenReturn(testBills);
        assertThrows(ResponseStatusException.class, () -> billService.withdrawBills(9000));

        verify(mockRepository).findAll();
    }

    @Test
    @DisplayName("withdrawBills - throw error if not enough bills")
    public void getBillsNotEnoughBills() {
        when(mockRepository.findAll()).thenReturn(testBills);
        when(mockRepository.existsBillByValue(1000)).thenReturn(true, true, false);
        when(mockRepository.removeBillByValue(1000)).thenReturn(new Bill(1000), new Bill(1000), null);
        when(mockRepository.existsBillByValue(500)).thenReturn(true, true, true, false);
        when(mockRepository.removeBillByValue(500)).thenReturn(new Bill(500), new Bill(500), new Bill(500), null);
        when(mockRepository.existsBillByValue(100)).thenReturn(true, true, true, true, true, false);
        when(mockRepository.removeBillByValue(100))
                .thenReturn(new Bill(100), new Bill(100), new Bill(100), new Bill(100), new Bill(100), null);

        assertDoesNotThrow(() -> billService.withdrawBills(3600));
        assertThrows(ResponseStatusException.class, () -> billService.withdrawBills(500));
    }
}