package com.zipple.common.oauth.viewer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@CrossOrigin
public class ViewController {

//    @GetMapping
//    public RedirectView kakaoPage() {
//        return new RedirectView("https://www.zipple.co.kr");
//    }
//
//    @GetMapping(value = "/swagger")
//    public RedirectView swaggerPage() {
//        return new RedirectView("https://www.zipple.co.kr");
//    }

    @GetMapping()
    public String pageFe() {
        return "index";
    }
}
