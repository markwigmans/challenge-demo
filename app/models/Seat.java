package models;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Seat {

    public final int row;
    public final int seat;
    public final String priceList;

    @JsonCreator
    public Seat(@JsonProperty("row") final int row, @JsonProperty("seat") final int seat,
            @JsonProperty("priceList") final String priceList) {
        super();
        this.row = row;
        this.seat = seat;
        this.priceList = priceList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
