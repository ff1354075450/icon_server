<%--
  Created by IntelliJ IDEA.
  User: ysk13
  Date: 2017-6-6
  Time: 10:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
%>
<html>
<head>
    <title>文件上传</title>
    <script type="text/javascript">
        i = 1;
        j = 1;
        $(document).ready(function () {

            $("#btn_add1").click(function () {
                document.getElementById("newUpload1").innerHTML += '<div id="div_' + i + '"><input  name="file" type="file"  /><input type="button" value="删除"  onclick="del_1(' + i + ')"/></div>';
                i = i + 1;
            });

            $("#btn_add2").click(function () {
                document.getElementById("newUpload2").innerHTML += '<div id="div_' + j + '"><input  name="file_' + j + '" type="file"  /><input type="button" value="删除"  onclick="del_2(' + j + ')"/></div>';
                j = j + 1;
            });
        });

        function del_1(o) {
            document.getElementById("newUpload1").removeChild(document.getElementById("div_" + o));
        }

        function del_2(o) {
            document.getElementById("newUpload2").removeChild(document.getElementById("div_" + o));
        }

    </script>
</head>
<body>
<h1>springMVC包装类上传文件</h1>
<form name="userForm2" action="<%=path%>/api/router/uploadImg" enctype="multipart/form-data" method="post">
    <div id="newUpload2">
        <input type="file" name="file">
    </div>
    <input type="button" id="btn_add2" value="增加一行">
    <input type="submit" value="上传">
</form>
</body>
</html>
