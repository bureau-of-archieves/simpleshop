package simpleshop.dto;



import simpleshop.data.metadata.PropertyFilter;

import java.time.LocalDate;
import java.util.Date;

public class EmployeeSearch extends ContactSearch {

    private LocalDate hireDateLower;
    private LocalDate hireDateUpper;

    @PropertyFilter(property = "hireDate", operator = PropertyFilter.Operator.LESS, negate = true)
    public LocalDate getHireDateLower() {
        return hireDateLower;
    }

    public void setHireDateLower(LocalDate hireDateLower) {
        this.hireDateLower = hireDateLower;
    }

    @PropertyFilter(property = "hireDate", operator = PropertyFilter.Operator.GREATER, negate = true)
    public LocalDate getHireDateUpper() {
        return hireDateUpper;
    }

    public void setHireDateUpper(LocalDate hireDateUpper) {
        this.hireDateUpper = hireDateUpper;
    }

}
