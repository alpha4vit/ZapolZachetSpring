package by.gurinovich.ZapolZachetSpring.utils.validotors;

import by.gurinovich.ZapolZachetSpring.models.Group;
import by.gurinovich.ZapolZachetSpring.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class GroupValidator implements Validator {
    private final GroupService groupService;

    @Autowired
    public GroupValidator(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(Group.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Group group = (Group) target;
        if (groupService.findByName(group.getName()) != null)
            errors.rejectValue("name", "", "Group with this name is already exists");
    }
}
