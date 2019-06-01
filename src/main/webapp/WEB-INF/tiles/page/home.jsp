<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<ul>
    <li><spring:message code="home.currenttemperature" /> : <span id="temperature">${temperature.temperature} *C (${temperature.date})</span></li>
    <li><spring:message code="home.currenthumidity" /> : <span id="humidity">${humidity.humidity} % (${humidity.date})</span></li>
</ul>