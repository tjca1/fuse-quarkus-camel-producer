package fuse.quarkus.camel.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "quotation")
@Data
@NoArgsConstructor
public class QuotationEntity {

    @Id
    @GeneratedValue
    private Long id ;
    @Column(name = "date")
    private Date date;
    @Column(name = "current_price")
    private BigDecimal currencyPrice;
    @Column(name = "pct_change")
    private String pctChange;
    @Column(name = "pair")
    private String pair;
}
