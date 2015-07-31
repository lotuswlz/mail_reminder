<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<%
    String basePath = request.getContextPath();
%>
<head>
    <title>Notification</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta charset="UTF-8"/>
    <script type="text/javascript" src="<%=basePath%>/static/js/jquery/jquery.js"></script>
    <script type="text/javascript" src="<%=basePath%>/static/js/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/static/js/main.js"></script>
</head>
<body>
<h1>Welcome</h1>
    <label>Username: </label><span id="userName"></span><br/>
</body>
<script type="text/javascript">
    $(document).ready(function() {
        $.ajax({
            type: "GET",
            url: "/credential",
            contentType: "application/json; charset=utf-8",
            success: function (user) {
                var userName = user.userName;
                $("#userName").html(userName);
            },
            error: function (e) {
                alert("Error: " + e.message);
            }
        });
    });
</script>
</html>