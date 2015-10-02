<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<t:loginPage title="Please login" headerText="Login page">

    <div class="row">
        <div class="col-md-6 col-md-offset-3">

            <h3>Welcome!</h3>

            <c:if test="${not empty error}">
                <div class="alert alert-warning">
                    Your login was unsuccessful. <br>
                    Caused: ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
                </div>
            </c:if>

            <form action="j_spring_security_check.do" name="f" method="post" class="form-horizontal">
                <div class="form-group">
                    <label for="txtUsername" class="col-md-2 control-label">Username:</label>

                    <div class="col-md-10">
                        <input id="txtUsername" name="j_username" autofocus="autofocus" class="form-control">
                    </div>
                </div>

                <div class="form-group">
                    <label for="txtPassword" class="col-md-2 control-label">Password:</label>

                    <div class="col-md-10">
                        <input id="txtPassword" name="j_password" type="password" class="form-control">
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-md-offset-2 col-md-10">
                        <input type="submit" value="Login" class="btn btn-default">
                        <input type="reset" value="Reset" class="btn btn-default">
                    </div>
                </div>

                <sec:csrfInput/>
            </form>


        </div>
    </div>

</t:loginPage>
