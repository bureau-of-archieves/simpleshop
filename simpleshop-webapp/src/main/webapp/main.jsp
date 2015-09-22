<%@include file="WEB-INF/_header.jspf" %>
<!DOCTYPE html>
<html data-ng-app="spongeApp" data-ng-controller="spongeController">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="shortcut icon" type="image/png"  href="img/site.png" >
    <title>SimpleShop Demo Web App</title>

    <%--stylesheets--%>
    <link rel="stylesheet" href="css/jquery-ui.css">
    <link rel="stylesheet" href="css/jquery-ui.structure.css">
    <link rel="stylesheet" href="css/jquery-ui.theme.css">

    <link rel="stylesheet" href="css/bootstrap.css">
    <link rel="stylesheet" href="css/bootstrap-theme.css">
    <link rel="stylesheet" href="css/bootstrap-datetimepicker.css">
    <link rel="stylesheet" href="css/site-layout.css">
    <link rel="stylesheet" href="css/site-theme.css">
</head>

<body>

<%--top menu--%>
<nav id="topNav" class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <button class="navbar-toggle collapsed" type="button" data-toggle="collapse"
                    data-target="#navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">Top</a>
        </div>

        <div class="collapse navbar-collapse" id="navbar-collapse">
            <ul class="nav navbar-nav">

                <li ><a href="#" data-spg-list='{"modelName":"Category", "variant":"main"}'>Categories</a></li>

                <li class="dropdown">
                    <a href="#" data-toggle="dropdown" class="dropdown-toggle">Search <span class="caret"></span>
                    </a>

                    <ul role="menu" class="dropdown-menu">
                        <li data-ng-repeat="item in menu">
                            <a href="#" class="icon"
                               data-spg-search="{{item.name}}">
                                <ctrl:icon value="{{item.icon}}"/>&nbsp; {{item.name}} </a>
                        </li>
                    </ul>
                </li>

                <li class="dropdown result" data-ng-show="getViewIds().length">
                    <a href="#" data-toggle="dropdown" class="dropdown-toggle">Views <span class="caret"></span>
                    </a>
                    <ul role="menu" class="dropdown-menu">

                        <li>
                            <a href="javascript:void(0);">
                                <div class="row">
                                    <table>
                                        <tr><td><span data-ng-click="closeOthers('')">Close All</span></td></tr>
                                    </table>
                                </div>

                            </a>
                        </li>

                        <li class="ng-scope" data-ng-repeat="resultName in getViewIds()">
                            <a class="ng-binding result-menu-item" href="javascript:void(0);">

                                <div class="row">

                                    <table>
                                        <tr>
                                            <td>
                                                <span data-ng-click="scrollTo(resultName)" style="display:inline-block; margin-right:4.5em;">{{resultName | pascal}}</span>

                                                <div class="btn-group btn-group-xs" style="position:absolute; right:0.5em"   role="menuitem" aria-label="Menu item options">
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
                        <li><a href="javascript:showMessage('Modal message box test.')">About</a>
                        </li>
                        <li><a href="json.jsp">Test</a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="container-fluid page-body">

    <%--page header--%>
    <header id="pageHeader" class="page-header row">
        <h1>
            <a href=" "> <%--refresh main page--%>
                <small>SimpleShop SPA Web App</small>
            </a>
        </h1>
    </header>

    <main class="row">

        <section class="col-xs-12">

            <section style="min-height: 65em" id="resultSection" class="hide-children row">
                <%--views--%>
                <div id="messageNoView" class="alert alert-info display no-display-predecessor" role="alert">
                    There is no open view.
                </div>
            </section>

        </section>

    </main>
    <%--page footer--%>
    <footer class="row">
        <hr>
        <div class="col-xs-8 col-xs-offset-2">
            <small class="text-info">Experimental SPA Web app demo based on Northwind database. Powered by
                AngularJS, Spring Hibernate and Drools; created by John Zhang, 2015.
            </small>
        </div>
    </footer>

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
<script src="js/jquery.js"></script>
<script src="js/jquery-ui.js"></script>
<script src="js/jquery.iframe-transport.js"></script>
<script src="js/jquery.fileupload.js"></script>
<script src="js/moment.js"></script>
<script src="js/bootstrap.js"></script>
<script src="js/angular.js"></script>
<script src="js/angular-animate.js"></script>
<script src="js/ui-bootstrap-tpls.js"></script>
<script src="js/bootstrap-datetimepicker.js"></script>
<script src="js/zcl.js"></script>
<script src="js/sponge.js"></script>

<%--page initialization script--%>
<script>
     zcl.removeAnchor();
</script>

</body>
</html>

