package ma.xproce.syndicconnect.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;

@Controller
public class AuthController {

    @GetMapping("/redirect")
    public String redirectAfterLogin(Authentication authentication) {

        String role = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority(); // ex: ROLE_SYNDIC

        if (role.equals("ROLE_SYNDIC")) {
            return "redirect:/syndic/home";
        } else if (role.equals("ROLE_RESIDENT")) {
            return "redirect:/resident/home";
        }

        return "redirect:/login";
    }
}
