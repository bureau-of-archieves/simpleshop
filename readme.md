Simpleshop Single Page Web Application
=======

Summary
-----------
This is an experimental single page web application based on the libraries/frameworks below:

* Bootstrap
* jQuery
* AngularJS
* Spring Framework
* Drools
* Hibernate
* H2 Database

The goal of this project is to create a framework which can quickly build a CRUD web application for a given domain. 

The page is divided into a header area and a view area. The view area can display a list of independent views. 
Currently there are 5 types of views:
* Search view - provides a form to search for a type of objects
* List view - displays search results which is a list of objects; support paging and sorting of a single field.
* Details view - displays a single object in detail
* Update view - provides a form to update an object
* Create view - provides a form to create an object

All views have a model type, which is the type of model it can display. 
Views have all their backing data stored in a central location on the client side, including content, open parameters and so on.
Domain object metadata is accessible on both server side and client side.

Below is a screenshot of the user interface:
<img src="img/screenshot1.png">

Server locale is set via JVM options. Please ensure they are <code>java -Duser.country=AU -Duser.language=en</code> which is the only supported server locale at the moment.
Configuration data for other locale is not set up in this demo project. 

Bugs
-------------


Todo List
-----------
 * Spring security overwrites the http caching headers for the jsp views.The jsp views should not use spring security
 * Implement get user profile.
 * pricing module - price can depend on purchase or preset supplier price. also implement discount and rebate rules.
 * Add default employee and shipper.
 * Direct error messages to each field as well as the whole model.
 * create a model after searching will copy certain search parameters.
 * Display search criteria in the list view.
 * Trim unnecessary parts of the request before posting json.
 * a changeable user locale, first set at login time. it affects ui language, default payment currency.
 * disabled state for edit controls - e.g. editngselect will not need to retrieve a list when disabled. this disabled state is stored on server side as well.
 * allow adding purchase - from supplier, by employee, product list and amount, price per unit
 * calculate stock, pending amount - ordered but not delivered
 * auditing - object created, field changed, object deleted, user logins, top level controller actions
 * watch product notification when price change or become in stock. check out and record transaction.
 * spgCombo - commonly used values when the control is focused but no keywords typed yet.
 * Add proper logging.

Future Todo List 
-----------
 * css/script bundling with gulp
 * support for named inline representation - a property annotation that combines interpolate and displayFormat with a name
 * Support full internationalization - in jsp and angular.
 * Separate template and data. This way a type of template can be cached by the client.
 * drag and drop swap view positions.
 * catch error when parsing embedded json data fails
 * email & print
 * scheduled job
 * reporting
 * page notification 
 * externalize config
 * 
 
Limitations
-----------
The following architectural limitations are accepted and no attempt will be made to address them.

* The search framework cannot generate very sophisticated queries especially when there are nested OR and AND expressions.Things difficult to achieve with Hibernate Criteria API are not attempted.
* The 5 basic views display the associated type of domain object directly. Create a display view for a new view model if other types of query results are to be returned. These view models are considered read only. 
* To simplify things each view is independent. A view will not use any part of the model object of another view. 







