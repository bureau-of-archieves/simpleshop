<%--
  Created by IntelliJ IDEA.
  User: JOHNZ
  Date: 5/06/2015
  Time: 4:26 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Json service test page</title>
    <link rel="stylesheet" href="components/bootstrap/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="components/bootstrap/dist/css/bootstrap-theme.min.css">

    <style type="text/css" >
        .container .row {
            margin-top: 1em;
        }

        .big_block {
            width: 90%;
        }

    </style>
</head>
<body>
<br>
<div class="container">
    <div class="row">
        <div class="col-md-2"><label for="url">Request Url:</label></div>
        <div class="col-md-10">
           <span id="url_prefix"></span> <input id="url">
        </div>
    </div>
    <div class="row">
        <div class="col-md-2"><label for="content">Post Data:</label></div>
        <div class="col-md-10" >
            <textarea id="content" class="big_block" rows="12"></textarea>
        </div>

    </div>
    <div class="row">
        <div class="col-md-offset-2 col-md-1">
            <button id="submit" >Submit</button>
        </div>
        <div class="col-md-9">
            <button id="clear_post" >Clear</button>
        </div>
    </div>

    <hr>

    <div class="row">
        <div class="col-md-2">Response:</div>
        <div  class="col-md-10">
            <pre id="result" class="big_block"></pre>
        </div>
    </div>
</div>

<script src="js/jquery.js"></script>
<script type="text/javascript">
    $(function(){

        var url_prefix = "${pageContext.request.contextPath}/json/";
        $("#url_prefix").html(url_prefix);
        var display_response = function(data){
            $("#result").text(JSON.stringify(data, null, 4));
        };

        var report_error = function(jqXHR, textStatus){
          alert("Request failed: " + textStatus + "\r\n" + jqXHR.statusText);
        };

        $("#submit").click(function(){


            var url = $("#url").val();
            if(!url){
                alert("Type in url.");
                return;
            }
            url = url_prefix + url;

            var content = $("#content").val();

            var ajax_options = {
            };

            if(content){ //post
                ajax_options.method = "POST";
                ajax_options.dataType = "json";
                ajax_options.contentType = "application/json";
                try {
                    content = $.parseJSON(content);
                } catch(err){
                    alert(err);
                    return;
                }
                ajax_options.data = JSON.stringify(content);
            }

            $.ajax(url, ajax_options)
                    .done(display_response)
                    .fail(report_error)
                    .always(function(){

                    });

        });

        $("#clear_post").click(function(){
            $("#content").val("");
        });

    });
</script>
</body>
</html>
