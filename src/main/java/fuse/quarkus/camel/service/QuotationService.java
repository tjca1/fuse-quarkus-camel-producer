package fuse.quarkus.camel.service;


import com.fasterxml.jackson.databind.ObjectMapper;

import fuse.quarkus.camel.client.CurrencyPriceClient;
import fuse.quarkus.camel.dto.CurrencyPriceDTO;
import fuse.quarkus.camel.dto.QuotationDTO;
import fuse.quarkus.camel.entity.QuotationEntity;
import fuse.quarkus.camel.message.KafkaEvents;
import fuse.quarkus.camel.repository.QuotationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@ApplicationScoped
public class QuotationService {
	
	private final String USD_BRL = "USD-BRL";

    @Inject
    @RestClient
    CurrencyPriceClient currencyPriceClient;

    @Inject
    QuotationRepository quotationRepository;

    @Inject
    KafkaEvents kafkaEvents;
    
    public void sendKafka(){
    	 System.out.println("============================sendKafka======================================");
    	 kafkaEvents.sendNewKafkaEvent(QuotationDTO.builder()
                 .currencyPrice(new BigDecimal("0.0"))
                 .date(new Date())
                 .build()
         );
    	 System.out.println("=============================sendKafka=====================================");
    }
    
    public void consomeKafka(){
   	 System.out.println("===============================consomeKafka===================================");
   	 kafkaEvents.sendNewKafkaEvent(QuotationDTO.builder()
                .currencyPrice(new BigDecimal("0.0"))
                .date(new Date())
                .build()
        );
   	 System.out.println("===========================consomeKafka======================================");
   }

    public void getCurrentPrice(){
        try{
            CurrencyPriceDTO currencyPriceDTO = new CurrencyPriceDTO ();

            LinkedHashMap map = currencyPriceClient.getPriceByPairObject(USD_BRL);
            ObjectMapper mapper = new ObjectMapper();
            mapper.convertValue(currencyPriceDTO , CurrencyPriceDTO.class);

            if(updateCurrentInfoPrice(currencyPriceDTO)){
                kafkaEvents.sendNewKafkaEvent(QuotationDTO.builder()
                        .currencyPrice(new BigDecimal(currencyPriceDTO.getUSDBRL().getBid()))
                        .date(new Date())
                        .build()
                );
            }
        }catch(Exception ex) {
            ex.getMessage();


        }
        
       /*
        kafkaEvents.sendNewKafkaEvent(QuotationDTO.builder()
                        .currencyPrice(new BigDecimal("1"))
                        .date(new Date())
                        .build()

           );
       */





    }

    private Boolean updateCurrentInfoPrice(CurrencyPriceDTO currencyPriceDTO){
        QuotationEntity entity = new QuotationEntity();
        BigDecimal currentPrice = new BigDecimal(currencyPriceDTO.getUSDBRL().getBid());
        AtomicBoolean updatePrice = new AtomicBoolean(false);
        List<QuotationEntity> quotationList = quotationRepository.findAll().list();
        if(quotationList.isEmpty()){
            entity.setDate(new Date());
            entity.setCurrencyPrice(currentPrice);
            saveQuotation(currencyPriceDTO);
        }else{
            QuotationEntity lastDollarEntity = quotationList.get(quotationList.size() - 1);
            if(currentPrice.floatValue() > lastDollarEntity.getCurrencyPrice().floatValue()){
                updatePrice.set(true);
                saveQuotation(currencyPriceDTO);
            }

        }
        return updatePrice.get();
    }

    private void saveQuotation(CurrencyPriceDTO dto) {
        QuotationEntity entity = new QuotationEntity();
        entity.setDate(new Date());
        entity.setCurrencyPrice(new BigDecimal(dto.getUSDBRL().getBid()));
        entity.setPctChange(dto.getUSDBRL().getPctChange());
        entity.setPair("USD-BRL");
        quotationRepository.persist(entity);
    }

}
