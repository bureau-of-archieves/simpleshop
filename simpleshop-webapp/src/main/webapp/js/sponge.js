/*
 * This file contains script that backs the UI layer.
 * Above is an example of unnecessary comment.
 */
(function () {

    var message = {"requestFailed": "Failed to send request to server."};

    /**
     * All references to UI layer element id or url are defined in this object.
     */
    var site = new (function () {
        var jsonPath = "json/"; //the root path for json related operations, e.g. save object, refresh object or search.
        var viewPath = "ng/"; //the root path of all views

        /**
         * Construct the url to get a single model in json format.
         * @param modelName will be converted to url model name, e.g. 'customer_group'.
         * @param modelId Pass false in modelId to get the json of a new model, which contains all defaults.
         * @returns {string}
         */
        this.modelJsonUrl = function (modelName, modelId) {
            var url = jsonPath + zcl.pascalNameToUrlName(modelName) + "/";
            url += modelId || "new";
            return url;
        };

        /**
         * Construct the search url.for a model type.
         * When accessed with 'GET', this url returns the search parameters object with all default values.
         * When accessed with a 'POST', you post the populated search parameters object and get a list of models of the specified type.
         * @param modelName model type in url naming.
         * @returns {string}
         */
        this.searchJsonUrl = function (modelName) {
            return jsonPath + zcl.pascalNameToUrlName(modelName) + "/search";
        };

        /**
         * Construct a url for a model name what you can use to save a model with a 'POST'.
         * @param modelName
         * @returns {string}
         */
        this.saveJsonUrl = function (modelName) {
            return jsonPath + zcl.pascalNameToUrlName(modelName) + "/save";
        };

        /**
         * Construct a url for a model name what you can use to delete a model with a 'POST'.
         * @param modelName
         * @returns {string}
         */
        this.deleteJsonUrl = function (modelName) {
            return jsonPath + zcl.pascalNameToUrlName(modelName) + "/delete";
        };

        this.newJsonUrl = function (modelName) {
            return jsonPath + zcl.pascalNameToUrlName(modelName) + "/new";
        };

        /**
         * If a model supports quick search (or keyword search, such as in a autocomplete), the controller will support the list operation.
         * @param modelName specify what type of model we want to retrieve.
         * @returns {string} the url to post to.
         */
        this.listJsonUrl = function (modelName) {
            return jsonPath + zcl.pascalNameToUrlName(modelName) + "/list";
        };

        this.dropdownJsonUrl = function (modelName) {
            return jsonPath + zcl.pascalNameToUrlName(modelName) + "/dropdown";
        };

        /**
         * Construct the url to access a view.
         * @param viewName full view name as in the view jsp file.
         * @param params query string parameters in an object.
         */
        this.viewUrl = function (viewName, params) {
            var viewUrl = viewPath + viewName + ".jsp";
            if (params) {
                viewUrl += "?" + $.param(params);
            }
            return viewUrl;
        };

        /**
         * Get the url of the metadata service.
         * @returns {string} the url to get all model metadata.
         */
        this.metadataUrl = function () {
            return jsonPath + "metadata";
        };

        //page element dependencies
        this.noViewElementId = "messageNoView"; //id of the message element which is shown where there is no view in the view section. The main section is divided into search view section and view section.
        this.headerElementId = "pageHeader"; //header element id, the header is fix positioned.

        //angular js dependencies
        this.ajs = {};

        var container = $("#" + this.noViewElementId).parent();

        var countColumns = function(views){
            var count = 0;
            if(views.length > 0){

                var view = $(views.get(0));
                var lastLeft = view.position().left;
                count = 1;
                for(var i=1; i<views.length; i++){
                    var newLeft = $(views.get(i)).position().left;
                    if(newLeft > lastLeft){
                        lastLeft = newLeft;
                        count++;
                    } else {
                        break;
                    }
                }
            }

            return count;
        };

        var findColumn = function(columnPositions, left){

            for(var i=0; i<columnPositions.length; i++){
                if(columnPositions[i].left >= left){
                    return i;
                }
            }
            return -1;
        };

        this.layout = function(){
            var views = container.children(".view.display");
            if(views.length == 0) {
                return;
            }

            //redo layout
            var columnPositions = [];
            var currentColumnCount = countColumns(views);
            if(currentColumnCount == 1){
                columnPositions.push({});
                views.css("margin-top", 0);
                return;
            }
            var maxLeft = -1;
            for(var i=0; i<views.length; i++){
                var view = $(views.get(i));
                var viewPos = view.position();
                if(viewPos.left > maxLeft) {
                    var newColumnPos = {};
                    newColumnPos.left = viewPos.left;
                    newColumnPos.top = viewPos.top;
                    newColumnPos.bottom = viewPos.top + view.outerHeight();
                    columnPositions.push(newColumnPos);
                    maxLeft = viewPos.left;
                    view.css("margin-top", 0);
                } else {
                    var columnIndex = findColumn(columnPositions, viewPos.left);
                    var marginTop = columnPositions[columnIndex].bottom - viewPos.top;
                    columnPositions[columnIndex].bottom += view.outerHeight();
                    view.css("margin-top", marginTop);
                }
            }
        };

        $( window ).resize(this.layout);
    })();

    /**
     * A map from view key to viewDetails.
     * A view key is a unique code of a set of content. It concisely indicate what content is being displayed in the view.The same content can only be displayed in one view at a time.
     *A viewDetails object is compound object which contains all backing data of a view.
     * @type {{}} the global view details map.
     */
    var viewMap = {};

    //region util functions - these are specific to the sponge ui layer. Generic ones should be added to zcl.js.

    var headerHeight = $("#" + site.headerElementId).height(); //header is fix positioned

    /**
     * Scroll to an element.
     * @param id
     */
    var scrollTo = function (id) {
        if (!id) { //scroll to top
            window.location.href = "#";
        }

        var elem = document.getElementById(id);
        if (!elem)
            return;

        window.location.href = "#" + id;
        window.scrollBy(0, -headerHeight); //header is fixed positioned
    };

    /**
     * Set if an element is visible.
     * There are a few different different techniques used to set element visibility in sponge, this method consider them all.
     * @param id
     * @param show true to show.
     * @returns {boolean} true if the operation is successful.
     */
    var display = function (id, show) {
        var elem = document.getElementById(id);
        if (!elem)
            return false;

        elem = $(elem);
        if (!elem.parent().hasClass("hide-children")) {
            if (show)
                elem.show();
            else
                elem.hide();
        } else {
            var classes = elem.attr("class");
            if (show) {
                if (classes.indexOf("display") < 0) {
                    elem.addClass("display");
                }
            } else {
                if (classes.indexOf("display") >= 0) {
                    elem.removeClass("display");
                }
            }
        }
        site.layout();
        return true;
    };

    /**
     * A utility function to simplify functional programming.
     * @param rejectReason pass null to indicate success.
     * @returns {*} returns a promise that has already been resolved or rejected.
     */
    var createPromise = function (rejectReason) {
        var deferred = $.Deferred();
        if (rejectReason)
            deferred.reject(rejectReason);
        else
            deferred.resolve();
        return deferred.promise();
    };

    var bodyScope = null;

    /**
     * Get the scope at the view section level, which is the parent scope of all view scopes.
     * @returns {*|jQuery}
     */
    var getBodyScope = function () {
        if(bodyScope == null){
            //var $body = angular.element(document.body);
            //bodyScope = $body.injector().get('$rootScope');
            bodyScope = $("#" + site.noViewElementId).scope();
        }
        return bodyScope;
    };

    /**
     * Apply a function, if already in digest cycle just call it (it is already being applied).
     * todo should eventually eliminate all calls to this function.
     * @param func
     */
    var safeApply = function (func) {
        var scope = getBodyScope();
        if (scope.$root.$$phase != '$apply' && scope.$root.$$phase != '$digest') {
            scope.$apply(func);
        } else {
            func();
        }
    };

    /**
     * Report an error.This is usually called on the endOp promise.
     * @param error error object.
     */
    var reportError = function (error) {
        if (!error)
            return;

        if (typeof(error) == "object") {
            error = message["requestFailed"];
        }

        var instance = site.ajs.$modal.open({
            backdrop: true,
            templateUrl: "messageBox.html",
            size: "md",
            windowClass: "msgbox",
            controller: 'messageBoxController'
        });

        instance.data = {title: "An error has occurred", message: error};

    };

    window.showMessage = reportError;

    /**
     * Check if a viewType is a subtype of parentViewType.
     * Example: sub-list type -> list_detailed
     * @param viewType string.
     * @param parentViewType string.
     * @returns {boolean}
     */
    var isSubtypeOf = function (viewType, parentViewType) {
        if (viewType == parentViewType)
            return true;

        return viewType.indexOf(parentViewType + "_") == 0;
    };

    /**
     * Find the view details of a view.
     * @param viewId
     * @returns {*} returns null if viewId does not exist or is already disposed.
     */
    var findViewDetails = function (viewId) {
        var key = findViewKey(viewId);
        if (key) {
            return viewMap[key];
        }
        return null;
    };

    /**
     * Find the view key by viewId. Logically this finds the content being displayed by the view.
     * @param viewId
     * @returns {*}
     */
    var findViewKey = function (viewId) {
        var ownProperties = zcl.getOwnProperties(viewMap);
        for (var i = 0; i < ownProperties.length; i++) {
            var key = ownProperties[i];
            var viewDetails = viewMap[key];
            if (viewDetails && viewDetails.viewId == viewId) {
                return key;
            }
        }
        return null;
    };

    /**
     * The element to go to when a view is closed.
     * @param targetElement the element to be closed.
     */
    var goBackElementId = function (targetElement) {
        var gotoId = "";
        var nextElement = targetElement.next(".display");
        if (nextElement && nextElement.length == 1 && nextElement.attr("id") != site.noViewElementId) {
            gotoId = nextElement.attr("id");
        }
        if (!gotoId) {
            var prevElement = targetElement.prev(".display");
            if (prevElement && prevElement.length == 1) {
                gotoId = prevElement.attr("id");
            }
        }
        return gotoId;
    };


    /**
     * Defines the relationship between viewName and modelName and viewType.
     * @param modelName the model name.
     * @param viewType the full view type.
     * @returns {string} the view name.
     */
    var getViewName = function (modelName, viewType) {
        return zcl.pascalNameToUrlName(modelName) + "-" + viewType;
    };

    /**
     * Resolve the real model id from the input.
     * @param modelId could be an id selector of the input field which contains the model id.
     * @returns {*} model id resolved.
     */
    var resolveModelId = function (modelId) {
        if (!modelId)
            return modelId;

        if (typeof modelId == "string") {
            if (modelId.substring(0, 1) == "#") {
                var input = $(modelId);
                modelId = input.val();
                modelId = parseInt(modelId);
                if (isNaN(modelId)) {
                    modelId = zcl.subStrBeforeFirst(input.val(), '-', true);
                }
            }
        }

        return modelId;
    };

    /**
     * Update new model cache.
     * @param bodyScope body scope.
     * @param modelName model name.
     * @param model pass null if want to retrieve from server.
     */
    var ensureNewModel = function (bodyScope, modelName, model) {
        if (!bodyScope.newModel) {
            bodyScope.newModel = {};
        }

        if (!angular.isUndefined(model) && model != null) {
            bodyScope.newModel[modelName] = model;
            return createPromise(null);
        }

        model = bodyScope.newModel[modelName];
        if (!angular.isUndefined(model) && model != null) {
            return createPromise(null);
        }

        //get from server
        var url = site.newJsonUrl(modelName);
        return $.ajax(url, {
            type: "GET",
            contentType: "application/json",
            dataType: "json"
        }).done(function (response) {
            if (response.status == "OK") {
                bodyScope.newModel[modelName] = response.content;
            } else {
                bodyScope.newModel[modelName] = {};
            }
        });
    };

    /**
     * Update search model cache.
     * @param bodyScope body scope.
     * @param modelName model name.
     * @param model pass null if want to retrieve from server.
     */
    var ensureSearchModel = function (bodyScope, modelName, model) {
        if (!bodyScope.searchModel) {
            bodyScope.searchModel = {};
        }

        if (!angular.isUndefined(model) && model != null) {
            bodyScope.searchModel[modelName] = model;
            return createPromise(null);
        }

        model = bodyScope.searchModel[modelName];
        if (!angular.isUndefined(model) && model != null) {
            return createPromise(null);
        }

        //get from server
        var url = site.searchJsonUrl(modelName);
        return $.ajax(url, {
            type: "GET",
            contentType: "application/json",
            dataType: "json"
        }).done(function (response) {
            if (response.status == "OK") {
                bodyScope.searchModel[modelName] = response.content;
            } else {
                bodyScope.searchModel[modelName] = {};
            }
        });
    };

    //endregion

    var spongeApp = angular.module("spongeApp", ['ui.bootstrap'], null);

    //region filters

    spongeApp.filter("placeholder", function () {
        return function (input) {
            return input;
        };
    });

    spongeApp.filter("formatSortInfo", function () {
        return function (item) {
            return zcl.camelNameToSpacedName(item["property"]) + (item["ascending"] ? " Asc" : " Desc" )
        };
    });

    spongeApp.filter("status", function () {
        return function (input) {
            return input == 'A' ? "Active" : "Inactive";
        };
    });

    spongeApp.filter('yn', function () {
        return function (input) {
            return input && input !== 'N' ? '\u2713' : '\u2718';
        };
    });

    spongeApp.filter('phone', function () {
        return function (input) {
            return input;
        };
    });

    //{{item.prefix|indent:'_':-1}}
    spongeApp.filter("indent", [function(){

        return function (val, separator,delta) {

            var count = val.split(separator).length - 1;
            count += delta;
            if(count < 0)
                count = 0;

            return zcl.repeat("M", count);
        };
    }]);

    spongeApp.filter('url', function () {
        return function (input) {
            if (input) {
                var index = input.toString().indexOf("#");
                if (index >= 0) {
                    return input.substring(index + 1, input.length - 1);
                }
            }
            return input;
        };
    });

    spongeApp.filter('url_label', function () {
        return function (input) {
            if (input) {
                var index = input.toString().indexOf("#");
                if (index > 0) {
                    return input.substring(0, index);
                }
            }
            return input;
        };
    });

    spongeApp.filter('yesno', function () {
        return function (input) {
            return input && input !== 'N'  ? 'Yes' : 'No';
        };
    });

    spongeApp.filter('na', function () {
        return function (input) {
            return input != null ? input : 'N/A';
        };
    });

    spongeApp.filter('percent', function () {
        return function (input) {
            return input != null ? input + "%" : 'N/A';
        };
    });

    spongeApp.filter('prefix', function () {
        return function (input, pref) {
            if (input) {
                return pref + input;
            }
            return "";
        };
    });

    spongeApp.filter('suffix', function () {
        return function (input, suff) {
            if (input) {
                return input + suff;
            }
            return "";
        };
    });

    /**
     * Transform text into pascal casing.
     * E.g. test-details_view -> Test Details View
     */
    spongeApp.filter('pascal', function () {
        return function (input) {
            var values = input.replace(/[-_]/g, ' ').split(' ');
            for (var i = 0; i < values.length; i++) {
                values[i] = zcl.firstCharUpper(values[i]);
            }
            return values.join(' ');
        };
    });

    /**
     * Concatenate the property values of an object.
     * e.g. {{customer | concat:"firstName":" ":"lastName"}} outputs:
     * John Zhang
     */
    spongeApp.filter('concat', function () {
        return function () {
            var input = arguments[0];
            if(angular.isUndefined(input) || input === null)
                return null;

            var result = "";
            if(angular.equals({}, input))
                return result;
            var prevIsProp = false;
            for (var i = 1; i < arguments.length; i++) {
                var prop = arguments[i];
                var val = zcl.getProp(input, prop);

                if (!angular.isUndefined(val)) {
                    if (prevIsProp)
                        result += " ";
                    result += val;
                    prevIsProp = true;
                } else {
                    result += prop;
                    prevIsProp = false;
                }
            }
            return result;
        };
    });

    /**
     * Transform input object into a formatted string.
     */
    spongeApp.filter('interpolate', ['$interpolate', function ($interpolate) {
        return function (input, modelName) {

            if(angular.isUndefined(input) || input === null){
                return null;
            }

            if (!modelName)
                return input;

            var bodyScope = getBodyScope();
            var format = zcl.getProp(bodyScope, "metadata[" + modelName + "].interpolateFormat");
            if (!format)
                return input;

            var exp = $interpolate(format);
            return exp(input);
        };
    }]);

    /**
     * Format a date time value with moment.js.
     */
    spongeApp.filter('moment', function () {

        return function (input, format) {

            if (angular.isUndefined(input) || input == null)
                return input;

            if (!format)
                format = "LLL";

            return moment(input, 'x').format(format);
        };
    });

    //endregion

    //region service

    spongeApp.factory("spongeService", ["$compile", function ($compile) {

        var loadedLists = {};
        /**
         * Load a list of objects and send to the callback function.
         * @param url where we retrieve the objects in json form.
         * @param refresh re-retrieve the list if already cached.
         * @param callback function (loadedList)
         */
        var loadList = function(url, refresh, callback){

            if(!refresh && loadedLists.hasOwnProperty(url)){
                callback(loadedLists[url].content);
            }

            var success = function(data, textStatus, jqXHR){
                if(data["status"] == "OK"){
                    loadedLists[url] = data;
                    callback(loadedLists[url].content);
                } else {
                    reportError(data["description"]);
                }

            };

            var error = function( jqXHR,  textStatus,  errorThrown){
                reportError(textStatus + " - " + errorThrown);
            };

            $.ajax({
                dataType: "json",
                contentType: "application/json",
                url: url,
                success: success,
                error : error
            });
        };

        /**
         * All operations fired on the client side must execute beginOp and endOp in pair with the same token.
         * This will trigger a digest cycle at root level.
         * @param token used to identity an operation.
         */
        var beginOp = function (token) {
            var bodyScope = getBodyScope();
            if ($.inArray(token, bodyScope.operationLocks) >= 0)
                return createPromise("Operation is in progress, please wait.");

            safeApply(function () {
                bodyScope.operationLocks.push(token); //trigger ui change, e.g. spinning wheel.
            });
            return createPromise(null);
        };

        /**
         * End an operation by removing it from the operation lock list.
         * This will trigger a digest cycle at root level.
         * @param token the operation to end.
         */
        var endOp = function (token) {
            var bodyScope = getBodyScope();
            var index = $.inArray(token, bodyScope.operationLocks);
            if (index < 0)
                return createPromise("Operation '" + token + "' is not in progress.");

            safeApply(function () {
                bodyScope.operationLocks.splice(index, 1);
            });
            return createPromise(null);
        };

        var sequenceNumbers = {};

        /**
         * Generate the unique id for a view.
         * @param viewName
         * @param instanceId
         * @returns {string}
         */
        var generateViewId = function (viewName, instanceId) {
            if (instanceId)
                return viewName + "-" + instanceId;

            if (viewName.indexOf("-search") > 0)
                return viewName;

            //those are not identified by instanceId use an instance sequence number
            if (!sequenceNumbers[viewName]) {
                sequenceNumbers[viewName] = 0;
            }
            sequenceNumbers[viewName]++;

            return viewName + "-" + sequenceNumbers[viewName];
        };

        var defaultGetViewOption = {
            removeExisting: false, //if the same content (view key) is already being display, true -> go the view; false -> close the view and continue to open the new view.
            instanceIdInViewKey: true,//whether instance id is a part of view key - e.g. list views have client-side generated sequence number as instance id which does not describe the content of the view.
            viewTypeInViewKey: false,//whether or not to put view type in the view key - e.g. true for search view and different search views show different content.
            postDataInViewKey: true //whether the data posted to the server to retrieve this view identifies the content of the view.
        };

        /**
         * Create a view key. A view key is an identifier of the content displayed in a view.
         * @param modelName dependant component.
         * @param viewType dependant component.
         * @param instanceId dependant component.
         * @param postData dependant component.
         * @param getViewOptions get view options.
         */
        var createViewKey = function (modelName, viewType, instanceId, postData, getViewOptions) {
            //calculate viewKey; two views have the same key if they display the same content and therefore one has to close for the other to open.
            var viewKeyObject = [modelName];
            viewKeyObject.push(getViewOptions.viewTypeInViewKey ? viewType : null);
            viewKeyObject.push(getViewOptions.instanceIdInViewKey && instanceId ? instanceId : null);
            viewKeyObject.push(getViewOptions.postDataInViewKey && postData ? postData : null);
            return angular.toJson(viewKeyObject); //use this to remove $$hashKey.
        };

        /**
         * Parse the html returned by from the server.
         * The html string will be parsed into a set of dom elements and a data object.
         * @param viewHtml view html returned from the server.
         * @param viewId view id generated on the client side. View id is always created on the client side.
         * @returns {*} constructed dom elements and the data object to bind to.
         */
        var parseViewHtml = function (viewHtml, viewId) {
            if (!viewHtml) {
                viewHtml = "";
            }

            //handle view id.
            var markerIndex = viewHtml.lastIndexOf("<replace-id-marker>");
            if (markerIndex >= 0) {
                var marker = viewHtml.substr(markerIndex);
                viewHtml = viewHtml.substring(0, markerIndex);
                var contentEndIndex = marker.lastIndexOf("<");
                marker = marker.substring(19, contentEndIndex).trim();
                if (marker)
                    viewHtml = viewHtml.replace(new RegExp(marker, "g"), viewId);
            }

            var elements = $.parseHTML(viewHtml, null, true);

            //separate view and model
            var json = null;
            for (var i = elements.length - 1; i >= 0; i--) {
                if (elements[i] && elements[i].tagName == "SCRIPT") {
                    json = elements[i].innerHTML;
                    elements.splice(i, 1);//remove last script tag
                    break;
                }
            }

            json = $.trim(json);
            var newModel = json ? JSON.parse(json) : {};
            return {domElements: elements, dataObject: newModel};
        };

        /**
         * After the view is successfully created, initialise the view elements.
         * @param viewDetails whose view is to be initialised.
         */
        var initViewElements = function (viewDetails) {

            var elements = viewDetails.viewElements;

            //jquery initialization for the view
            $(elements).find("ul.nav.nav-tabs").makeTab();

        };

        /**
         * Get a view with its model (in JSON) embedded from the server.
         * @param modelName model name in pascal casing.
         * @param viewType type of the view; e.g. search, create, details, update, list.
         * @param instanceId an object that uniquely identifies view instance for the given viewName (which is modelName-viewType).
         * @param params query string parameters as properties of a plain object.
         * @param model the content to post to the server in order to generate the view and its data. If this is not null a post instead of a get will occur.
         * @param getViewOptions - removeExisting whether to remove existing view with the same id.
         * @returns {*} the promise of this operation.
         */
        var getView = function (modelName, viewType, instanceId, params, model, getViewOptions, eventScope) {

            getViewOptions = $.extend($.extend({}, defaultGetViewOption), getViewOptions);
            var viewName = getViewName(modelName, viewType);
            var viewKey = createViewKey(modelName, viewType, instanceId, model, getViewOptions);

            //check for existing view
            var viewId = null;
            var existingViewDetails = viewMap[viewKey];
            if (!getViewOptions.removeExisting && existingViewDetails) {

                viewId = existingViewDetails.viewId;
                display(viewId, true);
                scrollTo(viewId);
                return existingViewDetails.viewType == viewType ? createPromise(null) : createPromise(zcl.formatObject("The content of view '{0}' is already being displayed in another view.", [viewType]));
            }

            if (!viewId) {
                viewId = generateViewId(viewName, instanceId);
            }
            var viewUrl = site.viewUrl(viewName, params);
            var operationKey = "get-" + viewId;

            var createRequest = function () {
                if (model) { //need to post

                    var clone = angular.copy(model);
                    prePostHandler(clone, eventScope);

                    return $.ajax(
                        viewUrl,
                        {
                            type: "POST",
                            data: JSON.stringify(clone),
                            contentType: "application/json",
                            dataType: "html"
                        }
                    );
                } else {
                    return $.get(viewUrl);
                }
            };

            var createView = function (viewHtml) {

                var parseResult = parseViewHtml(viewHtml, viewId);

                var nextElement = existingViewDetails ? $(existingViewDetails.viewElements).last().next() : $("#" + site.noViewElementId);
                if (!nextElement) {
                    return createPromise("Could not determine the insert position for the view being created.");
                }

                if (existingViewDetails) { //remove old view
                    $(existingViewDetails.viewElements).remove();
                }

                var postData = angular.copy(model);

                viewMap[viewKey] = { //set newViewDetails
                    viewKey: viewKey,
                    viewId: viewId,
                    modelName: modelName,
                    viewType: viewType,
                    instanceId: instanceId,
                    params: params,
                    postData: postData,
                    model: parseResult.dataObject,
                    viewElements: parseResult.domElements,
                    getViewOptions: getViewOptions
                };

                try {
                    safeApply(function () {
                        try {
                            nextElement.before(parseResult.domElements);
                            $compile(parseResult.domElements)(getBodyScope());
                            site.layout();
                        }
                        catch (ex) {
                            delete viewMap[viewKey];
                            throw ex;
                        }
                    });
                } catch (ex) {
                    return createPromise("View compilation failed:" + ex);
                }

                initViewElements(viewMap[viewKey]);
                scrollTo(viewId);
                return createPromise(null);
            };

            return beginOp(operationKey)
                .fail(function () {
                    reportError("Cannot begin operation.");
                })
                .then(function () {
                    return createRequest().then(
                        createView,
                        function (jqXHR, textStatus, description) {
                            return createPromise("Failed to retrieve view " + viewName + ": " + (description ? description : textStatus));
                        }
                    ).always(
                        function () {
                            return endOp(operationKey);
                        }
                    );
                });
        };

        /**
         * Create a json post request.
         * @param url
         * @param data json object.
         * @returns {*}
         */
        var createJsonPostRequest = function (url, data) {

            return $.ajax(url,
                {
                    type: "POST",
                    data: JSON.stringify(data),
                    contentType: "application/json",
                    dataType: "json"
                });
        };

        /**
         * Save the backing model of a view.
         * @param viewId specify which view.
         * @returns {*}
         */
        var save = function (viewId) {

            var viewDetails = findViewDetails(viewId);
            var scope = viewDetails.scope;

            var saveSuccess = function (response) {
                if (response["status"] == "OK") {
                    return cancel(viewId, false, response["content"]);
                } else {
                    scope.$apply(function () {
                        scope["model"] = response["content"];
                        viewDetails.model = response["content"];
                    });
                    return createPromise(response["description"]);
                }
            };

            var saveFailed = function (p, s, e) {
                return createPromise(s + " - " + e);
            };

            var operationKey = "save-" + viewId;
            var url = site.saveJsonUrl(viewDetails.modelName);
            return beginOp(operationKey).then(
                function () {
                    return createJsonPostRequest(url, scope["model"])
                        .then(saveSuccess, saveFailed)
                        .always(function () {
                            endOp(operationKey);
                        });
                }
            );
        };

        var remove = function (modelName, modelId, viewId) {

            if (!viewId)
                return createPromise("View Id is not passed.");

            var deleteSuccess = function (response) {
                if (response["status"] == "OK") {
                    var viewDetails = findViewDetails(viewId);
                    var scope = viewDetails.scope;
                    var content = scope["model"];
                    if (angular.isArray(content)) {
                        var deleted = -1;
                        for (var i = 0; i < content.length; i++) {
                            if (content[i]["id"] == modelId) {
                                deleted = i;
                                break;
                            }
                        }
                        if (deleted >= 0) {
                            safeApply(function () {
                                content.splice(deleted, 1);
                            });
                        }
                        return createPromise(null);
                    } else {
                        return cancel(viewId, true);
                    }

                }
                return createPromise(response["description"]);

            };

            var operationKey = "delete-" + viewId;
            var url = site.deleteJsonUrl(modelName);
            return beginOp(operationKey).then(
                function () {
                    return createJsonPostRequest(url, modelId)
                        .then(deleteSuccess)
                        .always(function () {
                            endOp(operationKey);
                        });
                }
            );
        };

        /**
         * Close create view or
         * Close update view and open details view.
         * @param viewId
         * @param noOpenAnother true if should not open another view when the view is closed.
         * @param model the latest model object from the view being closed.
         * @returns {*}
         */
        var cancel = function (viewId, noOpenAnother, model) {

            var viewDetails = findViewDetails(viewId);
            var modelName = viewDetails.modelName;
            var viewType = viewDetails.viewType;
            if (noOpenAnother || isSubtypeOf(viewType, "create") && !model)
                return close(viewId);

            if (isSubtypeOf(viewType, "create")){
                return close(viewId).done(function(){
                    var modelId = model[site.getMetadata(modelName)["idPropertyName"]];
                    getView(modelName, "details" + viewType.substr(6), modelId, {modelId: modelId}, null, {removeExisting: true});
                });
            }
            return getView(modelName, "details" + viewType.substr(6), viewDetails.instanceId, viewDetails.params, null, viewDetails.getViewOptions);
        };

        /**
         * Refresh the content of a view.
         * @param viewId the view to refresh.
         * @param pageDelta if the model is a list then when refresh we can change the page index.
         * @returns {*}
         */
        var refresh = function (viewId, pageDelta) {

            var element = $("#" + viewId);
            if (element.size() == 0)
                return createPromise(null);

            var viewDetails = findViewDetails(viewId);

            //get the url to retrieve json data.
            var jsonUrl = null;
            var viewType = viewDetails.viewType;
            if (isSubtypeOf(viewType, "list")) {
                jsonUrl = site.searchJsonUrl(viewDetails.modelName);
            } else {
                jsonUrl = site.modelJsonUrl(viewDetails.modelName, viewDetails.instanceId);
            }

            //change the page.
            var operationKey = "refresh-" + viewId;
            var model = viewDetails.postData;
            if (pageDelta) {
                model["pageIndex"] += pageDelta;
            }

            var createRequest = function (jsonUrl, model) {
                var ajaxPromise;
                if (model) { //need to post
                    ajaxPromise = $.ajax(
                        jsonUrl,
                        {
                            type: "POST",
                            data: JSON.stringify(model),
                            contentType: "application/json",
                            dataType: "json"
                        }
                    );
                } else {
                    ajaxPromise = $.get(jsonUrl);
                }
                return ajaxPromise;
            };

            return beginOp(operationKey).fail(function () {
                reportError("Cannot begin operation.");
            }).then(function () {
                return createRequest(jsonUrl, model).then(
                    function (json) {
                        //refresh result
                        safeApply(function () {
                            viewDetails.model = json;
                            viewDetails.scope.master = viewDetails.model["content"];
                            viewDetails.scope.reset();
                        });
                        return createPromise(null);
                    },
                    function (jqXHR, textStatus, description) {
                        return createPromise("Failed to refresh view " + viewId + ": " + (description ? description : textStatus));
                    }
                ).always(function () {
                        return endOp(operationKey);
                    });
            });
        };

        /**
         * Close a view.
         */
        var close = function (viewId) {

            safeApply(function () {
                var viewKey = findViewKey(viewId);
                if (viewKey)
                    delete viewMap[viewKey];
            });
            $("#" + viewId).remove();
            site.layout();
            return createPromise(null);
        };

        var prePostProcessors = {

            propertyValue: function (model, path, args) {

                if (args.length == 0)
                    throw {message: "propertyValue pre-post processor requires a property name argument."};

                var propertyName = args[0];
                try {
                    var target = zcl.getProp(model, path);
                } catch (ex) {

                }
                if (angular.isObject(target)) {
                    var value = target[propertyName];

                    if (angular.isUndefined(value)) {
                        value = null;
                    }
                    zcl.setProp(model, path, value);
                }
            }
        };

        /**
         * Update model before posting it to the server.
         * @param model the model to post.
         * @param eventScope
         */
        var prePostHandler = function (model, eventScope) {
            var formElement = null;
            if (eventScope && eventScope["eventElement"]) {
                formElement = $(eventScope["eventElement"]).closest("form");
            }

            if (!formElement) {
                return;
            }

            $(formElement[0]).closest("form").find("[data-pre-post]").each(function (index, element) {
                element = $(element);
                var path = element.data("ngModel");
                if (!path)
                    return;

                path = zcl.subStrAfterFirst(path, ".", false);
                var processorString = element.data("prePost");
                var processorName = zcl.subStrBeforeFirst(processorString, ":", false);
                var args = processorString.split(":");
                args.shift();
                var processor = prePostProcessors[processorName];
                if (!processor)
                    return;
                processor(model, path, args);

            });

        };

        return {
            loadList: loadList,
            getView: getView,
            save: save,
            remove: remove,
            cancel: cancel,
            refresh: refresh,
            close: close,
            prePost: prePostHandler,
            sequenceNumbers: {}
        };

    }]);

    //endregion

    //region directives

    /**
     * This directive enables an element to open a search view for a model when clicked.
     */
    spongeApp.directive("spgSearch", ["spongeService", function (spongeService) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {

                $(element).click(function ($event) {
                    var modelName = attrs["spgSearch"];
                    spongeService.getView(modelName, "search", null, null, null, {viewTypeInViewKey: true})
                        .fail(function (error) {
                            reportError(error);
                        });
                    $event.preventDefault();
                });
            }
        };
    }]);

    /**
     * This directive enables an element to open a create view for a model when clicked.
     */
    spongeApp.directive("spgCreate", ["spongeService", function (spongeService) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {

                $(element).click(function ($event) {
                    var modelName = attrs["spgCreate"];
                    spongeService.getView(modelName, "create", null, null, null, {viewTypeInViewKey: true})
                        .fail(function (error) {
                            reportError(error);
                        });
                    $event.preventDefault();
                });
            }
        };
    }]);

    /**
     * Annotate an element so that when clicked the update view of a model is opened and its details view closed (if one is open)..
     */
    spongeApp.directive("spgUpdate", ["spongeService", function (spongeService) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {

                $(element).click(function ($event) {
                    var args = JSON.parse(attrs["spgUpdate"]);
                    var modelId = args["modelId"];
                    spongeService.getView(args["modelName"], "update", modelId, {modelId: args["modelId"]}, null, {removeExisting: true})
                        .fail(function (error) {
                            reportError(error);
                        });
                    //$event.preventDefault();
                });
            }
        };
    }]);

    /**
     * Annotate an element so that when clicked the details view of a model is opened.
     */
    spongeApp.directive("spgDetails", ["spongeService", function (spongeService) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {

                $(element).click(function ($event) {
                    var args = JSON.parse(attrs["spgDetails"]);
                    var modelName = args["modelName"];
                    var modelId = args["modelId"];

                    if (typeof modelId == "string") {
                        if (modelId.substring(0, 1) == "#") {
                            modelId = $(modelId).val();
                            if (isNaN(parseInt(modelId))) {
                                modelId = zcl.subStrBeforeFirst($(args["modelId"]).val(), '-', true);
                            }
                        }
                    }
                    if (!modelId)
                        return;

                    spongeService.getView(modelName, "details", modelId, {modelId: modelId}, null, {removeExisting: true})
                        .fail(function (error) {
                            reportError(error);
                        });
                    //$event.preventDefault();
                });
            }
        };
    }]);

    /**
     * Annotate an element so that when clicked the object is deleted.
     * If successful then will also delete first .list-group-item abcestor.
     */
    spongeApp.directive("spgDelete", ["spongeService", function (spongeService) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {

                var jqElem = $(element);
                jqElem.click(function ($event) {
                    $event.stopPropagation();
                    var args = JSON.parse(attrs["spgDelete"]);
                    var modelName = args["modelName"];
                    var modelId = args["modelId"];
                    modelId = resolveModelId(modelId);
                    if (!modelId)
                        return;

                    spongeService.remove(modelName, modelId, jqElem.closest(".view").attr("id"))
                        .fail(function (error) {
                            reportError(error);
                        });
                });
            }
        };
    }]);

    spongeApp.directive("spgRemovableCollection", ["$parse",function($parse){

        return {
            restrict: 'A',
            scope: true,
            link: function (scope, element, attrs) {
                var collectionPath = attrs["spgRemovableCollection"];
                if(!collectionPath)
                    return;

                var getter = $parse(collectionPath);
                scope.remove = function(item){
                    var collection = getter(scope);
                    if(!collection)
                        return;

                    for(var i=0; i<collection.length; i++){
                        if(angular.equals(collection[i], item)){
                            collection.splice(i, 1);
                            return;
                        }
                    }
                };
            }

        };

    }]);

    //data-spg-load-list="${itemsVariableName},${url}"
    spongeApp.directive("spgLoadList", ["spongeService", function(spongeService){

        return {
            restrict: 'A',
            scope: true,
            link: function (scope, element, attrs) {
                var args = attrs["spgLoadList"];
                if(!args)
                    return;
                args = args.split(",");
                if(args.length < 2)
                    return;//configuration error

                var listPropertyName = args[0];
                var listUrl = args[1];
                var refresh = args.length > 2 ? args[2] : false;

                scope[listPropertyName] = [];
                spongeService.loadList(listUrl, refresh, function(loadedList){
                    safeApply(function(){
                        scope[listPropertyName] = loadedList;
                    });
                });
            }
        };
    }]);

    //data-spg-upload
    spongeApp.directive("spgUpload", ["spongeService", function(spongeService){

        return {
            restrict: 'A',
            link: function (scope, element, attrs) {

                var fileElement = $(element);
                fileElement.css("position", "absolute").css("width", "0").css("height","0").css("overflow","hidden").css("opacity","0");
                var viewId = fileElement.closest(".view[data-ng-controller='viewController']").attr("id");
                fileElement.fileupload({
                    dataType: 'json',
                    add: function (e, data) {
                        data.url = fileElement.data("url");
                        data.submit();
                    },
                    done: function (e, data) {
                        if(data.result["status"] == "OK"){
                            spongeService.refresh(viewId, 0);
                        } else {
                            reportError(data.result["description"]);
                        }
                    },
                    error: function (data, status, e) {
                        reportError(e);
                    }
                });


            }
        };
    }]);

    //data-spg-upload-list="${fieldRef}" <- upload result is put into this collection
    spongeApp.directive("spgUploadList", ["$parse", function($parse){

        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var targetCollection = attrs["spgUploadList"];
                var getter = $parse(targetCollection);
                var setter = getter.assign;
                var fileElement = $(element);

                fileElement.fileupload({
                    dataType: 'json',
                    done: function (e, data) {
                        if(data.result["status"] == "OK"){
                            var collection = getter(scope);
                            if(!collection){
                                setter(scope, []);
                            }
                            collection = getter(scope);

                            safeApply(function(){
                                zcl.addAll(collection, data.result["content"]);
                            });
                        } else {
                            reportError(data.result["description"]);
                        }
                    },
                    error: function (data, status, e) {
                        reportError(e);
                    }
                });
            }
        };
    }]);

    spongeApp.directive("spgSelect", ["$parse", function ($parse) {

        return {
            restrict: 'A',
            scope: true,
            link: function (scope, element, attrs) {
                var args = JSON.parse(attrs["spgSelect"]);
                if (!args)
                    return;

                var collectionPath = args["collectionPath"];
                if(!collectionPath)
                    return;

                var getter = $parse(collectionPath);
                var setter = getter.assign;

                scope.add = function(item){
                    if(angular.equals(item, {}))
                        return;

                    var collection = getter(scope);
                    if(!collection){
                        setter(scope, []);
                    }
                    collection = getter(scope);
                    for(var i=0; i<collection.length; i++){
                        if(angular.equals(collection[i], item))
                            return;
                    }
                    collection.push(item);
                };

                $(element).change(function(){

                    safeApply(function(){
                        var val = $(element).val();
                        if(!val)
                            return;

                        var item = scope.dropdownList[parseInt(val)];
                        scope.add(item);
                    });
                });

                var modelName = args["modelName"];
                if (!modelName)
                    return;
                var url = site.dropdownJsonUrl(modelName);

                $.ajax(
                    url,
                    {
                        type: "GET",
                        contentType: "application/json"
                    }
                ).then(function (result) {
                        safeApply(function () {
                            var list = result.content;
                            if(list == null){
                                list = [];
                            }
                            list.unshift({});
                            scope.dropdownList = list;
                        });
                    });

            }
        };
    }]);


    /**
     * Annotate an element so that when clicked the details view of a model is opened.
     */
    spongeApp.directive("spgList", ["spongeService", function (spongeService) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var args = JSON.parse(attrs["spgList"]);
                var modelName = args["modelName"];
                var variant = args["variant"];
                variant = variant ? "_" + variant : ""; //keep the variant of the initiating view
                var criteriaPath = args["criteriaPath"];
                var containingViewId = element.closest(".view").attr("id");

                $(element).click(function ($event) {
                    //get criteria.
                    var criteria = {};
                    if (criteriaPath) {
                        criteria = scope[criteriaPath];
                        if (angular.isUndefined(criteria) || criteria == null) {
                            reportError("Criteria is not set.");
                            return false;
                        }
                    }

                    scope["eventElement"] = element;
                    spongeService.getView(modelName, "list" + variant, null, null, criteria, {
                        instanceIdInViewKey: true,
                        initiatingViewId: containingViewId
                    }, scope)
                        .fail(function (error) {
                            reportError(error);
                        });
                });
            }
        };
    }]);

    spongeApp.directive("spgSave", ["spongeService", function (spongeService) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {

                $(element).click(function ($event) {

                    if ($(element).closest("form").hasClass("ng-invalid")) {
                        reportError("Please correct error(s) in the form first.");
                        safeApply(function () {
                            scope.showError = true;
                        });
                        return false;
                    }

                    var viewId = attrs["spgSave"];
                    spongeService.save(viewId)
                        .fail(function (error) {
                            reportError(error);
                        })
                        .done(function () {
                            safeApply(function () {
                                scope.showError = false;
                            });
                        });

                    return false;
                });
            }
        };
    }]);

    /**
     * Can refresh update., details and list
     */
    spongeApp.directive("spgRefresh", ["spongeService", function (spongeService) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {

                var args = JSON.parse(attrs["spgRefresh"]);
                var viewId = args["viewId"];
                if (!viewId)
                    return;

                $(element).click(function ($event) {

                    $event.preventDefault();
                    if ($(this).parent().hasClass("disabled"))
                        return false;

                    spongeService.refresh(viewId, args["pageDelta"])
                        .fail(function (error) {
                            reportError(error);
                        });
                    return false;

                });
            }
        };
    }]);

    spongeApp.directive("spgCancel", ["spongeService", function (spongeService) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {

                $(element).click(function () {

                    if ($(element).closest("form.spg-form").hasClass("ng-dirty")) {
                        if (!confirm("Do you want to discard un-saved changes in the form?"))
                            return false;
                    }

                    spongeService.cancel(attrs["spgCancel"])
                        .fail(function (error) {
                            reportError(error);
                        });

                    return false;
                });
            }
        };
    }]);

    spongeApp.directive("spgClose", ["spongeService", function (spongeService) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var targetId = attrs["spgClose"];
                if (!targetId)
                    return;

                var targetElement = $("#" + targetId);
                //var viewTitle = targetElement.find(".panel-heading .title-text").text();
                $(element).click(function () {

                    if (targetElement.find("form.spg-form").hasClass("ng-dirty")) {
                        if (!confirm("Do you want to discard un-saved changes in the form?"))
                            return false;
                    }

                    var gotoId = goBackElementId(targetElement);
                    spongeService.close(targetId)
                        .done(function () {
                            scrollTo(gotoId);
                        })
                        .fail(function (error) {
                            reportError(error);
                        });
                    return false;
                });
            }
        };
    }]);

    //data-spg-hide="hide_element_id"
    spongeApp.directive("spgHide", function () {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var targetId = attrs["spgHide"];
                if (!targetId)
                    return;

                $(element).click(function () {
                    var targetElement = $("#" + targetId);
                    var gotoId = goBackElementId(targetElement);
                    if (display(targetId, false)) {
                        safeApply(zcl.emptyFunc);
                        scrollTo(gotoId);
                        return true;
                    }
                    return false;
                });

            }
        };
    });

    //data-spg-show="show_element_id"
    spongeApp.directive("spgShow", function () {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var targetId = attrs["spgShow"];
                $(element).click(function () {
                    display(targetId, true);
                });
            }
        };
    });

    spongeApp.directive("spgDate", ["$filter", function ($filter) {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function (scope, element, attrs, ngModel) {

                var format = attrs["spgDate"];

                var dateParser = function (value) {
                    var result = +moment(value, format);
                    if (angular.isNumber(result) && !isNaN(result))
                        return result;
                    return value;
                };

                var dateFormatter = function (value) {
                    var result = $filter('moment')(value, format);
                    if (result)
                        return result;
                    return value;
                };

                var update = function () {

                    safeApply(function () {
                        var text = $(element).val();
                        ngModel.$setViewValue(text);
                        ngModel.$commitViewValue();
                    });
                };

                ngModel.$parsers.push(dateParser);
                ngModel.$formatters.push(dateFormatter);
                $(element).data("update", update);

                setTimeout(function(){
                    var input = $(element);
                    var dateFormat = input.data("spgDate");
                    input.closest(".date")
                        .datetimepicker({format: dateFormat})
                        .on("dp.change", function () {
                            setTimeout(input.data("update"), 0);
                        });
                }, 50);
            }
        };
    }]);

    //data-spg-toTop
    spongeApp.directive("spgToTop", function () {
        return {
            restrict: 'A',
            link: function (scope, element) {
                $(element).click(function () {
                    scrollTo("");
                    return false;
                });
            }
        };
    });

    //data-spg-new-scope
    spongeApp.directive("spgNewScope", function () {
        return {
            restrict: 'AE',
            scope: true
        };
    });

    //data-toggle
    spongeApp.directive("spgToggle", function () {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var variable = attrs["spgToggle"];
                if (!variable)
                    return;

                $(element).click(function () {
                    scope.$apply(function () {
                        scope[variable] = !scope[variable];
                    });
                    site.layout();
                    return false;
                });
            }
        };
    });

    spongeApp.directive("spgCombo", function (spongeService) {
        return {
            restrict: 'A',
            require: 'ngModel',
            scope: true,
            link: function (scope, element, attrs, ngModel) {

                scope.ngModel = ngModel;
                var config = {delay: 500, validationErrorKey: "spg-combo"};
                var configOverride = JSON.parse(attrs["spgCombo"]);
                $.extend(config, configOverride);
                var input = $(element).find("input");
                var ol = $(element).find("ol");

                var getList = function () {
                    var text = input.val();
                    if (!text) {//clear list
                        scope.comboList = [];
                        scope.selectedIndex = -1;
                        scope.showList = false;
                        scope.$digest();
                        return;
                    }

                    scope.showList = true;
                    scope.loadingList = true;
                    scope.comboList = [];
                    scope.selectedIndex = -1;
                    scope.$digest();

                    var promise = input.data("pending-list-request");
                    if (promise) {
                        promise.abort();
                    }
                    var model = {keywords: text};
                    var data = JSON.stringify(model);
                    promise = $.ajax(site.listJsonUrl(config["targetModelName"]), {
                        type: "POST",
                        data: data,
                        contentType: "application/json",
                        dataType: "json"
                    });
                    input.data("pending-list-request", promise);

                    promise.done(function (data) {
                        if (data && data.content) {
                            scope.comboList = data.content;
                        } else {
                            scope.comboList = [];
                        }

                    }).fail(function () {
                        scope.showList = false;
                    }).always(function () {
                        scope.loadingList = false;
                        input.data("pending-list-request", null);
                        scope.$apply();
                    });
                };

                scope.updateView = function (model) {
                    ngModel.$setViewValue(model);
                    ngModel.$commitViewValue();
                    ngModel.$setValidity(config.validationErrorKey, true);
                };

                ngModel.$render = function () {
                    if (scope.ngModel.$viewValue) {
                        var format = config["displayFormat"];
                        var text = scope.$eval(format);
                        input.val(text);
                    } else {
                        input.val(null);
                    }
                };

                input.focus(function () {
                    ngModel.$setTouched();
                    input.select();
                });

                var closeComboList = function () {
                    ngModel.$rollbackViewValue();
                    scope.showList = false;
                    scope.loadingList = false;
                    scope.comboList = [];
                    input.data("pending-change-handler", null);
                    if (scope.$$phase != "$digest" && scope.$$phase != "$apply")
                        scope.$digest();
                    ngModel.$setValidity(config.validationErrorKey, true);
                };

                input.blur(function () {
                    setTimeout(closeComboList, config.delay);
                });

                var handleSpecialKeys = function(event){
                    var keyCode = event.keyCode;
                    if(keyCode == 32)
                        return true;

                    if(scope.showList && !scope.loadingList){
                        if(keyCode == 39 || keyCode == 40){
                            if(scope.comboList.length > 0){
                                scope.selectedIndex = (scope.selectedIndex + 1) % scope.comboList.length;
                                if (scope.$$phase != "$digest" && scope.$$phase != "$apply")
                                    scope.$digest();
                            }
                            return true;
                        }

                        if(keyCode == 37 || keyCode == 38){
                            if(scope.comboList.length > 0){
                                if(scope.selectedIndex > 0){
                                    scope.selectedIndex--;
                                } else {
                                    scope.selectedIndex = scope.comboList.length - 1;
                                }
                                if (scope.$$phase != "$digest" && scope.$$phase != "$apply")
                                    scope.$digest();
                            }
                            return true;
                        }

                        if(keyCode == 9 || keyCode == 13){

                            if(scope.selectedIndex >= 0){
                                scope.updateView(scope.comboList[scope.selectedIndex]);
                                closeComboList();
                                safeApply(zcl.emptyFunc);
                            }
                            return true;
                        }

                        if(keyCode == 27){
                            closeComboList();
                            return true;
                        }
                    }

                    return !(event.key && event.key.length == 1);
                };

                input.keypress(function (event) {

                    if(handleSpecialKeys(event)){
                        return;
                    }

                    ngModel.$setValidity(config.validationErrorKey, false);
                    ngModel.$setViewValue(null);

                    var target = $(event.target);
                    var pendingChangeHandler = target.data("pending-change-handler");
                    if (pendingChangeHandler) {
                        clearTimeout(pendingChangeHandler);
                    }
                    pendingChangeHandler = setTimeout(getList, config.delay);
                    target.data("pending-change-handler", pendingChangeHandler);
                });
            }
        };
    });

    //http://stackoverflow.com/questions/25344368/angular-ng-model-empty-strings-should-be-null
    /**
     * Use on an input element. If the input is empty string, convert it to a null value.
     */
    spongeApp.directive('spgNoEmptyString', function () {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function (scope, elem, attrs, ctrl) {
                ctrl.$parsers.push(function (viewValue) {
                    if (viewValue == "") {
                        return null;
                    }
                    return viewValue;
                });
            }
        };
    });

    /**
     * If enter key is pressed on this element, call the click event of another element.
     */
    spongeApp.directive('spgEnterToClick', function () {
        return {
            restrict: 'A',
            link: function (scope, elem, attrs, ctrl) {
                var selector = attrs["spgEnterToClick"];
                if (!selector)
                    return;

                var jqElem = $(elem);
                jqElem.keypress(function (event) {
                    if (event.keyCode != 13) {
                        return;
                    }

                    jqElem.parent().find(selector).click();
                    event.preventDefault();
                    event.stopPropagation();
                });
            }
        };
    });

    //todo review this directive
    spongeApp.directive("spgBeginLinkRequest", function (spongeService) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var targetId = attrs["spgBeginLinkRequest"];
                $(element).click(function () {
                    spongeService.beginLinkRequest(targetId)
                        .fail(function (error) {
                            reportError(error);
                        });
                    return false;
                });
            }
        };
    });

    spongeApp.directive("spgMin", function (spongeService) {
        return {
            restrict: 'A',
            link: function (scope, element) {
                //todo not implemented.
            }
        };
    });

    spongeApp.directive("spgMax", function (spongeService) {
        return {
            restrict: 'A',
            link: function (scope, element) {
                //todo not implemented.
            }
        };
    });

    //endregion

    //region controllers

    spongeApp.controller("spongeController", ["$scope", "$http", "spongeService", "$modal", function ($scope, $http, spongeService, $modal) {

        $scope.linkRequests = []; //need to select an model to complete the operation.

        $scope.operationLocks = []; //in-progress long running operations

        $scope.scrollTo = scrollTo;

        site.ajs.$modal = $modal;

        /**
         * load application metadata which sponge.js depends on.
         */
        $scope.loadMetadata = function () {
            $http.get(site.metadataUrl()).success(function (data, status, headers) {

                //init metadata
                var metadata = data.content;
                $scope.metadata = metadata;
                site.getMetadata = function(modelName){
                    return metadata[modelName];
                };

                //init menu
                var menu = [];
                for (var modelName in metadata) {
                    var model = metadata[modelName];
                    if (model["searchable"]) {
                        menu.push(model);
                    }
                }
                menu.sort(function (m1, m2) {
                    return m1.name.localeCompare(m2.name);
                });
                $scope.menu = menu;

            }).error(function (data, status, headers) {
                reportError('Failed to load application metadata, please retry later. Error:' + status);
            });
        };

        $scope.closeResult = function (resultName) {

            var targetElement = $("#" + resultName);
            if (targetElement.find("form.spg-form").hasClass("ng-dirty")) {
                if (!confirm("Do you want to discard un-saved changes in the form?"))
                    return false;
            }

            spongeService.close(resultName);
        };

        $scope.getViewIds = function () {
            var result = [];
            var keys = zcl.getOwnProperties(viewMap);
            for (var i = 0; i < keys.length; i++) {
                var key = keys[i];
                var viewId = viewMap[key].viewId;
                var display = $("#" + viewId).css("display");
                if (display && display != "none" && display != "hidden")
                    result.push(viewId);
            }

            return result;
        };

        $scope.hasClass = function (id, className) {
            var element = $("#" + id);
            if (element.size() == 0)
                return false;

            return element.hasClass(className);
        };

        $scope.closeOthers = function (resultName) {
            var resultNames = $scope.getViewIds();
            var alertShown = false;
            for (var i = 0; i < resultNames.length; i++) {
                var otherName = resultNames[i];
                if (otherName != resultName) {
                    if (!alertShown) {
                        if(!confirm("Do you want to close all " + (resultName ? "other" : "") + " views? All unsaved changes will be lost.")){
                            return;
                        } else {
                            alertShown = true;
                        }
                    }
                    spongeService.close(otherName);
                }
            }
        };

        $scope.makeCriteria = function (scope, modelName, propertyName, value) {
            var bodyScope = getBodyScope();
            var promise = ensureSearchModel(bodyScope, modelName, null);
            promise.done(function () {
                var prototype = bodyScope.searchModel[modelName];
                scope.criteria = angular.copy(prototype);
                scope.criteria[propertyName] = value;
            });
        };

        $scope.loadMetadata();

    }]);

    spongeApp.controller("viewController", ["$scope", "$element", function ($scope, $element) {

        var id = $element.attr("id");
        var forms = $element.find("form");
        var viewDetails = findViewDetails(id);
        viewDetails.scope = $scope;
        $scope.hideBody = false;
        $scope.master = viewDetails.model["content"];

        //copy sort properties from initiating view.
        if(viewDetails.getViewOptions.initiatingViewId){
            var initiatingViewDetails = findViewDetails(viewDetails.getViewOptions.initiatingViewId);
            if(initiatingViewDetails && initiatingViewDetails.model["tags"]["sortProperties"]){  //not closed yet and has sortProperties
                $scope.sortProperties = initiatingViewDetails.model["tags"]["sortProperties"];
                $scope.selectedSortProperties = [];
                if(viewDetails.postData && viewDetails.postData["pageSize"]) {
                    viewDetails.postData["sortInfoList"] = $scope.selectedSortProperties;//wire up with the selected sort
                }
            }
        }

        if (isSubtypeOf(viewDetails.viewType, "list")) {
            $scope.previousEnabled = function () {
                var viewDetails = findViewDetails(id);
                if (viewDetails && viewDetails.model["tags"]["prevPage"])
                    return "";
                return "disabled";
            };

            $scope.nextEnabled = function () {
                var viewDetails = findViewDetails(id);
                if (viewDetails && viewDetails.model["tags"]["nextPage"])
                    return "";
                return "disabled";
            };
        }

        $scope.reset = function () {
            $scope.model = angular.copy($scope.master);
            forms.each(function (index, form) {
                if ($scope[form.name]) {
                    $scope[form.name].$setPristine();
                }
            });
            $scope.showError = false;
        };

        $scope.isUnchanged = function () {

            return angular.equals($scope.master, $scope.model);
        };

        $scope.addToCollection = function (collection, modelName) {
            var bodyScope = getBodyScope();
            var promise = ensureNewModel(bodyScope, modelName, null);
            promise.done(function () {
                safeApply(function () {
                    var prototype = bodyScope.newModel[modelName];
                    collection.push(angular.copy(prototype));
                });
            });
        };

        $scope.removeFromCollection = function (collection, item) {
            if (collection && item) {
                var index = collection.indexOf(item);
                if (index >= 0) {
                    collection.splice(index, 1);
                }
            }
        };

        $scope.addToMap = function (map, editorId) {
            var keyInput = $(editorId + " .add_entry");
            var newKey = keyInput.val();
            if (!newKey) {
                keyInput.focus();
                return;
            }
            if (newKey in map) {
                var entries = $(editorId + " .entries .form-group");
                for (var i = 0; i < entries.size(); i++) {
                    var entry = $(entries.get(i));
                    if (entry.find("label").text() == newKey) {
                        entry.find("input").focus();
                        break;
                    }
                }
                return;
            }

            map[newKey] = "";
            setTimeout(function () {
                keyInput.closest(".form-group").find("input[data-key='" + newKey + "']").focus()
            }, 50);
        };

        $scope.removeFromMap = function (map, key, editorId) {
            if (key in map) {
                delete map[key];
            }
        };

        $scope.mapSize = function (obj) {
            return zcl.getOwnProperties(obj).length;
        };

        $scope.reset();
    }]);


    spongeApp.controller('messageBoxController', ['$scope', '$modalInstance', function ($scope, $modalInstance) {

        $scope.title = $modalInstance.data.title;
        $scope.message = $modalInstance.data.message;

        $scope.ok = function () {
            $modalInstance.close(null);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    }]);

    //endregion

    //region jq plugins

    $.fn.starwars = function () {

        var scrollingDiv = this.find(".scrolling > div"); //todo find the line-height of 1em here to replace 50
        var initTop = this.height() + 50 * 2;//delay 2 seconds
        var endTop = -scrollingDiv.height() - 50 * 2;
        var duration = (initTop - endTop) / 50 * 1000;//100px per sec

        scrollingDiv.css("top", initTop);

        var replay = function () {

            scrollingDiv.css("top", initTop);
            scrollingDiv.animate({top: endTop}, {
                duration: duration,
                easing: "linear",
                complete: replay
            });

        };

        scrollingDiv.animate({top: endTop}, {
            duration: duration,
            easing: "linear",
            complete: replay
        });

        var perspectiveValue = Math.round(this.height() * 0.667) + "px";
        this.css("-webkit-perspective", perspectiveValue);
        this.css("perspective", perspectiveValue);
    };


    $.fn.makeTab = function () {
        var tabHeaders = this.find("a[data-tab-pane]");
        var tabContent = this.nextAll(".tab-content");

        var clickHandler = function () {
            var newLi = $(this).closest("li");
            if (newLi.hasClass("active"))
                return false;

            //deactivate old tabpage
            for (var i = 0; i < tabHeaders.length; i++) {
                var oldA = $(tabHeaders.get(i));
                var oldLi = oldA.closest("li");
                if (!oldLi.hasClass("active"))
                    continue;

                oldLi.removeClass("active");
                var oldPaneName = oldA.attr("data-tab-pane");
                tabContent.find("div[data-tab-pane=" + oldPaneName + "]").removeClass("active");
                //tabContent.find("div[data-tab-pane=" + oldPaneName + "]").addClass("visibility-hidden");
                break;
            }

            //activate new tabpage
            var newPaneName = $(this).attr("data-tab-pane");
            tabContent.find("div[data-tab-pane=" + newPaneName + "]").addClass("active");
            //tabContent.find("div[data-tab-pane=" + newPaneName + "]").removeClass("visibility-hidden");
            newLi.addClass("active");

            return false;
        };
        tabHeaders.click(clickHandler);
        return this;
    };

    //endregion

})();