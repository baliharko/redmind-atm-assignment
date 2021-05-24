package com.baliharko.redmindatm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bill {

    private String id;
    private int value;

    public Bill(int value) {
        this.value = value;
    }
}
