package fuse.quarkus.camel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.util.Date;

@Jacksonized
@Builder
@Data
@AllArgsConstructor
public class QuotationDTO {
    private Date date;
    private BigDecimal currencyPrice;

}
