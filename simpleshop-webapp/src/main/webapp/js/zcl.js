/**
 * This js file uses Jsdoc, whose usage is detailed in the "Tag Dictionary" section of the following url:
 * http://usejsdoc.org/
 *
 * See /examples folder for example usage of the functions.
 */
(function () {

    if (window.zcl) {
        return;//name already taken, abort initialization.
    }
    window.zcl = {};


    var nestedPropRegex = /\[([\w0-9._$]+)]/g;

    var placeholderRegex = /\{([\w0-9._$]+)}/g;

    /**
     * Get the nested property of an object.
     * @param {object} o - an object; must not be null.
     * @param {string} s - dot separated list of property names, e.g. 'contactDetails.address.postcode'.
     * @returns {*} the property value or null of one is not found.
     */
    zcl.getProp = function (o, s) {
        s = s.replace(nestedPropRegex, '.$1'); // convert indexes to properties
        s = s.replace(/^\./, '');           // strip a leading dot
        var a = s.split('.');
        while (a.length) {
            var n = a.shift();
            if (n in o) {
                o = o[n];
            } else {
                return undefined;
            }
        }
        return o;
    };


    /**
     * Set the nested property of an object. This is only done if the object before the last dot exists.
     * @param {object} o - an object; must not be null.
     * @param {string} s - dot separated list of property names, e.g. 'contactDetails.address.postcode'.
     * @param v - property value.
     * @returns {boolean} true if the property is successfully set.
     */
    zcl.setProp = function (o, s, v) {
        s = s.replace(nestedPropRegex, '.$1'); // convert indexes to properties
        s = s.replace(/^\./, '');           // strip a leading dot
        var a = s.split('.');

        for (var i = 0; i < a.length - 1; i++) {
            o = o[a[i]];
            if (!o)
                return false;
        }

        o[a[a.length - 1]] = v;
        return true;
    };


    /**
     * Get the lst of own property names of obj.
     * @param {object} obj - an object.
     * @returns {string[]} names of own properties on obj. Will return [] if none is found.
     */
    zcl.getOwnProperties = function (obj) {
        var names = [];
        var i = 0;
        for (var propName in obj) {
            if (obj.hasOwnProperty(propName))
                names[i++] = propName;
        }
        return names;
    };


    /**
     * Enumerate all combinations of combinationSize elements in the sub-list of parameter list starting from startIndex (inclusive).
     * Redraw is not allowed, e.g. from list ['a', 'b', 'c'] we can only get one combination of size 3: ['a', 'b', 'c']; ['a', 'a', 'a'] is not counted.
     * @param {*[]} list - an array of anything.
     * @param {int} combinationSize - size of combination.
     * @param {function} visitor - a callback function that will be called when a combination is found (passed in as an array parameter); return true to stop enumeration.
     * @param {int} [startIndex] - The actual list to use to enumerate combinations is the sub-list list[startIndex ~ theLastIndex]; defaults to 0 to use the whole list.
     * @returns {boolean} true if enumerated all, false otherwise.
     */
    zcl.combination = function (list, combinationSize, visitor, startIndex) {
        if (typeof startIndex === 'undefined')
            startIndex = 0;

        var buffer = new Array(combinationSize); //populate the combination here

        var enumerate = function (startIndex, remainingCount) { //minimise recursive stack frame size
            if (remainingCount) {
                var bufferIndex = combinationSize - remainingCount;
                var lastIndex = list.length - remainingCount;
                for (var i = startIndex; i <= lastIndex; i++) {
                    buffer[bufferIndex] = list[i];
                    if (!enumerate(i + 1, remainingCount - 1))
                        return false;
                }
            } else {
                if (visitor(buffer))
                    return false;
            }

            return true;
        };

        return enumerate(startIndex, combinationSize);//enumerate all combinations whose length is combinationSize and lowest item index >= 0.
    };


    /**
     * Enumerate all combinations of combinationSize elements in the sub-list of parameter list starting from startIndex (inclusive).
     * Redraw is allowed, e.g. from list ['a'], we can get one combination ['a', 'a', 'a'] whose size is 3.
     * @param {*[]} list - an array of anything.
     * @param {int} combinationSize - size of combination.
     * @param {function} visitor - a callback function that will be called when a combination is found (passed in as an array parameter); return true to stop enumeration.
     * @param {int} [startIndex] - The actual list to use to enumerate combinations is the sub-list list[startIndex ~ theLastIndex]; defaults to 0 to use the whole list.
     * @returns {boolean} true if enumerated all, false otherwise.
     */
    zcl.combinationRedrawAllowed = function (list, combinationSize, visitor, startIndex) {
        if (typeof startIndex === 'undefined')
            startIndex = 0;

        var buffer = new Array(combinationSize); //populate the combination here

        var enumerate = function (startIndex, remainingCount) { //minimise recursive stackframe size
            if (remainingCount) {
                var bufferIndex = combinationSize - remainingCount;
                for (var i = startIndex; i < list.length; i++) {
                    buffer[bufferIndex] = list[i];
                    if (!enumerate(i, remainingCount - 1))
                        return false;
                }
            } else {
                if (visitor(buffer))
                    return false;
            }

            return true;
        };

        return enumerate(startIndex, combinationSize);//enumerate all combinations whose length is combinationSize and lowest item index >= 0.
    };


    /**
     * Format an object into a string.
     * @param {string} template - '{propName}' and '{index}' will be replaced by the corresponding values in obj.
     * @param {Object} obj - the property values of which will be used to fill the placeholders in template.
     * @param {boolean} [dontShowUndefined=false] - if true then placeholders whose values are undefined are not replaced by literal 'undefined'.
     * @returns {string} the formatted string.
     */
    zcl.formatObject = function (template, obj, dontShowUndefined) {
        if (!template || !obj) return template;

        return template.replace(placeholderRegex, function (match) {
            var propValue = zcl.getProp(obj, match.substring(1, match.length - 1));
            if (propValue === undefined && dontShowUndefined)
                return match;
            else
                return propValue;
        });
    };


    /**
     * Calculate the kth statistics of a list of number.
     * @param {number[]} list - a list of number.
     * @param {int} k - specify that we want the kth smallest number.
     * @param {int} [start] - the actual calculation is based on sublist list[start-end]. Defaults to 0.
     * @param {int} [end] - end index of the sublist; defaults to list.length-1; inclusive.
     * @returns {number} the kth smallest number in sublist[start~end].
     */
    zcl.kthStatistics = function (list, k, start, end) {
        if (k < start || k > end) throw "Argument k is out of range.";
        //partition
        var j = start;
        for (var i = start + 1; i <= end; i++) {
            if (list[i] <= list[start]) {
                j++;
                if (i != j) {	//swap
                    var temp = list[i];
                    list[i] = list[j];
                    list[j] = temp;
                }
            }
        }

        var relativeIndex = j - start;
        if (relativeIndex == k) return list[start];
        else if (relativeIndex < k) return zcl.kthStatistics(list, k - relativeIndex - 1, j + 1, end);
        else return zcl.kthStatistics(list, k, start + 1, j);
    };


    /**
     * Move the minimum element to the beginning of the list and the maximum to the end of the list.
     * @param {number[]} list - a list of number.
     */
    zcl.moveMinAndMax = function (list) {
        var blockSize = 1;
        var temp;
        while (blockSize < list.length) {
            for (var i = 0; i * blockSize < list.length; i += 2) {
                var minLeft = i * blockSize;
                var minRight = minLeft + blockSize;

                if (minRight >= list.length) continue;

                if (list[minRight] < list[minLeft]) {//swap
                    temp = list[minLeft];
                    list[minLeft] = list[minRight];
                    list[minRight] = temp;
                }
                if (blockSize == 1) continue;

                var maxLeft = minRight - 1;
                var maxRight = maxLeft + blockSize;
                if (maxRight >= list.length) {
                    maxRight = list.length - 1;
                }
                if (list[maxLeft] > list[maxRight]) {//swap
                    temp = list[maxRight];
                    list[maxRight] = list[maxLeft];
                    list[maxLeft] = temp;
                }
            }
            blockSize *= 2;
        }
    };


    /**
     * Create an array filled with 0s and 1s which is the lower n bits of number.
     * Least significant bit is at index 0.
     * @param {number} number - the number to calculate the binary representation for.
     * @param {int} n - get lowest n bits of number.
     * @returns {int[]} a bit array.
     */
    zcl.toBitArray = function (number, n) {
        var result = [];
        for (var i = 0; i < n; i++) {
            result[i] = number % 2;
            number = Math.floor(number / 2);
        }
        return result;
    };


    /**
     * Create a nested ul structure from a list of tree nodes.
     * @param {Object[]} items - a list of tree nodes in deapth first order.
     * @param {string} contentTemplate - formatObject template, used as the content template of the li elements.
     * @returns {string} the THML fragment of a ul element that represent the tree defined by the items.
     * @todo enable the second parameter to be a function item => string as well.
     */
    zcl.ulTree = function (items, contentTemplate) {
        var buffer = [];
        var parentStack = [0];
        for (var i = 0; i < items.length; i++) {
            var item = items[i];
            var parentIndex = parentStack.lastIndexOf(item.parent);
            //check relationship to the previous item
            var popCount = parentStack.length - (parentIndex + 1);
            if (popCount == 0) { //first child to previous item
                buffer.push("<ul><li>");
                buffer.push(zcl.formatObject(contentTemplate, item));
                buffer.push("</li>");
                //} else if (popCount == 1) { //sibling to previous item; this branch is commented out because it is covered by the branch below
                //	parentStack.pop();
                //	document.write("<li>" + item.id + "(parent = " + item.parent + ")</li>");
            } else { //need to close opening uls
                while (popCount > 0) {
                    parentStack.pop();
                    if (popCount > 1) {
                        buffer.push("</ul>");
                    }
                    popCount--;
                }
                buffer.push("<li>");
                buffer.push(zcl.formatObject(contentTemplate, item));
                buffer.push("</li>");
            }
            parentStack.push(item.id);
        }
        while (parentStack.length > 1) {
            parentStack.pop();
            buffer.push("</ul>");
        }

        return buffer.join('');
    };


    /**
     * Find the first first longest increasing subsequence in list by dynamic programming.
     * @param {number[]} list - a list numbers.
     * @returns {number[]} the first longest increasing subsequence of the passed in list.
     */
    zcl.longestIncreasingSubsequence = function (list) {
        if (list.length == 0)
            return [];

        var subSeqMinEndIdx = [-1, 0]; //subSeqMinEndIdx[j] => the end index of the subsequence whose lengh is j and end value is the smallest.
        var backTrack = [-1]; //a map used to reconstruct the longest increasing subsequence; backTrack[u] -> backTrack[backTrack[u]] -> ....
        for (var i = 1; i < list.length; i++) {
            var start = 1, end = subSeqMinEndIdx.length - 1;//bin search list[i] in list[subSeqMinEndIdx[1]] to list[subSeqMinEndIdx[endIndex]]
            while (start <= end) {
                var mid = Math.floor(start + (end - start) / 2);
                if (list[subSeqMinEndIdx[mid]] == list[i]) {
                    start = -1;
                    break;
                } else if (list[subSeqMinEndIdx[mid]] > list[i]) {
                    end = mid - 1;
                } else {
                    start = mid + 1;
                }
            }

            if (start == -1) //same as one of the end values, no use
                continue;

            if (start == subSeqMinEndIdx.length) {
                backTrack[i] = subSeqMinEndIdx[start - 1]; //found longer increasing subsequence
                subSeqMinEndIdx[start] = i;
            } else if (list[i] < list[subSeqMinEndIdx[start]]) { //existing increasing subsequence got smaller end value
                backTrack[i] = backTrack[subSeqMinEndIdx[start]];
                subSeqMinEndIdx[start] = i;
            }
        }

        //reconstruct
        var result = [];
        var index = subSeqMinEndIdx[subSeqMinEndIdx.length - 1];
        while (index != -1) {
            result.push(list[index]);
            index = backTrack[index];
        }
        return result.reverse();
    };


    //region string functions

    /**
     * Convert the first char of the string to upper case.
     * @param {string} str - a string.
     * @returns {string} string with first char in upper case.
     */
    zcl.firstCharUpper = function (str) {
        if (!str)
            return str;

        return str.charAt(0).toUpperCase() + str.substr(1);
    };

    /**
     * Convert the first char of the string to lower case.
     * @param {string} str - a string.
     * @returns {string} string with first char in lower case.
     */
    zcl.firstCharLower = function (str) {
        if (!str)
            return str;
        return str[0].toLowerCase() + str.substr(1);
    };

    var entityMap = {
        "&": "&amp;",
        "<": "&lt;",
        ">": "&gt;",
        '"': '&quot;',
        "'": '&#39;',
        "/": '&#x2F;'
    };

    /**
     * https://github.com/janl/mustache.js/blob/master/mustache.js#L82
     * @param {string} str the html to sanitize.
     * @returns {string} escaped html text for display.
     */
    zcl.escapeHtml = function (str) {
        return String(str).replace(/[&<>"'\/]/g, function (s) {
            return entityMap[s];
        });
    };


    /**
     * Get the substring between the last last argument and the first first argument after that.
     * @param {string} str - source string.
     * @param {string} last
     * @param {string} first
     * @returns {string}
     */
    zcl.subStrBetweenLastAndFirst = function(str, last, first){

        var lastIndex = str.lastIndexOf(last);
        if(lastIndex != -1){
            str = str.substring(lastIndex + 1);
            var firstIndex = str.indexOf(first);
            if(firstIndex != -1)
                str = str.substring(0, firstIndex);
        }

        return str;
    };

    /**
     * Get the substring before the first first argument.
     * @param {string} str - source string.
     * @param {string} first - separator.
     * @param {boolean} trim - whether to trim result.
     * @returns {string}
     */
    zcl.subStrBeforeFirst = function(str, first, trim){
        if(!str)
            return str;

        var index = str.indexOf(first);
        if(index >= 0){
            str = str.substr(0, index);
        }

        return trim ? str.trim() : str;
    };

    /**
     * Get the substring after the first first argument.
     * @param {string} str - source string.
     * @param {string} first - separator.
     * @param {boolean} trim - whether to trim result.
     * @returns {string}
     */
    zcl.subStrAfterFirst = function(str, first, trim){
        if(!str)
            return str;

        var index = str.indexOf(first);
        if(index >= 0){
            str = str.substr(index + 1);
        }

        return trim ? str.trim() : str;
    };

    zcl.camelNameToUrlName = function(camelName){//camel name to underscored name
        var result = [];
        for(var i=0; i<camelName.length; i++){
            var ch = camelName.charAt(i);
            var lowerChar = ch.toLowerCase();
            if(ch == lowerChar)
                result.push(ch);
            else{
                result.push("_");
                result.push(lowerChar);
            }
        }
        return result.join('');
    };

    zcl.pascalNameToUrlName = function(pascalName){//camel name to underscored name
        var result = [];
        for(var i=0; i<pascalName.length; i++){
            var ch = pascalName.charAt(i);
            var lowerChar = ch.toLowerCase();
            if(ch == lowerChar)
                result.push(ch);
            else{
                if(i != 0)
                    result.push("_");
                result.push(lowerChar);
            }
        }
        return result.join('');
    };

    zcl.endWith = function (src, suffix) {
        var lastIndex = src.lastIndexOf(suffix);
        if(lastIndex < 0)
            return false;

        if(lastIndex + suffix.length == src.length)
            return true;
        return false;
    };

    zcl.camelNameToSpacedName = function (str) {
        var result = [];
        for (var i = 0; i < str.length; i++) {
            var ch = str.charAt(i);
            if (ch == ch.toLowerCase()) {
                result.push(ch);
            } else {
                result.push(' ');
                result.push(ch.toLowerCase());
            }
        }
        return result.join('');
    };

    zcl.objectToKeyValuePair = function(obj, compareFn){
        var properties = zcl.getOwnProperties(obj);
        var result = [];
        for(var i=0; i<properties.length; i++) {
            var prop = properties[i];
            var value = obj[prop];
            result.push({key:prop, value:value});
        }

        if(!compareFn){
            compareFn = function(a, b) {
                return (a.key< b.key?-1:(a.key> b.key?1:0));
            };
        }
        result.sort(compareFn);

        return result;
    };

    zcl.removeAnchor = function(){
        location.hash = '';
    };

    //http://stackoverflow.com/a/4152613/355963
    zcl.repeat = function repeat(pattern, count) {
        if (count < 1) return '';
        var result = '';
        while (count > 1) {
            if (count & 1) result += pattern;
            count >>= 1, pattern += pattern;
        }
        return result + pattern;
    };

    //endregion

})();
