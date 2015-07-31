<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<%
    String basePath = request.getContextPath();
%>
<head>
    <title>Task List</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta charset="UTF-8"/>
    <script type="text/javascript" src="<%=basePath%>/static/js/jquery/jquery.js"></script>
    <script type="text/javascript" src="<%=basePath%>/static/js/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/static/js/main.js"></script>
</head>
<body>
<h1>Welcome</h1>
<a href="/logout" target="_self">logout</a>
</body>
</html>