package uz.sh;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Shoxruh Bekpulatov
 * Time : 24/02/23
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDTO {

    private Long id;

    private String name;

    private Integer age;
}
