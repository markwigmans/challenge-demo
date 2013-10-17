package models;

import java.util.Collections;
import java.util.Map;

import models.utils.LogUtils;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;

/**
 * Price lists global for all stadiums.
 * 
 * @author Mark Wigmans
 * 
 */
public class PriceList {

    public enum PriceCategory {
        kids, adults, seniors
    }

    /**
     * Unique identifying a pricelist
     */
    public final String id;
    public final String name;
    public final Map<PriceCategory, Integer> pricecategories;

    @JsonCreator
    public PriceList(@JsonProperty(value = "id", required = true) final String id, @JsonProperty("name") final String name,
            @JsonProperty("pricecategories") final Map<PriceCategory, Integer> priceCategories) {
        super();
        this.id = id;
        this.name = name;
        this.pricecategories = Collections.unmodifiableMap(priceCategories);
    }

    /**
     * Helper constructor for testing purposes.
     */
    public PriceList(final String id, final double kids, final double adults, final double seniors) {
        this.id = id;
        this.name = id;
        final Map<PriceCategory, Integer> map = Maps.newHashMap();
        map.put(PriceCategory.kids, Double.valueOf(kids * 100.0).intValue());
        map.put(PriceCategory.adults, Double.valueOf(adults * 100.0).intValue());
        map.put(PriceCategory.seniors, Double.valueOf(seniors * 100.0).intValue());
        this.pricecategories = Collections.unmodifiableMap(map);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, LogUtils.STANDARD_STYLE);
    }

    public Integer getPrice(final PriceCategory category) {
        return pricecategories.get(category);
    }
}
