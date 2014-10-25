<!DOCTYPE HTML>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<html>
<head>
	<title>TVR Decoder</title>
	<meta name="google-site-verification" content="slrW544StiiGPgULPcN6D3hvx-BCNi8ch9PXDwCceJk" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="description" content="Decoder tool for EMV credit card data. Started with TVR (Terminal Verification Results) and grew to an APDU trace."/>
    <meta name="og:title" content="TVR Decoder"/>
    <meta name="og:description" content="Decoder tool for EMV credit card data. Started with TVR (Terminal Verification Results) and grew to an APDU trace."/>
    <meta name="og:image" content="http://tvr-decoder.appspot.com/chip.png"/>
	<script src="/jquery-1.3.2.min.js" type="text/javascript"></script>
    <script type="text/javascript">
	    function doDecode() {
	        $("#display").slideUp('slow',function(){
                $("#display").html("Loading...").show();
                var tag = $('#tag_field').val();
                var meta = $('#tagmetaset_field').val();
                if (ga) {
                    ga('send', 'event', tag, meta);
                }
                $.post('/t/decode', {tag: tag, value: $('#value_field').val(),meta: meta},function(data) {
					$("#display").html(data).slideDown('slow',function() {
						$(".decoded,.composite-decoded").each(function() {
			    			$(this).click(function(e) {
			    			    highlightBytes($(this));
								e.stopPropagation();
							});
				    	});
                        $(".expander").each(function() {
                            $(this).click(function(e) {
                                toggleExpander($(this));
                                e.stopPropagation();
                            });
                            initExpander($(this));
                        })
			    	});
                    $('[data-category="c-apdu"].apdu-label').prepend('<i class="fa fa-calculator"></i> ');
                    $('[data-category="r-apdu"].apdu-label').before('<i class="fa fa-credit-card"></i> ');
		    	});
            });
	    }
	
	    function onOptionChange() {
	        var value_field = $('#value_field')
	        var options = $('#tag_field').attr("options");
	        var selectedIndex = $('#tag_field').attr("selectedIndex");
	        var maxLength = options[selectedIndex].getAttribute("data-maxlength");
	        value_field.attr("maxLength", maxLength);
	        if (value_field.val().length > maxLength) {
	            value_field.val(value_field.val().substr(0, maxLength))
	        }
	    }

	    var highlighted=null;

		function highlightBytes(decoded) {
		    clearHighlight();
		    decoded.addClass("highlight");
		    highlighted=decoded;

            var rawDataId=decoded.attr("data-i");
            var start=+decoded.attr("data-s");
            var end=+decoded.attr("data-e");
			$("#rawData-"+rawDataId).show("fast");
			var i = 0;
			for (i = start; i < end; i++) {
				$("#b-" + i).addClass("highlight");
			}
		}

        function clearHighlight() {
            if (highlighted!=null) {
                highlighted.removeClass("highlight");
            }
            highlighted=null;
            $(".bytes").removeClass("highlight");
        }

		function hideRawData(rawDataId) {
		    clearHighlight();
			$("#rawData-"+rawDataId).hide("fast");
		}

        function getExpander(e) {
            var itemId = e[0].getAttribute("data-item");
            var collapsed = e.hasClass("glyphicon-zoom-in");
            var detail = $('.detail[data-item="' + itemId + '"]');
            var chunk = $('.composite-chunked[data-item="' + itemId + '"]');
            return {collapsed: collapsed, detail: detail, chunk: chunk};
        }

        function initExpander(e) {
            var expander = getExpander(e);
            if (expander.collapsed) {
                expander.detail.addClass("collapsed");
                expander.chunk.addClass("expanded");
            } else {
                expander.detail.addClass("expanded");
                expander.chunk.addClass("collapsed");
            }
        }

        function toggleExpander(e) {
            var expander = getExpander(e);
            if (expander.collapsed) {
                expander.detail.addClass("expanded").removeClass("collapsed");
                expander.chunk.removeClass("expanded").addClass("collapsed");
                e.removeClass("glyphicon-zoom-in").addClass("glyphicon-zoom-out")
            } else {
                expander.detail.removeClass("expanded").addClass("collapsed");
                expander.chunk.addClass("expanded").removeClass("collapsed");
                e.removeClass("glyphicon-zoom-out").addClass("glyphicon-zoom-in")
            }
        }

    </script>
    <link rel="stylesheet" href="/tvr.css" />
    <link rel="stylesheet" href="/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/css/bootstrap-theme.min.css"/>
    <link rel="stylesheet" href="/css/font-awesome.min.css"/>
</head>
<body onload="onOptionChange()">
    <div id="container"><div id="content">
    <form onsubmit="doDecode();return false" class="form-inline">
    <select id="tag_field" onchange="onOptionChange()" class="form-control">
    <c:forEach items="${tagInfos}" var="tagInfo">
        <option value="${tagInfo.key}" data-maxlength="${tagInfo.value.maxLength}">${tagInfo.value.shortName}</option>
    </c:forEach>
    </select>
    <textarea type="text" id="value_field" class="form-control" rows="1"></textarea>
    <input type="submit" value="Decode" class="btn btn-primary"/>
    <label for="tagmetaset_field"  style="font-size:small">with tags</label>
    <select id="tagmetaset_field" class="form-control">
    <c:forEach items="${tagMetaSets}" var="tagMeta">
        <option value="${tagMeta}">${tagMeta}</option>
    </c:forEach>
    </select>
    </form>
        <small>What on earth is this all about? Data involved in <a href="http://en.wikipedia.org/wiki/EMV">credit card</a> card transactions...</small>
    <div id="display">
    </div>
    </div>
    <div id="footer">
    	<p><a href="https://github.com/wcurrie/emv-bertlv">Project</a></p>
    </div>
    </div>
    <script>
        (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
            (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
                m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
        })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

        ga('create', 'UA-7717677-1', 'auto');
        ga('send', 'pageview');
    </script>
</body>
