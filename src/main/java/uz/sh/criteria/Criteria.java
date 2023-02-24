package uz.sh.criteria;

import java.io.Serializable;
import java.util.List;

/**
 * @author Shoxruh Bekpulatov
 * Time : 24/02/23
 */
public interface Criteria extends Serializable {


    List<Filter> getFilters();
}
