/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function sendXmlPostRequest(url, data, callback, error_callback) {
    var request = new XMLHttpRequest();
    request.open("POST", url, true);
    request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    request.onreadystatechange = function () {
        if (this.readyState === 4 && (this.status >= 200 && this.status < 300)) {
            callback(request.response);
        } else if (this.status !== 200) {
            error_callback(request.response)
        }
    };
    request.send(JSON.stringify(data));
}
function sendXmlGetRequest(url, callback, error_callback) {
    var request = new XMLHttpRequest();
    request.open("GET", url, true);
    request.onreadystatechange = function () {
        if (this.readyState === 4 && (this.status >= 200 && this.status < 300)) {
            callback(request.response);
        } else if (this.status !== 200) {
            error_callback(request.response)
        }

    };
    request.send();
}
function sendXmlPutRequestJsonData(url, data, callback, error_callback) {
    var request = new XMLHttpRequest();
    request.open("PUT", url, true);
    request.setRequestHeader("Content-Type", "application/json");
    request.onreadystatechange = function () {
        if (this.readyState === 4 && (this.status >= 200 && this.status < 300)) {
            callback(request.response);
        } else if (this.status !== 200) {
            error_callback(request.response)
        }

    };
    request.send(JSON.stringify(data));
}
function sendXmlPutRequest(url, callback, error_callback) {
    var request = new XMLHttpRequest();
    request.open("PUT", url, true);

    request.onreadystatechange = function () {
        if (this.readyState === 4 && (this.status >= 200 && this.status < 300)) {
            callback(request.response);
        } else if (this.status !== 200) {
            error_callback(request.response)
        }

    };
    request.send();
}
function sendXmlDeleteRequest(url, callback, error_callback) {
    var request = new XMLHttpRequest();
    request.open("DELETE", url, true);

    request.onreadystatechange = function () {
        if (this.readyState === 4 && (this.status >= 200 && this.status < 300)) {
            callback(request.response);
        } else if (this.status !== 200) {
            error_callback(request.response)
        }

    };
    request.send();
}