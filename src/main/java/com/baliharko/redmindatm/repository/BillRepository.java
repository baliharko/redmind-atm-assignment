package com.baliharko.redmindatm.repository;

import com.baliharko.redmindatm.model.Bill;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends MongoRepository<Bill, String> {
    Bill removeBillByValue(int value);
    boolean existsBillByValue(int value);
    int countBillsByValue(int value);
    List<Bill> getBillsByValue(int value);
}
