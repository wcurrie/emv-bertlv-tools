<!DOCTYPE HTML>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<html>
<head>
  <title>TVR Decoder</title>
  <jsp:include page="head.jsp"/>
</head>
<body onload="yupWeAreLoaded();initialiseDecoding()">
<div id="container">
  <div id="content">
    <c:if test="${not embed}">
      <form onsubmit="doDecode();return false" class="form-inline">
        <select id="tag_field" onchange="onOptionChange()" class="form-control">
          <c:forEach items="${tagInfos}" var="tagInfo">
            <option value="${tagInfo.key}"
                    data-maxlength="${tagInfo.value.maxLength}"
                    data-longName="${tagInfo.value.longName}"
                    data-short="${tagInfo.value.shortBackground}"
                    data-long="${tagInfo.value.longBackground}">
                    ${tagInfo.value.shortName}
            </option>
          </c:forEach>
        </select>
        <textarea type="text" id="value_field" class="form-control" rows="1"></textarea>
        <input type="submit" value="Decode" class="btn btn-primary"/>
        <label for="tagmetaset_field" style="font-size:small">with tags</label>
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
      <small>What on earth is this all about? Data involved in <a href="http://en.wikipedia.org/wiki/EMV">credit
        card</a> card transactions. What does <a href="#" id="show-tag-background" class="tag-name" title="Show field explanation"></a> mean?
      </small>
      <div id="tag-background">
        <span class="tag-name"></span>:
        <span class="short-background"></span>
        <div class="long-background"></div>
        <a href="#" id="hide-tag-background" class="fa fa-close" title="Close"></a>
      </div>
    </c:if>
    <div id="display">
      <c:choose>
        <c:when test="${not empty value}">
          <jsp:include page="/t/decode?tag=${tag}&meta=${meta}&value=${value}"/>
        </c:when>
        <c:otherwise>
          <div id="example-list">
            Try an example:
            <ul>
              <li><a href="/t/decode/95/EMV/0000880000">Terminal Verification Results (TVR)</a></li>
              <li><a href="/t/decode/9B/EMV/ff00">Transaction Status Indicator</a></li>
              <li><a href="/t/decode/8E/EMV/0000000000000000410342031E031F02">Cardholder Verification Method List</a>
              </li>
              <li><a href="/t/decode/constructed/EMV/77299f2701009f360200419f2608c74d18b08248fefc9f10120110201009248400000000000000000029ff">Response to Generate AC</a></li>
            </ul>
            See how some of these elements appear on a <a href="/t/examples">receipt</a>.
          </div>
        </c:otherwise>
      </c:choose>
    </div>
    <div id="mouse-tip"></div>
  </div>
  <div id="footer">
    <p><a href="https://github.com/wcurrie/emv-bertlv-tools">Github</a></p>
  </div>
</div>
<jsp:include page="gaTracking.jsp"/>
<c:if test="${not empty value}">
  <script>
    $('#tag_field').val('<c:out value="${tag}"/>');
    $('#tagmetaset_field').val('<c:out value="${meta}"/>');
    $('#value_field').val(decodeURIComponent('<c:out value="${value}"/>'));
  </script>
</c:if>
</body>
</html>
