package by.gurinovich.ZapolZachetSpring.DTO;

import by.gurinovich.ZapolZachetSpring.models.Group;
import by.gurinovich.ZapolZachetSpring.models.Subject;

public class GroupAndSubject {
    private Group group;
    private Subject subject;

    public Subject getSubject() {
        return subject;
    }

    public GroupAndSubject(Group group, Subject subject) {
        this.group = group;
        this.subject = subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
