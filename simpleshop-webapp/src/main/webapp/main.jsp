<%@include file="WEB-INF/_header.jspf"%>
<!DOCTYPE html>
<html data-ng-app="spongeApp" data-ng-controller="spongeController">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>SimpleShop Demo Web App</title>

    <%--stylesheets--%>
    <link rel="icon" href="img/site.ico" type="image/x-icon"/>
    <link rel="stylesheet" href="css/jquery-ui.css">
    <link rel="stylesheet" href="css/bootstrap.css">
    <link rel="stylesheet" href="css/bootstrap-theme.css">
    <link rel="stylesheet" href="css/site-layout.css"/>
    <link rel="stylesheet" href="css/site-theme.css"/>
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

                <li class="dropdown">
                    <a href="#" data-toggle="dropdown" class="dropdown-toggle">Search <span class="caret"></span>
                    </a>

                    <ul role="menu" class="dropdown-menu">
                        <li data-ng-repeat="item in menu">
                            <a href="#" class="icon"
                               data-spg-search="{{item.name}}" >
                                <ctrl:icon value="{{item.icon}}" />&nbsp; {{item.name}} </a>
                        </li>
                    </ul>
                </li>

                <li class="dropdown result" data-ng-show="getViewIds().length">
                    <a href="#" data-toggle="dropdown" class="dropdown-toggle">Results <span class="caret"></span>
                    </a>
                    <ul role="menu" class="dropdown-menu">

                        <li>
                            <a href="javascript:void(0);">
                                <span data-ng-click="closeOthers('')">Close All</span>
                            </a>
                        </li>

                        <li class="ng-scope" data-ng-repeat="resultName in getViewIds()">
                            <a class="ng-binding result-menu-item" href="javascript:void(0);">
                                <span data-ng-click="scrollTo(resultName)">{{resultName | pascal}}</span>
                                    <span class="glyphicon glyphicon-remove-circle"
                                          data-ng-click="closeResult(resultName);$event.stopPropagation();"
                                          title="Close"></span>
                                    <span class="glyphicon glyphicon-asterisk" data-ng-click="closeOthers(resultName)"
                                          title="Close Others"></span>
                            </a>
                        </li>

                    </ul>
                </li>

                <li class="dropdown">
                    <a href="#" data-toggle="dropdown" class="dropdown-toggle">
                        Help <span class="caret"></span>
                    </a>
                    <ul role="menu" class="dropdown-menu">
                        <li><a>About</a>
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
    <header id="pageHeader" class="page-header">
        <h1>
            <a href=" "> <%--refresh main page--%>
                <small>SimpleShop SPA Web App</small>
            </a>
        </h1>
    </header>

    <main class="row">

        <section class="col-xs-12">
            <div class="row">
                <section style="min-height: 60em" id="resultSection" class="col-xs-12 hide-children">
                    <%--views--%>
                    <div id="messageNoView" class="alert alert-info display no-display-predecessor" role="alert">
                        There is no open view.
                    </div>
                </section>
            </div>
        </section>

    </main>

    <%--page footer--%>
    <footer>
        <hr>
        <div class="col-xs-8 col-xs-offset-2">
            <small class="text-info">Experimental SPA Web app demo based on Northwind database. Powered by
                AngularJS, Spring Hibernate and Drools; created by John Zhang, 2015.
            </small>
        </div>
    </footer>

</div>

<%--scripts--%>
<script src="js/jquery.js"></script>
<script src="js/bootstrap.js"></script>
<script src="js/angular.js"></script>
<script src="js/jquery-ui.js"></script>
<script src="js/zcl.js"></script>
<script src="js/sponge.js"></script>

<%--page initialization script--%>
<script>


</script>

</body>
</html>

