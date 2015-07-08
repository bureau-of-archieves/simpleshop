<%@include file="../_header.tag"%>

<%@attribute name="title"%>
<%@attribute name="cssClass"%>

<div class="starwars viewport ${cssClass}">
    <div class="scrolling">
        <div>
            <h3>${title}</h3>
            <jsp:doBody/>
        </div>
    </div>
</div>