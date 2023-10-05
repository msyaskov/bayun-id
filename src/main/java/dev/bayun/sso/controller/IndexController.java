package dev.bayun.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Максим Яськов
 */

@Controller
public class IndexController {

    @GetMapping(path = "/")
    public String getIndexPage() {
        return "redirect:/main";
    }

}
