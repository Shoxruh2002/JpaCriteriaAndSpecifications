package uz.sh.criteria;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Shoxruh Bekpulatov
 * Time : 24/02/23
 */
@Data
public class AuthorCriteria implements Criteria {

    private Filter nameFilter;

    private Filter ageFilter;

    private List<Filter> filters = new ArrayList<>();

    public AuthorCriteria( Optional<String> nameFilter, Optional<Integer> ageFilter ) {
        nameFilter.ifPresent(s -> {
            this.nameFilter = new Filter(Filter.FilterMode.LIKE_TRIM_PERCENT, "name", s);
            filters.add(this.nameFilter);
        });
        ageFilter.ifPresent(s -> {
            this.ageFilter = new Filter(Filter.FilterMode.EQUALS, "age", s);
            filters.add(this.ageFilter);
        });
    }
}
