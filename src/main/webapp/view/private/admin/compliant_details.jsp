<%@ page import="com.project.businesslogic.user.User" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Complaint details</title>
</head>
<body>
<div class="wrapper">

    <%--include header--%>
    <%@ include file="/view/public/common/header.jsp" %>

    <div class="content">
        <div class="left-bar">

        </div>
        <div class="central-bar">
            <div id="profileData" align="center">
                <h2 style="color: lightseagreen;">Complaint text</h2>
                <p>${complaint.text}</p>

                <form id="solve-complaint-button-form" action="${root}/job/complaint/solve" method="post">
                    <input class="button_blue" name="submit" size="60" value="Mark solved" type="submit" style="margin-top: 40px">
                    <input type="hidden" value="${complaint.id}" name="compliantId" />
                </form>
            </div>
        </div>
        <div class="right-bar">

        </div>
    </div>

    <%--include footer--%>
    <%@ include file="/view/public/common/footer.jsp" %>
</div>
</body>
</html>