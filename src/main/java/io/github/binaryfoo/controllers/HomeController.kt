package io.github.binaryfoo.controllers

import java.util.HashMap

import io.github.binaryfoo.RootDecoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

/**
 * Controller for handling the basic home page request
 */
@Controller class HomeController {

    @RequestMapping("/home") fun welcome(): ModelAndView {
        val model = HashMap<String, Any>()
        model.put("tagInfos", RootDecoder.getSupportedTags())
        model.put("tagMetaSets", RootDecoder.getAllTagMeta())
        return ModelAndView("home", model)
    }

}