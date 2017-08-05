<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<html class=" js no-touch no-android chrome no-firefox no-iemobile no-ie no-ie10 no-ie11 no-ios">
<head>
    <meta charset="utf-8">
    <title>登录页面</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <link href="<%=path%>/css/login.css" rel="stylesheet">
    <link rel="stylesheet" href="<%=path%>/css/index.css" type="text/css">
</head>

<body>
<div class="home_bg" id="ha">
    <div class="logo2"><img src="<%=path%>/image/logo.png" width="353" height="187"></div>
    <div class="login">
        <div class="login_box">
            <form action="<%=path%>/auth/login" method="post">
                <ul>
                    <li>
                        <label class="tb username f_l"></label>
                        <input name="name" type="text" id="name" value="${name}" class="inputbox f_l"
                               placeholder="账号">
                    </li>
                    <li>
                        <label class="tb password f_l"></label>
                        <input name="password" type="password" id="password" value="${password}" maxlength="16"
                               class="inputbox f_l" placeholder="密码">
                    </li>
                    <li class="jzmm">
                        <input type="checkbox" checked="" value="checkbox">
                        <label class="jz">记住密码</label>

                        <c:if test="${!message}">
                            <span style="color: red;">${message}</span>
                        </c:if>
                    </li>
                    <li><input class="btn" type="submit" value="登录" id="login-submit"></li>
                </ul>
            </form>
        </div>
    </div>
</div>
</body>
</html>
