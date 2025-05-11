package fpc.app.service.app;

import fpc.app.model.app.Club;
import fpc.app.model.auth.User;
import java.util.List;

public interface SubscriptionService {
  void subscribe(User user, Club club);

  List<User> getSubscribers(Club club);

  List<Club> getSubscriptions(User user);

  void unsubscribe(User user, Club club);

}
