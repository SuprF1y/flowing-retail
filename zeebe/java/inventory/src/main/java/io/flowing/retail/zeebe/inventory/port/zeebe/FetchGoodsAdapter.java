package io.flowing.retail.zeebe.inventory.port.zeebe;

import java.time.Duration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.zeebe.gateway.ZeebeClient;
import io.zeebe.gateway.api.clients.JobClient;
import io.zeebe.gateway.api.events.JobEvent;
import io.zeebe.gateway.api.subscription.JobHandler;
import io.zeebe.gateway.api.subscription.JobWorker;


@Component
public class FetchGoodsAdapter implements JobHandler {
  
  @Autowired
  private ZeebeClient zeebe;

  private JobWorker subscription;
  
  @PostConstruct
  public void subscribe() {
    subscription = zeebe.jobClient().newWorker()
      .jobType("fetch-goods-z")
      .handler(this)
      .timeout(Duration.ofMinutes(1))
      .open();      
  }

  @PreDestroy
  public void closeSubscription() {
    subscription.close();      
  }
  
  @Override
  public void handle(JobClient client, JobEvent job) {    
    System.out.println("fetch goods");
    client.newCompleteCommand(job).send().join();
  }

}
