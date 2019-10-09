<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>失败</title>
</head>
<body>
<p>
    请求失败了! 请校验参数
    <%=request.getAttribute("test") %> <br>

</p>
</body>
</html>
