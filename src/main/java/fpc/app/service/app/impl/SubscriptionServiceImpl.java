package fpc.app.service.app.impl;

import fpc.app.model.app.Club;
import fpc.app.model.app.Subscription;
import fpc.app.model.auth.User;
import fpc.app.repository.app.SubscriptionRepository;
import fpc.app.service.app.SubscriptionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
  private final SubscriptionRepository subscriptionRepository;

  @Override
  public void subscribe(User user, Club club) {
    subscriptionRepository.save(Subscription.builder().club(club).user(user).build());
  }

  @Override
  public List<User> getSubscribers(Club club) {
    return subscriptionRepository.getUsersByClub(club.getId());
  }

  @Override
  public List<Club> getSubscriptions(User user) {
    return subscriptionRepository.getClubsByUserId(user.getId());
  }

  @Override
  public List<Long> getSubscriptionsIds(User user) {
    return subscriptionRepository.getClubsIdsByUserId(user.getId());
  }

  @Override
  public void unsubscribe(User user, Club club) {
    subscriptionRepository.deleteByUserIdAndClubId(user.getId(), club.getId());
  }
}
