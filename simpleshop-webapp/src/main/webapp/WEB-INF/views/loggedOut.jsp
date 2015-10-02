
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:loginPage title="You have successfully Logged out" headerText="You have logged out">
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <div class="alert alert-info">
                <h3>You have successfully Logged out</h3>
            </div>

            <hr>
            <c:url var="loginUrl"  value="/login.do"  />
            <a href="${loginUrl}">Login again</a>
        </div>
    </div>

</t:loginPage>
