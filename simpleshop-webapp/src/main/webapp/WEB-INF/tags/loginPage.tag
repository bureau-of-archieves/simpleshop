<%@ tag language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<%@attribute name="title"%>
<%@attribute name="headerText"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/components/bootstrap/dist/css/bootstrap.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/components/bootstrap/dist/css/bootstrap-theme.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">

    <title>${title}</title>
</head>
<body>
<div class="container">

    <div class="page-header">
        <h2>Simpleshop&nbsp;
            <small>${headerText}</small>
        </h2>
    </div>

    <section class="main">
        <jsp:doBody/>
    </section>

    <footer>
        <hr>
        <div class="row">
            <div class="col-md-8 col-md-offset-2">
                This is footer.
            </div>
        </div>
    </footer>
</div>
</body>
</html>


