package com.baliharko.redmindatm.controller;

import com.baliharko.redmindatm.model.Bill;
import com.baliharko.redmindatm.service.AtmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/atm")
public class AtmController {

    private final AtmService atmService;

    @GetMapping("/all")
    public List<Bill> getAllBills() {
        return atmService.getAllBills();
    }

    @GetMapping("/withdraw/{amount}")
    public List<Bill> withdrawBills(@PathVariable double amount) {
        return atmService.withdrawBills(amount);
    }

    @GetMapping("/balance")
    public String getCurrentBalance() {
        return "" + atmService.getTotalBalance();
    }
}
