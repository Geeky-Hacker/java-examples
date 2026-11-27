package com.geekyhacker.jpaprojections;

import java.math.BigDecimal;

/**
 * Closed interface projection over root properties only.
 */
public interface OrderSummary {

    Long getId();

    String getStatus();

    BigDecimal getTotal();
}
