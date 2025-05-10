package fpc.app.component;

import fpc.app.repository.app.TicketOfferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EndSalesScheduler {

  private final TicketOfferRepository ticketOfferRepository;

  @Scheduled(cron = "0 * * * * *")
  public void markMissedAppointments() {}
}
