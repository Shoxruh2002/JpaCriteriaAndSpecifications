package uz.sh.criteria;

import jakarta.validation.constraints.NotNull;
import uz.sh.Author;

import java.util.*;

/**
 * @author Shoxruh Bekpulatov
 * Time : 24/02/23
 */

public class AuthorCriteria implements Criteria {

    private Filter nameFilter;

    private Filter ageFilter;

    private final Map<Filter, String> filters = new HashMap<>();

    private final Map<String, String> selectedFields;


    public AuthorCriteria( @NotNull List<String> selectedFields, Optional<String> nameFilter, Optional<Integer> ageFilter ) {
        nameFilter.ifPresent(s -> {
            this.nameFilter = new Filter(Filter.FilterMode.LIKE_TRIM_PERCENT, "name", s);
            this.filters.put(this.nameFilter, Author.class.getSimpleName().toLowerCase());
        });
        ageFilter.ifPresent(s -> {
            this.ageFilter = new Filter(Filter.FilterMode.EQUALS, "age", s);
            this.filters.put(this.ageFilter, Author.class.getSimpleName().toLowerCase());
        });
        if ( selectedFields.size() == 0 )
            throw new RuntimeException("Cannot select 0 field");
        Map<String, String> map = new LinkedHashMap<>();
        for ( String f : selectedFields ) {
            map.put(f, Author.class.getSimpleName().toLowerCase());
        }
        this.selectedFields = map;

    }

    @Override
    public Map<Filter, String> getFilters() {
        return this.filters;
    }

    @Override
    public Map<String, String> getSelectedFields() {
        return this.selectedFields;
    }

    public Filter getNameFilter() {
        return nameFilter;
    }

    public Filter getAgeFilter() {
        return ageFilter;
    }
}
