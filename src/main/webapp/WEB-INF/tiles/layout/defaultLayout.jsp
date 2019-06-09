<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    
    <!-- Bootstrap core CSS-->
    <link href="<c:url value="/vendor/bootstrap/css/bootstrap.min.css" />" rel="stylesheet">
    <!-- Custom fonts for this template-->
    <link href="<c:url value="/vendor/font-awesome/css/font-awesome.min.css" />" rel="stylesheet" type="text/css">
    <!-- Page level plugin CSS-->
    <link href="<c:url value="/vendor/datatables/dataTables.bootstrap4.css" />" rel="stylesheet">
    
    <!-- Custom styles for this template-->
    <link href="<c:url value="/vendor/sb-admin/css/sb-admin.css" />" rel="stylesheet">
    
    
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/reset.css" />">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/layout.css" />">
    <title><tiles:getAsString name="title" /></title>
    
    <!-- scripts placed on top instead of bottom -->
    <!-- so, each page included can use of this JavaScripts, ex jQuery -->
    <!-- Bootstrap core JavaScript-->
    <script src="<c:url value="/vendor/jquery/jquery.min.js" />"></script>
    <script src="<c:url value="/vendor/bootstrap/js/bootstrap.bundle.min.js" />"></script>
    <!-- Core plugin JavaScript-->
    <script src="<c:url value="/vendor/jquery-easing/jquery.easing.min.js" />"></script>
    <!-- Page level plugin JavaScript-->
    <script src="<c:url value="/vendor/datatables/jquery.dataTables.js" />"></script>
    <script src="<c:url value="/vendor/datatables/dataTables.bootstrap4.js" />"></script>
</head>
<body class="fixed-nav sticky-footer bg-dark">
    <tiles:insertAttribute name="header"/>
    <div class="content-wrapper">
        <div class="container-fluid">
            <tiles:insertAttribute name="content"/>
        </div>
        <footer class="sticky-footer">
            <div class="container">
                <tiles:insertAttribute name="footer"/>
            </div>
        </footer>
    </div>
    <!-- Custom scripts for all pages-->
    <script src="<c:url value="/vendor/sb-admin/js/sb-admin.js" />"></script>
    <!-- Custom scripts for this page-->
    <!-- script src="<c:url value="/vendor/sb-admin/js/sb-admin-datatables.min.js" />"></script -->
</body>
</html>