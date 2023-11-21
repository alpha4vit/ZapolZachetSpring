package by.gurinovich.ZapolZachetSpring.security;

import by.gurinovich.ZapolZachetSpring.models.User;
import by.gurinovich.ZapolZachetSpring.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("customSecurityExpression")
@RequiredArgsConstructor
public class CustomSecurityExpression {

    private final UserService userService;

    public boolean isEmailVerified(){
        User user = userService.getAuthenticatedUser();
        if (user != null){
            return user.isEmailVerified();
        }
        return false;
    }
}
