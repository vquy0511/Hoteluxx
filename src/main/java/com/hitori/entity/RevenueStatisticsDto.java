package com.hitori.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
public class RevenueStatisticsDto {
    private List<Object[]> bookingDetails;
    private BigDecimal totalPrice;

    public RevenueStatisticsDto(List<Object[]> bookingDetails, BigDecimal totalPrice) {
        this.bookingDetails = bookingDetails;
        this.totalPrice = totalPrice;
    }
}
