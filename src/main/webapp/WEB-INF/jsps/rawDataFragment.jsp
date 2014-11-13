<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<c:set var="rawDataId" value="${rawDataId+1}" scope="request"/>
<div id="rawData-${rawDataId}" style="display: none;">
<c:set var="lastNewLine" value="0"/>
Hex <a href="javascript:hideRawData(${rawDataId})" style="font-size: small">(Hide)</a>:
<c:forEach items="${data}" var="b" varStatus="cnt"><c:if test="${(cnt.count-lastNewLine)%60==0}"><br></c:if><span class="bytes b-${b.byteOffset}">${b.value}</span></c:forEach>
</div>