package models;

import models.utils.LogUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A seat within a block.
 * 
 * @author Mark Wigmans
 * 
 */
public class Seat {

    public final int row;
    public final int seat;
    public final boolean available;
    public final String priceList;

    @JsonCreator
    public Seat(@JsonProperty(value = "row", required = true) final int row,
            @JsonProperty(value = "seat", required = true) final int seat,
            @JsonProperty(value = "available", required = true) final boolean available,
            @JsonProperty("priceList") final String priceList) {
        super();
        this.row = row;
        this.seat = seat;
        this.available = available;
        this.priceList = StringUtils.defaultString(priceList);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, LogUtils.STANDARD_STYLE);
    }
}
