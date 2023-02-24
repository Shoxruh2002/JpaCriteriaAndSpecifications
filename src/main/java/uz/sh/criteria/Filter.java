package uz.sh.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Shoxruh Bekpulatov
 * Time : 24/02/23
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Filter {


    private FilterMode FilterMode;

    private String field;

    private Object value;

//    private String aliases;

    public enum FilterMode {
        EQUALS_AS_STRING,
        EQUALS,
        LIKE,
        LIKE_PREFIX_PERCENT,
        LIKE_SUFFIX_PERCENT,
        LIKE_TRIM_PERCENT,
    }
}
