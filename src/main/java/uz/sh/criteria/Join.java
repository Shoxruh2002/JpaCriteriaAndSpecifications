package uz.sh.criteria;

import jakarta.persistence.criteria.JoinType;
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
public class Join {

    private Class clazz;

    private String clazzField;

    private String joinField;

    private JoinType joinType;

}
