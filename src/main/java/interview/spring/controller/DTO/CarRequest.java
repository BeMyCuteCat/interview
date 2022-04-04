package interview.spring.controller.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class CarRequest {

    @NotEmpty
    @Length(max = 256, min = 1)
    private String userName;

    @NotEmpty
    @Length(max = 256, min = 1)
    @Pattern(regexp = "Toyota Camry|BMW 650")
    private String model;

    @NotNull
    @Min(value = 1)
    @Max(value = 100)
    private int num;
}