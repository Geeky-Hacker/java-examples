package com.geekyhacker.jpaprojections;

import java.math.BigDecimal;

public record OrderRow(Long id, String status, BigDecimal total, String customerName) {
}
