package models;

import models.PriceList.PriceCategory;
import models.utils.LogUtils;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The result of buying an seat for a given stadium.
 * 
 * @author Mark Wigmans
 * 
 */
public class Ticket {

    public final String requestId;
    public final Seat seat;
    public final PriceCategory category;
    public final Integer price;

    public Ticket(final String requestId, final Seat seat, final PriceCategory category, final Integer price) {
        super();
        this.requestId = requestId;
        this.seat = seat;
        this.category = category;
        this.price = price;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, LogUtils.STANDARD_STYLE);
    }
}
