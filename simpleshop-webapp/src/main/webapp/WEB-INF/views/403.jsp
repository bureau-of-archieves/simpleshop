
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:loginPage title="Permission denied" headerText="Authentication failed">
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <h3>You are not authorized to access this page.</h3>
        </div>
    </div>

    <hr>
    <c:url var="loginUrl"  value="/login.do"  />
    <a href="${loginUrl}">Login again</a>
</t:loginPage>
