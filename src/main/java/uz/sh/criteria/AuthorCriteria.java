package uz.sh.criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Shoxruh Bekpulatov
 * Time : 24/02/23
 */

public class AuthorCriteria implements Criteria {

    private Filter nameFilter;

    private Filter ageFilter;

    private final List<Filter> filters = new ArrayList<>();


    public AuthorCriteria( Optional<String> nameFilter, Optional<Integer> ageFilter ) {
        nameFilter.ifPresent(s -> {
            this.nameFilter = new Filter(Filter.FilterMode.LIKE_TRIM_PERCENT, "name", s);
            this.filters.add(this.nameFilter);
        });
        ageFilter.ifPresent(s -> {
            this.ageFilter = new Filter(Filter.FilterMode.EQUALS, "age", s);
            this.filters.add(this.ageFilter);
        });
    }

    @Override
    public List<Filter> getFilters() {
        return this.filters;
    }

    public Filter getNameFilter() {
        return nameFilter;
    }

    public Filter getAgeFilter() {
        return ageFilter;
    }
}
