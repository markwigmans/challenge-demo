package models.utils;

import org.apache.commons.lang3.builder.StandardToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 
 * @author Mark Wigmans
 * 
 */
public final class LogUtils {

    /**
     * standard style for toString methods.
     * 
     * @see Object#toString()
     * @see ToStringBuilder
     */
    public static final StandardToStringStyle STANDARD_STYLE = new StandardToStringStyle();
    public static final StandardToStringStyle MINIMAL_STYLE = new StandardToStringStyle();

    static {
        STANDARD_STYLE.setFieldSeparator(", ");
        STANDARD_STYLE.setUseShortClassName(true);

        MINIMAL_STYLE.setFieldSeparator(", ");
        MINIMAL_STYLE.setUseClassName(false);
        MINIMAL_STYLE.setUseIdentityHashCode(false);
    }

    /**
     * A utility class, so no instances are allowed.
     */
    private LogUtils() {
        throw new Error("Utility class, so no instances are allowed.");
    }
}
