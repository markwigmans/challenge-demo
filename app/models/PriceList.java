package models;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PriceList {

    /**
     * Unique identifying a pricelist
     */
    public final String id;
    public final BigDecimal kid;
    public final BigDecimal adult;
    public final BigDecimal senior;

    public PriceList(final String id, final long kid, final long adult, final long senior) {
        this(id, BigDecimal.valueOf(kid), BigDecimal.valueOf(adult), BigDecimal.valueOf(senior));
    }

    public PriceList(final String id, final double kid, final double adult, final double senior) {
        this(id, BigDecimal.valueOf(kid), BigDecimal.valueOf(adult), BigDecimal.valueOf(senior));
    }

    @JsonCreator
    public PriceList(@JsonProperty("id") final String id, @JsonProperty("kid") final BigDecimal kid,
            @JsonProperty("adult") final BigDecimal adult, @JsonProperty("senior") final BigDecimal senior) {
        super();
        this.id = id;
        this.kid = kid;
        this.adult = adult;
        this.senior = senior;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public BigDecimal getPrice(final PriceCategory category) {
        switch (category) {
        case KID:
            return kid;
        case ADULT:
            return adult;
        case SENIOR:
            return senior;
        default:
            return null;
        }
    }
}
