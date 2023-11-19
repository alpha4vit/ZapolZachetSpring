package by.gurinovich.ZapolZachetSpring.services.senders;

import by.gurinovich.ZapolZachetSpring.models.Group;
import by.gurinovich.ZapolZachetSpring.models.Subject;

public interface Sender {
    void send();

    void send(Group group);

    void send(Group group, Subject subject);
}
