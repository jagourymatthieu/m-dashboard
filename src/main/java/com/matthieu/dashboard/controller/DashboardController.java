package com.matthieu.dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DashboardController {

    @RequestMapping( "/" )
    public ModelAndView index() {
        final ModelAndView mav = new ModelAndView();
        mav.setViewName( "dashboard" );
        return mav;
    }
}
