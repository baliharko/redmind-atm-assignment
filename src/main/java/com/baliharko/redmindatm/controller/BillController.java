package com.baliharko.redmindatm.controller;

import com.baliharko.redmindatm.model.Bill;
import com.baliharko.redmindatm.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/atm")
public class BillController {

    private final BillService billService;

    @GetMapping("/all")
    public List<Bill> getAllBills() {
        return billService.getAllBills();
    }

    @GetMapping("/withdraw/{amount}")
    public List<Bill> withdrawBills(@PathVariable double amount) {
        return billService.withdrawBills(amount);
    }

    @GetMapping("/balance")
    public String getCurrentBalance() {
        return "" + billService.getTotalBalance();
    }
}
