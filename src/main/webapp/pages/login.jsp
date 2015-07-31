<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html xmlns:th="http://www.thymeleaf.org">
<c:set var="basePath" value="${pageContext.request.contextPath }"/>
<%
    String basePath = request.getContextPath();
%>
<head>
    <title>Notification</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta charset="UTF-8"/>
    <script type="text/javascript" src="${basePath}/static/js/jquery/jquery.js"></script>
    <script type="text/javascript" src="${basePath}/static/js/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/js/main.js"></script>
</head>
<body>
<h1>Login</h1>

<form action="/" method="post">
    <h2>Email Information</h2>
    <label>Host: </label>
    <select name="host">
        <option value="webmail.in.telstra.com.au">Internal Telstra Inbox</option>
        <option value="corpmail.as.telstra.com.au">External Telstra Inbox</option>
        <option value="imap.gmail.com">Thoughtworks Inbox</option>
    </select>
    <br/>
    <label>Username: </label><input type="text" name="userName"/><br/>
    <label>Password: </label><input type="password" name="password"/><br/>

    <h2>Privacy Setting</h2>
    <label>Nick Name: </label><input type="text" name="nickname"/><br/>

    <h2>Email Rule</h2>
    <label>Not Implemented Yet</label><br/>
    <button id="saveCredential">Save</button>
</form>
</body>
</html>