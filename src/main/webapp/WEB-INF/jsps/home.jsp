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
	<script src="/jquery-1.11.1.min.js"></script>
    <script src="/bootstrap.min.js"></script>
    <script src="/app.js"></script>
    <link rel="stylesheet" href="/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/css/bootstrap-theme.min.css"/>
    <link rel="stylesheet" href="/css/font-awesome-face.css"/>
    <link rel="stylesheet" href="/tvr.css" />
</head>
<body onload="yupWeAreLoaded()">
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
        <label id="popoverChoice" style="display: none">
            <input type="checkbox" id="hidePopovers" checked onchange="disablePopovers(this)"/>
            Show Popovers
        </label>
        <a href="" class="fa fa-link" id="link-back" style="display: none"></a>
    </form>
    <small>What on earth is this all about? Data involved in <a href="http://en.wikipedia.org/wiki/EMV">credit card</a> card transactions.</small>
    <div id="display">
        <div id="example-list">
            Try an example:
            <ul>
                <li><a href="/t/decode/95/EMV/0000880000">Terminal Verification Results (TVR)</a></li>
                <li><a href="/t/decode/9B/EMV/ff">Transaction Status Indicator</a></li>
                <li><a href="/t/decode/8E/EMV/0000000000000000410342031E031F02">Cardholder Verification Method List</a></li>
                <li><a href="/t/decode/constructed/EMV/77299f2701009f360200419f2608c74d18b08248fefc9f10120110201009248400000000000000000029ff">Response to Generate AC</a></li>
            </ul>
            See how some of these elements appear on a <a href="/t/examples">receipt</a>.
        </div>
    </div>
    <div id="mouse-tip"></div>
    </div>
    <div id="footer">
    	<p><a href="https://github.com/wcurrie/emv-bertlv-tools">Github</a></p>
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
    <c:if test="${not empty value}">
        <script>
            $('#tag_field').val('<c:out value="${tag}"/>');
            $('#tagmetaset_field').val('<c:out value="${meta}"/>');
            $('#value_field').val(decodeURIComponent('<c:out value="${value}"/>'));
            doDecode();
        </script>
    </c:if>
</body>
