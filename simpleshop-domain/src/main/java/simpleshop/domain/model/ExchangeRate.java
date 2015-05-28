package simpleshop.domain.model;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "exchange_rates", uniqueConstraints = @UniqueConstraint(columnNames = {"from_symbol", "to_symbol", "record_date"}))
public class ExchangeRate {

    private Integer id;
    private String fromSymbol;
    private String toSymbol;
    private LocalDateTime recordDate;
    private BigDecimal rate;


    @Id
    @GeneratedValue
    @Column(nullable = false, insertable = false, updatable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "from_symbol", length = 3, nullable = false, updatable = false)
    @NotNull
    public String getFromSymbol() {
        return fromSymbol;
    }

    public void setFromSymbol(String fromSymbol) {
        this.fromSymbol = fromSymbol;
    }

    @Column(name = "to_symbol", length = 3, nullable = false, updatable = false)
    @NotNull
    public String getToSymbol() {
        return toSymbol;
    }

    public void setToSymbol(String toSymbol) {
        this.toSymbol = toSymbol;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    //[convention]timezone of all date and time is the timezone of the underlying JVM.
    @Column(name = "record_date", insertable = false, updatable = false, columnDefinition = "datetime default getdate()")
    public LocalDateTime getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDateTime date) {
        this.recordDate = date;
    }

    @Column(name = "rate", nullable = false, precision = 18, scale = 6)
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    public BigDecimal getRate() {
        return rate;
    }

}
