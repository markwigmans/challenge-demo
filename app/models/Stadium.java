package models;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    public Stadium(@JsonProperty("id") final String id, @JsonProperty("name") final String name) {
        super();
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}