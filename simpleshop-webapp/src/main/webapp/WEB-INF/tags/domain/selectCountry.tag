<%@tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="ctrl" tagdir="/WEB-INF/tags/controls" %>

<%@attribute name="path" required="true" %>

<ctrl:editNgSelect
        path="${path}"
        optionsUrl="/json/countries"
        optionsExpression="country.name for country in items track by country.name"  />