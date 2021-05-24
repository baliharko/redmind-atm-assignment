package com.baliharko.redmindatm.service;

import com.baliharko.redmindatm.model.Bill;
import com.baliharko.redmindatm.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AtmService {

    private final BillRepository repository;

    public List<Bill> getAllBills() {
        return repository.findAll();
    }

    public List<Bill> withdrawBills(double requestedAmount) {
        List<Bill> billsToWithdraw = new ArrayList<>();

        if (requestedAmount > getTotalBalance())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");

        for (Integer billValue : getBillDenominations()) {
            int quantity = (int) requestedAmount / billValue;

            if (quantity > 0) {
                for (int i = 0; i < quantity; i++) {

                    Bill toBeWithdrawn = repository.existsBillByValue(billValue) ? repository.removeBillByValue(billValue) : null;

                    if (toBeWithdrawn != null) {
                        billsToWithdraw.add(toBeWithdrawn);
                        requestedAmount -= billValue;
                    } else
                        break;
                }
            }
        }

        if (requestedAmount != 0) {
            repository.saveAll(billsToWithdraw);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough bills");
        }

        return billsToWithdraw;
    }

    public List<Integer> getBillDenominations() {
        return repository.findAll()
                .stream()
                .map(Bill::getValue)
                .distinct()
                .collect(Collectors.toList());
    }

    public double getTotalBalance() {
        return repository.findAll()
                .stream()
                .map(Bill::getValue)
                .reduce(0, (total, value) -> total += value);
    }

    public int getBillQuantity(int value) {
        return repository.countBillsByValue(value);
    }
}
