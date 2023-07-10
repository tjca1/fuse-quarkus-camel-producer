package fuse.quarkus.camel.scheduler;


import fuse.quarkus.camel.service.QuotationService;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class QuotationScheduter {

    @Inject
    QuotationService service;

    @Transactional
    @Scheduled(every = "30s" , identity = "task-job")
    void schedule(){
    	System.out.println("==================================================================");
    	System.out.println("==================================================================");
    	
        service.sendKafka();
        
    }
}
