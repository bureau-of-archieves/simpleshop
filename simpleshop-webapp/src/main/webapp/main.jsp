<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@ taglib prefix="ctrl" tagdir="/WEB-INF/tags/controls"  %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="f" uri="sponge/functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html data-ng-app="spongeApp" data-ng-controller="spongeController">
<head>
    <meta charset="UTF-8">
    <link rel="shortcut icon" type="image/png"  href="img/favicon.png" >
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <sec:csrfMetaTags/>

    <title><spring:message code="jsp.literal.appName" /></title>

    <%--stylesheets--%>
    <link rel="stylesheet" href="components/jquery-ui/themes/smoothness/jquery-ui.min.css"><!--required by the upload js-->
    <link rel="stylesheet" href="components/jquery-ui/themes/smoothness/theme.css">
    <link rel="stylesheet" href="components/bootstrap/dist/css/bootstrap.css">
    <link rel="stylesheet" href="components/bootstrap/dist/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="components/eonasdan-bootstrap-datetimepicker/build/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" href="components/toastr/toastr.min.css" >
    <link rel="stylesheet" href="css/site.css">
</head>

<body >

<%--top menu--%>
<nav id="topNav" class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <button class="navbar-toggle collapsed" type="button" data-toggle="collapse"
                    data-target="#navbar-collapse">
                <span class="sr-only"><spring:message code="jsp.literal.toggleNavigation" /></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">Top</a>
        </div>

        <div class="collapse navbar-collapse" id="navbar-collapse">
            <ul class="nav navbar-nav">

                <li ><a href="#" data-toggle="collapse" data-target="#navbar-collapse" data-spg-list='{"modelName":"Category", "variant":"main"}'>Categories</a></li>

                <comm:hasRole role="ADMIN" >
                    <li class="dropdown">
                        <a href="#" data-toggle="dropdown" class="dropdown-toggle">Search <span class="caret"></span>
                        </a>

                        <ul role="menu" class="dropdown-menu">
                            <li data-ng-repeat="item in menu">
                                <a href="#" class="icon" data-toggle="collapse" data-target="#navbar-collapse"
                                   data-spg-search="{{item.name}}">
                                    <ctrl:icon value="{{item.icon}}"/>&nbsp; {{item.name}} </a>
                            </li>
                        </ul>
                    </li>
                </comm:hasRole>

                <li class="dropdown result ng-cloak" data-ng-show="getViewIds().length">
                    <a href="#" data-toggle="dropdown" class="dropdown-toggle">Views <span class="caret"></span>
                    </a>
                    <ul role="menu" class="dropdown-menu">

                        <li>
                            <a href="javascript:void(0);" data-ng-click="closeOthers('')">
                                <div class="row">
                                    <table>
                                        <tr><td><span data-toggle="collapse" data-target="#navbar-collapse" >Close All</span></td></tr>
                                    </table>
                                </div>

                            </a>
                        </li>

                        <li class="ng-scope" data-ng-repeat="resultName in getViewIds()">
                            <a class="ng-binding result-menu-item" data-toggle="collapse" data-target="#navbar-collapse" href="javascript:void(0);">

                                <div class="row">

                                    <table>
                                        <tr>
                                            <td data-toggle="collapse" data-target="#navbar-collapse"  >
                                                <span data-ng-click="scrollTo(resultName)" style="display:inline-block; margin-right:4.5em;">{{resultName | pascal}}</span>

                                                <div class="btn-group btn-group-xs" style="position:absolute; right:0.5em" role="menuitem" aria-label="Menu item options">
                                                    <div class="btn btn-default" role="button" aria-label="Close Others" data-ng-click="closeOthers(resultName)" >
                                                        <span class="glyphicon glyphicon-remove-sign" title="Close Others"></span>
                                                    </div>
                                                    <div class="btn btn-default" role="button" aria-label="Close This" data-ng-click="closeResult(resultName);$event.stopPropagation();" >
                                                        <span class="glyphicon glyphicon-remove-circle" title="Close This"></span>
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                    </table>


                                </div>
                            </a>
                        </li>

                    </ul>
                </li>

                <li class="dropdown">
                    <a href="#" data-toggle="dropdown" class="dropdown-toggle">
                        Help <span class="caret"></span>
                    </a>
                    <ul role="menu" class="dropdown-menu">
                        <li><a href="javascript:alert('Not implemented yet.')">Options</a>
                        </li>
                        <li><a href="javascript:showMessage('Modal message box test.')">About</a>
                        </li>
                        <li><a href="json.jsp">Test</a>
                        </li>
                    </ul>
                </li>

            </ul>

            <ul class="nav navbar-nav navbar-right">
                <li><a href="#"><span style="position:relative;top:1px;left:-2px"><spring:message code="jsp.literal.shoppingCart" /></span> <span class="badge ng-cloak" >{{cart.items.length |number:0}}</span></a></li>

                <comm:hasUser>
                    <jsp:attribute name="isFalse">
                        <li><a href="login.do">Login</a></li>
                    </jsp:attribute>
                    <jsp:body>
                        <li><a href="logout.do">Logout</a></li>
                        <li><a href="#">${username}</a></li>
                    </jsp:body>
                </comm:hasUser>

            </ul>
        </div>
    </div>
</nav>

<div class="container-fluid page-body">

    <t:header/>

    <main class="row">

        <section class="col-xs-12">

            <section style="min-height: 40em" id="resultSection" class="hide-children row">
                <%--views--%>
                <div id="messageNoView" class="alert alert-info display no-display-predecessor" role="alert">
                    There is no open view.
                </div>
            </section>

        </section>

    </main>
    <%--page footer--%>
    <t:footer />

</div>

<%--link requests--%>
<ul id="notification_list" class="nav nav-pills nav-stacked">
    <li data-ng-show="operationLocks.length" style="background-color: transparent">
        <img class="pull-right" src="img/progress.gif" alt="progress indicator">
    </li>
</ul>

<script type="text/ng-template" id="messageBox.html">
    <div class="modal-header">
        <h3 class="modal-title">{{title}}</h3>
    </div>
    <div class="modal-body">
        <p>{{message}}</p>
    </div>
    <div class="modal-footer">
        <button class="btn btn-primary" type="button" data-ng-click="ok()">OK</button>
    </div>
</script>


<%--scripts--%>
<script src="components/jquery/dist/jquery.min.js"></script>
<script src="components/jquery-ui/jquery-ui.min.js"></script>
<script src="js/jquery.iframe-transport.js"></script>
<script src="js/jquery.fileupload.js"></script>
<script src="components/moment/min/moment.min.js"></script>
<script src="components/bootstrap/dist/js/bootstrap.min.js"></script>
<script src="components/angular/angular.js"></script>
<script src="js/i18n/angular-locale_${f:userLocale()}.js"></script>
<%--<script src="js/angular-animate.js"></script>--%>
<script src="components/angular-bootstrap/ui-bootstrap-tpls.min.js"></script>
<script src="components/eonasdan-bootstrap-datetimepicker/build/js/bootstrap-datetimepicker.min.js"></script>
<script src="components/toastr/toastr.min.js" ></script>
<script src="js/zcl.js"></script>
<script src="js/sponge.js"></script>

<%--page initialization script--%>
<script>
     zcl.removeAnchor();
</script>

</body>
</html>

