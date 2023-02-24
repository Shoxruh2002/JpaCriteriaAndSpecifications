package uz.sh.criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Shoxruh Bekpulatov
 * Time : 24/02/23
 */
public class BookCriteria implements Criteria {


    private Filter titleFilter;

    private Filter descriptionFilter;

    private Filter priceFilter;

    private Filter authorNameFilter;

    private Filter authorAgeFilter;

    private final List<Filter> filters = new ArrayList<>();


    public BookCriteria( Optional<String> titleFilter, Optional<String> descriptionFilter, Optional<Double> priceFilter, Optional<String> authorNameFilter, Optional<Integer> authorAgeFilter ) {
        titleFilter.ifPresent(s -> {
            this.titleFilter = new Filter(Filter.FilterMode.LIKE_TRIM_PERCENT, "title", s);
            this.filters.add(this.titleFilter);
        });
        descriptionFilter.ifPresent(s -> {
            this.descriptionFilter = new Filter(Filter.FilterMode.LIKE_TRIM_PERCENT, "description", s);
            this.filters.add(this.descriptionFilter);
        });
        priceFilter.ifPresent(s -> {
            this.priceFilter = new Filter(Filter.FilterMode.EQUALS, "price", s);
            this.filters.add(this.priceFilter);
        });
        authorAgeFilter.ifPresent(s -> {
            this.authorAgeFilter = new Filter(Filter.FilterMode.EQUALS, "age", s);
            this.filters.add(this.authorAgeFilter);
        });
        authorNameFilter.ifPresent(s -> {
            this.authorNameFilter = new Filter(Filter.FilterMode.LIKE_TRIM_PERCENT, "name", s);
            this.filters.add(this.authorNameFilter);
        });
    }


    @Override
    public List<Filter> getFilters() {
        return this.filters;
    }

    public Filter getTitleFilter() {
        return titleFilter;
    }

    public Filter getDescriptionFilter() {
        return descriptionFilter;
    }

    public Filter getPriceFilter() {
        return priceFilter;
    }

    public Filter getAuthorNameFilter() {
        return authorNameFilter;
    }

    public Filter getAuthorAgeFilter() {
        return authorAgeFilter;
    }
}
