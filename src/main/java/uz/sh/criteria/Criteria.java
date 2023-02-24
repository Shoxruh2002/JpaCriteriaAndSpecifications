package uz.sh.criteria;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Shoxruh Bekpulatov
 * Time : 24/02/23
 */
public interface Criteria extends Serializable {


    Map<Filter, String> getFilters();

    Map<String, String> getSelectedFields();

}
