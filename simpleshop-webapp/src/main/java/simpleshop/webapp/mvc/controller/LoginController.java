package simpleshop.webapp.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Login Controller.
 */
@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(){

        return "login";
    }

    @RequestMapping(value = "/loginFailed", method = RequestMethod.GET)
    public String loginFailed(Model model){

        model.addAttribute("error", true);
        return "forward:/login.do";
    }

    @RequestMapping(value = "/loggedOut", method = RequestMethod.GET)
    public String loggedOut(){

        return "loggedOut";
    }

    @RequestMapping(value = "403", method = RequestMethod.GET)
    public String notAuthorized(){

        return "403";
    }

}
