package com.matthieu.dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping( value = "/comics" )
public class ComicsController {

	@RequestMapping( value = "/lister" , method = RequestMethod.GET )
    public ModelAndView listerComics() {
        final ModelAndView mav = new ModelAndView( "content/comics/index" );
        return mav;
    }
	
}
