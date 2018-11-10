package com.wiwj.screen.mvc;

import com.wiwj.screen.model.User;
import com.wiwj.screen.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class LoginController {
    @Resource
    private UserService userService;
    @Resource
    private HttpServletRequest request;

    @RequestMapping("login")
    public Map login(@RequestBody User user) {
        Map ret = new HashMap();
        user = userService.login(user);
        if (user == null) {
            ret.put("status", 0);
        } else {
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            ret.put("status", 1);
            ret.put("user", user.getName());
            ret.put("jsessionid", session.getId());
        }
        return ret;
    }

    @RequestMapping("logout")
    public int logout() {
        HttpSession session = request.getSession();
        if (session != null) {
            session.removeAttribute("user");
        }
        return 1;
    }

}
