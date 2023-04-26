package by.gurinovich.ZapolZachetSpring.models;

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
