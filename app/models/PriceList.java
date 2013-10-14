package models;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import models.utils.LogUtils;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;

/**
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

    public PriceList(String id, double kids, double adults, double seniors) {
        this.id = id;
        this.name = id;
        final Map<PriceCategory, Integer> map = Maps.newHashMap();
        map.put(PriceCategory.kids, new BigDecimal(kids * 100).intValue());
        map.put(PriceCategory.adults, new BigDecimal(adults * 100).intValue());
        map.put(PriceCategory.seniors, new BigDecimal(seniors * 100).intValue());
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
