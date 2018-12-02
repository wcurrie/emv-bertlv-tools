package io.github.binaryfoo.controllers

import io.github.binaryfoo.RootDecoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import java.util.*

/**
 * Controller for handling the basic home page request
 */
@Controller
class HomeController {

  @RequestMapping("/home")
  fun welcome(): ModelAndView {
    val model = HashMap<String, Any>()
    model["tagInfos"] = RootDecoder.getSupportedTags()
    model["tagMetaSets"] = RootDecoder.getAllTagMeta()
    return ModelAndView("home", model)
  }

}
