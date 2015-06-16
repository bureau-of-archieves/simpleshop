package simpleshop.dto;



import simpleshop.data.metadata.PropertyFilter;

import java.util.Date;

public class EmployeeSearch extends ContactSearch {

    private Date hireDateLower;
    private Date hireDateUpper;

    @PropertyFilter(property = "hireDate", operator = PropertyFilter.Operator.LESS, negate = true)
    public Date getHireDateLower() {
        return hireDateLower;
    }

    public void setHireDateLower(Date hireDateLower) {
        this.hireDateLower = hireDateLower;
    }

    @PropertyFilter(property = "hireDate", operator = PropertyFilter.Operator.GREATER, negate = true)
    public Date getHireDateUpper() {
        return hireDateUpper;
    }

    public void setHireDateUpper(Date hireDateUpper) {
        this.hireDateUpper = hireDateUpper;
    }

}
