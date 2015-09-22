<%@tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>

<comm:peek var="replace_id_marker" />
<c:if test="${not empty replace_id_marker}" >
    <replace-id-marker>${replace_id_marker}</replace-id-marker>
</c:if>