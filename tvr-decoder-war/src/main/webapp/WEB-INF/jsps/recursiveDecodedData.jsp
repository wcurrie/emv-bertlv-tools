<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<c:forEach items="${decodedData}" var="item">
    <c:if test="${item.hexDump != null}">
        <c:set var="data" value="${item.hexDump}" scope="request"/>
        <jsp:include page="rawDataFragment.jsp"/>
    </c:if>
	<c:if test="${not item.composite}">
		<div class="decoded" data-s="${item.startIndex}" data-e="${item.endIndex}" data-i="${rawDataId}">
            <c:if test="${not empty item.rawData}"><span class="rawData" data-category="${item.category}">${item.rawData}</span></c:if>
            <span class="decodedData">${item.decodedData}</span>
        </div>
    </c:if>
	<c:if test="${item.composite}">
        <c:set var="itemId" value="${item.startIndex}-${item.endIndex}" scope="request"/>
        <div class="composite-decoded" data-s="${item.startIndex}" data-e="${item.endIndex}" data-i="${rawDataId}">
            <div>
                <span class="composite-label">
                    <span data-category="${item.category}">${item.rawData}</span>
                    <span class="glyphicon glyphicon-zoom-${noisy[item.tag] ? 'in' : 'out'} expander" data-item="${itemId}"></span>
                    <span class="composite-chunked" data-item="${itemId}">${item.decodedData}</span>
                </span>
            </div>
            <div class="detail" data-item="${itemId}">
                <div class="indent">
                    <c:set var="decodedData" value="${item.children}" scope="request"/>
                    <jsp:include page="recursiveDecodedData.jsp"/>
                </div>
            </div>
        </div>
    </c:if>
</c:forEach>
