"use strict";
/*
 * gulp tasks are defined here.
 */
var gulp = require('gulp');
var source = require("vinyl-source-stream"); //use conventional text streams with gulp
var concat = require("gulp-concat"); //concatenates files
//var lint = require("gulp-eslint"); //lint js and jsx files

var config = {
    jsFiles:[
        "./src/main/webapp/components/jquery/dist/jquery.min.js",
        "./src/main/webapp/js/jquery.iframe-transport.js",
        "./src/main/webapp/components/moment/min/moment.min.js",
        "./src/main/webapp/components/angular/angular.min.js",
        "./src/main/webapp/js/angular-animate.min.js",
        "./src/main/webapp/components/toastr/toastr.min.js"
    ]
};

gulp.task("processJS", function(){
    gulp.src(config.jsFiles)
        .pipe(concat("bundle.js"))
        .pipe(gulp.dest("./src/main/webapp/js"));
});


//
//gulp.task("lint", function(){
//    return gulp.src(config.paths.js)
//        .pipe(lint({config: 'eslint.config.json'}))
//        .pipe(lint.format());
//});
//
//gulp.task("watchHtmlFiles", function(){
//    gulp.watch(config.paths.html, ["reloadHtmlFiles"]);
//    gulp.watch(config.paths.js, ["processJS", "lint"]);
//});
//

gulp.task("default", ["processJS"]);