package io.github.binaryfoo.controllers

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.HexDumpFactory
import io.github.binaryfoo.RootDecoder
import io.github.binaryfoo.TagInfo
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*

import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.Collections
import java.util.HashMap
import java.util.logging.Level
import java.util.logging.Logger

Controller
public class DecodeController {

    private val hexDumpFactory = HexDumpFactory()
    private val rootDecoder = RootDecoder()

    RequestMapping(value = array("/decode"), method = array(RequestMethod.POST, RequestMethod.GET))
    public fun decode(RequestParam tag: String, RequestParam value: String, RequestParam meta: String, modelMap: ModelMap): String {
        try {
            decodeInto(tag, value, meta, modelMap)
            modelMap.addAttribute("noisy", NoisyTagList.noisyOnes())
            return "decodedData"
        } catch (e: DecodeFailedException) {
            modelMap.addAttribute("error", e.getMessage())
            return "validationError"
        }
    }

    RequestMapping(value = array("/decode/{tag}/{meta}/{value:(?s).+}"), method = array(RequestMethod.GET))
    public fun linkToDecode(PathVariable tag: String, PathVariable value: String, PathVariable meta: String, modelMap: ModelMap, RequestParam(required = false) embed: Boolean): String {
        modelMap.put("tag", tag)
        modelMap.put("value", URLEncoder.encode(value, "UTF-8"))
        modelMap.put("meta", meta)
        modelMap.put("tagInfos", RootDecoder.getSupportedTags())
        modelMap.put("tagMetaSets", RootDecoder.getAllTagMeta())
        modelMap.put("embed", embed)
        return "home"
    }

    RequestMapping(value = array("/api/decode"), produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseBody
    public fun decodeJson(RequestParam tag: String, RequestParam value: String, RequestParam meta: String): Map<String, Any> {
        val response = HashMap<String, Any>()
        decodeInto(tag, value, meta, response)
        return response
    }

    private fun decodeInto(tag: String, value: String, meta: String, response: MutableMap<String, Any>) {
        LOG.info("Request to decode tag [" + tag + "] and value [" + value + "] and meta [" + meta + "]")
        val tagInfo = RootDecoder.getTagInfo(tag)
        if (tagInfo == null) {
            LOG.fine("Unknown tag")
            throw DecodeFailedException("Unknown tag")
        }
        val error = tagInfo.decoder.validate(value)
        if (error != null) {
            LOG.fine("Validation error " + error)
            throw DecodeFailedException(error)
        }
        try {
            val upperCaseValue = value.toUpperCase()
            val decodedData = rootDecoder.decode(upperCaseValue, meta, tagInfo)
            LOG.fine("Decoded successfully $decodedData")
            if (decodedData.empty || decodedData[0].hexDump == null) {
                response.put("rawData", hexDumpFactory.splitIntoByteLengthStrings(upperCaseValue.replaceAll(":", " "), 0))
            }
            response.put("decodedData", decodedData)
        } catch (e: Exception) {
            e.printStackTrace()
            LOG.log(Level.FINE, "Error decoding", e)
            throw DecodeFailedException(getMessageTrace(e))
        }

    }

    ResponseStatus(value = HttpStatus.BAD_REQUEST)
    ResponseBody
    public fun handleException(e: DecodeFailedException): Map<String, String> {
        return mapOf("error" to (e.getMessage() ?: e.javaClass.getSimpleName()))
    }

    private fun getMessageTrace(e: Exception): String {
        val b = StringBuilder(if (e.getMessage() != null) e.getMessage() else "Either something has gone wrong or you've given me rubbish")
        var t: Throwable? = e.getCause()
        while (t != null) {
            b.append(", ").append(t)
            t = t!!.getCause()
        }
        return b.toString()
    }

    class object {

        private val LOG = Logger.getLogger(javaClass<DecodeController>().getName())
    }

}
