package com.ipoca.bbrpc.demo.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *@Author：xubang
 *@Date：2024/3/13  22:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    Long id;

    float amount;
}
