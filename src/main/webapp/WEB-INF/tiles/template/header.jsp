<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
    <!-- Navigation-->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top" id="mainNav">
        <a class="navbar-brand" href="<c:url value="/" />"><spring:message code="app.name" /></a>
        <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarResponsive">
            <ul class="navbar-nav navbar-sidenav" id="menuAccordion">
                <!-- Dashboard -->
                <li class="nav-item" data-toggle="tooltip" data-placement="right" title="Dashboard">
                    <a class="nav-link" href="<c:url value="/" />">
                        <i class="fa fa-fw fa-dashboard"></i>
                        <span class="nav-link-text"><spring:message code="common.home" /></span>
                    </a>
                </li>
                
                <!--  -->
                <li class="nav-item" data-toggle="tooltip" data-placement="right" title="Charts">
                    <a class="nav-link nav-link-collapse collapsed" data-toggle="collapse" href="#collapseComponents" data-parent="#menuAccordion">
                        <i class="fa fa-fw fa-microchip"></i>
                        <span class="nav-link-text"><spring:message code="nav.agents"/></span>
                    </a>
                    <ul class="sidenav-second-level collapse" id="collapseComponents">
<c:forEach var="agentName" items="${agents}">
                        <li>
                            <a class="nav-link" href="<c:url value="/agent/${agentName}" />">${agentName}</a>
                        </li>
</c:forEach>
                    </ul>
                </li>
                
                <!--  -->
                <li class="nav-item" data-toggle="tooltip" data-placement="right" title="Link">
                    <a class="nav-link" href="https://www.ossfsc.net/">
                        <i class="fa fa-fw fa-link"></i>
                        <span class="nav-link-text">ossfsc.net</span>
                    </a>
                </li>
            </ul>
            
            <!--  -->
            <ul class="navbar-nav sidenav-toggler">
                <li class="nav-item">
                    <a class="nav-link text-center" id="sidenavToggler">
                        <i class="fa fa-fw fa-angle-left"></i>
                    </a>
                </li>
            </ul>
            
            <!--  -->
            <ul class="navbar-nav ml-auto">
                <!--  -->
<sec:authorize access="isAuthenticated()">
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value="/userpage" />"><spring:message code="common.userpage"/></a>
                </li>
</sec:authorize>

                <!--  -->
<sec:authorize access="hasRole('ROLE_ADMIN')">
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value="/admin" />"><spring:message code="topmenu.admin" /></a>
                </li>
</sec:authorize>

                <!--  -->
                <li class="nav-item">
<sec:authorize access="!isAuthenticated()">
                    <a class="nav-link" href="<c:url value="/spring_security_login" />"><i class="fa fa-fw fa-sign-in"></i><spring:message code="common.login" /></a>
</sec:authorize>
<sec:authorize access="isAuthenticated()">
                    <a class="nav-link" href="<c:url value="/j_spring_security_logout" />">
                        <i class="fa fa-fw fa-sign-out"></i><spring:message code="common.logout" /></a>
</sec:authorize>
                </li>
            </ul>
        </div>
    </nav>