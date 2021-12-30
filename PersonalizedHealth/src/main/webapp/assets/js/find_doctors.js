/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function see_doctors() {
    var option = document.userForm.options.value;
    if (option === "house_dist") {
        var lat = document.getElementById("lat").value;
        var lon = document.getElementById("lon").value;

        var URL = "http://localhost:8080/PersonalizedHealth/examinations/docs/findDocLoc/" + lat + "/" + lon;
        sendXmlGetRequest(URL, print_docs, error);
    } else if (option === "car_dist") {
        const data = null;

        const xhr = new XMLHttpRequest();
        xhr.withCredentials = true;

        xhr.addEventListener("readystatechange", function () {
            if (this.readyState === this.DONE) {
                console.log(this.responseText);
            }
        });

        var lat = document.getElementById("lat").value;
        var lon = document.getElementById("lon").value;

        xhr.open("GET", "https://trueway-matrix.p.rapidapi.com/CalculateDrivingMatrix?origins=40.629041%2C-74.025606%3B40.630099%2C-73.993521%3B40.644895%2C-74.013818%3B40.627177%2C-73.980853&destinations=40.629041%2C-74.025606%3B40.630099%2C-73.993521%3B40.644895%2C-74.013818%3B40.627177%2C-73.980853");
        xhr.setRequestHeader("x-rapidapi-host", "trueway-matrix.p.rapidapi.com");
        xhr.setRequestHeader("x-rapidapi-key", "2b32ebee9emsh8012dc2806237ecp1e91f7jsn49cbb5a1c2b6");

        xhr.send(data);
    } else if (option === "price_rank") {
        var URL = "http://localhost:8080/PersonalizedHealth/examinations/randevouz/rankedRandevouz";
        sendXmlGetRequest(URL, print_ranked_byprice, error);
    }

}

function print_docs(response) {
    var jsonArray = JSON.parse(response);
    jsonArray.sort(GetSortOrder("Distance(in kilometers)"));
    var i = 1;
    document.getElementById("print_docs").innerHTML = "";
    for (id in jsonArray) {
        document.getElementById("print_docs").innerHTML += createTable(jsonArray[id], i);
        i++;
    }
}

function print_ranked_byprice(response) {
    var jsonArray = JSON.parse(response);
    jsonArray.sort(GetSortOrder("Price"));
    var i = 1;
    document.getElementById("print_docs").innerHTML = "";
    for (id in jsonArray) {
        document.getElementById("print_docs").innerHTML += createTable(jsonArray[id], i);
        i++;
    }
}


function createTable(data, i) {
    var html = "<table style='border:2px solid white; background-color: rgb(51, 83, 109);'><tr><th>Category</th><th>Value</th></tr>";
    for (const x in data) {
        var category = x;
        var value = data[x];
        html += "<tr><td>" + category + "</td><td>" + value + "</td></tr>";
    }
    html += "</table><br>";
    return html;
}

function error() {

}

function GetSortOrder(prop) {
    return function (a, b) {
        if (a[prop] > b[prop]) {
            return 1;
        } else if (a[prop] < b[prop]) {
            return -1;
        }
        return 0;
    }
}