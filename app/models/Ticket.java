package models;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Ticket {

    public final Seat seat;
    public final BigDecimal price;

    public Ticket(final Seat seat, final BigDecimal price) {
        super();
        this.seat = seat;
        this.price = price;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
