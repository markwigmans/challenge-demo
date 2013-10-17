package models;

import models.utils.LogUtils;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Models a stadium.
 * 
 * @author Mark Wigmans
 * 
 */
public class Stadium {

    /**
     * Unique identifying a stadium
     */
    public final String id;

    /**
     * Human readable name
     */
    public final String name;

    @JsonCreator
    public Stadium(@JsonProperty(value = "id", required = true) final String id, @JsonProperty("name") final String name) {
        super();
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, LogUtils.STANDARD_STYLE);
    }
}