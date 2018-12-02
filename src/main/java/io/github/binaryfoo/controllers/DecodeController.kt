package io.github.binaryfoo.controllers

import io.github.binaryfoo.RootDecoder
import io.github.binaryfoo.hex.HexDumpElement
import io.github.binaryfoo.toSimpleString
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*
import java.net.URLEncoder
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

@Controller
class DecodeController {

  private val logger = Logger.getLogger(DecodeController::class.java.name)
  private val rootDecoder = RootDecoder()

  @RequestMapping("/decode", method = [RequestMethod.POST, RequestMethod.GET])
  fun decode(@RequestParam tag: String, @RequestParam value: String, @RequestParam meta: String, modelMap: ModelMap): String {
    return try {
      decodeInto(tag, value, meta, modelMap)
      modelMap.addAttribute("noisy", NoisyTagList.noisyOnes())
      "decodedData"
    } catch (e: DecodeFailedException) {
      modelMap.addAttribute("error", e.message)
      "validationError"
    }
  }

  @RequestMapping("/decode/{tag}/{meta}/{value:(?s).+}", method = [RequestMethod.GET])
  fun linkToDecode(@PathVariable tag: String, @PathVariable value: String, @PathVariable meta: String, modelMap: ModelMap, @RequestParam(required = false) embed: Boolean): String {
    modelMap["tag"] = tag
    modelMap["value"] = URLEncoder.encode(value, "UTF-8")
    modelMap["meta"] = meta
    modelMap["tagInfos"] = RootDecoder.getSupportedTags()
    modelMap["tagMetaSets"] = RootDecoder.getAllTagMeta()
    modelMap["embed"] = embed
    return "home"
  }

  @RequestMapping("/api/decode", produces = [MediaType.APPLICATION_JSON_VALUE])
  @ResponseBody
  fun decodeJson(@RequestParam tag: String, @RequestParam value: String, @RequestParam meta: String): Map<String, Any> {
    val response = HashMap<String, Any>()
    decodeInto(tag, value, meta, response)
    return response
  }

  private fun decodeInto(tag: String, value: String, meta: String, response: MutableMap<String, Any>) {
    logger.info("Request to decode tag [$tag] and value [$value] and meta [$meta]")
    val tagInfo = RootDecoder.getTagInfo(tag)
    if (tagInfo == null) {
      logger.fine("Unknown tag")
      throw DecodeFailedException("Unknown tag")
    }
    val error = tagInfo.decoder.validate(value)
    if (error != null) {
      logger.fine("Validation error $error")
      throw DecodeFailedException(error)
    }
    try {
      val upperCaseValue = value.toUpperCase()
      val decodedData = rootDecoder.decode(upperCaseValue, meta, tagInfo)
      logger.fine("Decoded successfully ${decodedData.toSimpleString()}")
      if (decodedData.isEmpty() || decodedData[0].hexDump == null) {
        response["rawData"] = HexDumpElement.splitIntoByteLengthStrings(upperCaseValue.replace(":", " "), 0)
      }
      response["decodedData"] = decodedData
    } catch (e: Exception) {
      e.printStackTrace()
      logger.log(Level.FINE, "Error decoding", e)
      throw DecodeFailedException(getMessageTrace(e))
    }

  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ResponseBody
  fun handleException(e: DecodeFailedException): Map<String, String> {
    return mapOf("error" to (e.message ?: e.javaClass.simpleName))
  }

  private fun getMessageTrace(e: Exception): String {
    val b = StringBuilder(if (e.message != null) e.message else "Either something has gone wrong or you've given me rubbish")
    var t = e.cause
    while (t != null) {
      b.append(", ").append(t)
      t = t.cause
    }
    return b.toString()
  }

}
