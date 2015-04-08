<%@ page import="com.project.businesslogic.user.User" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin profile</title>
</head>
<body>
    <div class="wrapper">

        <%--include header--%>
        <%@ include file="/view/public/common/header.jsp" %>

            <div class="content">
                <div class="left-bar">
                    <h4>Active complaints</h4>
                    <c:forEach items="${activeCompliants}" var="complaint">
                        <div class="jobItem" style="font-size: smaller; margin-left: 8px; width: 200px;">
                            <div style="background: red; color: white; border-radius: 7px 7px 7px 7px">
                                <p style="text-align: center;font-size:
                                medium;margin-top: auto;">
                                    <b>${complaint.problemJob.title}</b></p>
                            </div>
                            <div style="margin-left: 10px; margin-right: 10px">
                                <b>User type: </b>${complaint.user.userType} <br>
                                <b>Publish date: </b>${complaint.date} <br>
                                <b>User email: </b>${complaint.user.email} <br>
                            </div>
                            <form action="${root}/jobs/options" method="get" style="margin-top: 10px; float: bottom">
                                <div align="center">
                                    <input type="hidden" name="jobId" value="${complaint.problemJob.id}"/>
                                    <button class="button_example" type="submit">Job details</button>
                                </div>
                            </form>
                            <form action="${root}/job/complaint/options" method="post" style="margin-top: 10px; float: bottom">
                                <div align="center">
                                    <input type="hidden" name="compliantId" value="${complaint.id}"/>
                                    <button class="button_example" type="submit">Complaint options</button>
                                </div>
                            </form>
                        </div>
                    </c:forEach>
                </div>
                <div class="central-bar">
                    <div id="profileData" align="center">
                        <img id="userLogo" src="${root}/user/image/${adminUser.id}" style="width: 175px;
                                                                                     height: 175px;
                                                                                     margin-top: 10px;
                                                                                     border: solid 3px #4b4b4b;
                                                                                     border-radius: 10px 10px 10px 10px;"/>
                        <h2>${adminUser.snf}</h2>
                        <p><b>Email: </b>${adminUser.email}</p>

                        <%  User user = (User) request.getAttribute("adminUser");
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            String newFormatBirthday = sdf.format(user.getBirthday());
                        %>
                        <p><b>DOB: </b><%=newFormatBirthday%></p>
                        <form id="edit-profile-button-form" action="${root}/usr/admin/profileDetails" method="get">
                            <input class="button_blue" name="submit" size="60" value="Edit my profile" type="submit" style="margin-top: 40px">
                            <input type="hidden" value="<sec:authentication property="principal.id" />" name="userId" />
                        </form>
                        <%--<form id="messages-button-form" action="${root}/messages/show" method="post">--%>
                            <%--<input class="button_blue" name="submit" size="60" value="My messages" type="submit">--%>
                            <%--<input type="hidden" value="<sec:authentication property="principal.id"/>" name="myId" />--%>
                        <%--</form>--%>
                    </div>
                </div>
                <div class="right-bar">
                    <h4>Solved complaints</h4>
                    <c:forEach items="${solvedCompliants}" var="complaint">
                        <div class="jobItem" style="font-size: smaller; margin-left: 8px; width: 200px;">
                            <div style="background: orange; color: white; border-radius: 7px 7px 7px 7px">
                                <p style="text-align: center;font-size:
                                medium;margin-top: auto;">
                                    <b>${complaint.problemJob.title}</b></p>
                            </div>
                            <div style="margin-left: 10px; margin-right: 10px">
                                <b>User type: </b>${complaint.user.userType} <br>
                                <b>Publish date: </b>${complaint.date} <br>
                                <b>User email: </b>${complaint.user.email} <br>
                            </div>
                            <form action="${root}/jobs/options" method="get" style="margin-top: 10px; float: bottom">
                                <div align="center">
                                    <input type="hidden" name="jobId" value="${complaint.problemJob.id}"/>
                                    <button class="button_example" type="submit">See details</button>
                                </div>
                            </form>
                        </div>
                    </c:forEach>
                </div>
            </div>

        <%--include footer--%>
        <%@ include file="/view/public/common/footer.jsp" %>
    </div>
</body>
</html>
