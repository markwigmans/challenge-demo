package models;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Block {

    /**
     * Unique identifying a block
     */
    public final String id;

    /**
     * Human readable name
     */
    public final String name;
    public final int rows;
    public final int seats;
    public final String defaultPrice;

    @JsonCreator
    public Block(@JsonProperty("id") final String id, @JsonProperty("name") final String name,
            @JsonProperty("rows") final int rows, @JsonProperty("seats") final int seats,
            @JsonProperty("defaultprice") final String defaultPrice) {
        super();
        this.id = id;
        this.name = name;
        this.seats = seats;
        this.rows = rows;
        this.defaultPrice = defaultPrice;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
