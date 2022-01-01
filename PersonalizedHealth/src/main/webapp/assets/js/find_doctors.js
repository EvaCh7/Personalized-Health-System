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
        var URL = "http://localhost:8080/PersonalizedHealth/examinations/docs/doctorsLatLon";

        sendXmlGetRequest(URL, car_distance, error);
    } else if (option === "price_rank") {
        var URL = "http://localhost:8080/PersonalizedHealth/examinations/randevouz/rankedRandevouz";
        sendXmlGetRequest(URL, print_ranked_byprice, error);
    }

}

function print_docs(response) {
    var jsonArray = JSON.parse(response);
    jsonArray.sort(GetSortOrder("Distance(in kilometers)"));
    document.getElementById("print_docs").innerHTML = "";
    for (id in jsonArray) {
        document.getElementById("print_docs").innerHTML += createTable(jsonArray[id]);
    }
}

function print_ranked_byprice(response) {
    var jsonArray = JSON.parse(response);
    jsonArray.sort(GetSortOrder("Price"));
    document.getElementById("print_docs").innerHTML = "";
    for (id in jsonArray) {
        document.getElementById("print_docs").innerHTML += createTable(jsonArray[id]);
    }
}

function car_distance(response) {
    var jsonArray = JSON.parse(response);

    const data = null;
    const xhr = new XMLHttpRequest();
    xhr.withCredentials = true;

    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === this.DONE) {
            var responseData = JSON.parse(xhr.responseText);
            let array = [];
            var count = Object.keys(jsonArray).length;
            for (var i = 0; i < count; i++) {
                array.push([jsonArray[i]["First Name"], jsonArray[i]["Last Name"], responseData["durations"][0][i]]);
            }
            array.sort(function (a, b) {
                return a[2] - b[2];
            });

            for (var i = 0; i < count; i++) {
                var time = secsToMins(array[i][2]);
                array[i][2] = time;
            }
            document.getElementById("print_docs").innerHTML = createArrayTable(array, count);
        }
    });

    var count = Object.keys(jsonArray).length;
    var lat = document.getElementById("lat").value;
    var lon = document.getElementById("lon").value;

    var URL = "https://trueway-matrix.p.rapidapi.com/CalculateDrivingMatrix?origins=" + lat + "%2C" + lon + "&destinations=";
    for (var i = 0; i < count; i++) {
        var lat_doc = jsonArray[i].lat;
        var lon_doc = jsonArray[i].lon;

        if (i === count - 1) {
            URL += lat_doc + "%2C" + lon_doc;
        } else {
            URL += lat_doc + "%2C" + lon_doc + "%3B";
        }
    }
    xhr.open("GET", URL);

    xhr.setRequestHeader("x-rapidapi-host", "trueway-matrix.p.rapidapi.com");
    xhr.setRequestHeader("x-rapidapi-key", "2b32ebee9emsh8012dc2806237ecp1e91f7jsn49cbb5a1c2b6");

    xhr.send(data);
}

function secsToMins(value) {
    return Math.floor(value / 60) + ":" + (value % 60 ? value % 60 : '00')
}

function createArrayTable(array, count) {
    var html = "<table style='border:2px solid white; background-color: rgb(51, 83, 109);'><tr><th>First Name</th><th>Last Name</th><th>Minutes to get there</th></tr>";
    for (var i = 0; i < count; i++) {
        console.log("e");
        html += "<tr><td>" + array[i][0] + "</td><td>" + array[i][1] + "</td><td>" + array[i][2] + "</td></tr>";
    }
    html += "</table><br>";
    return html;
}

function createTable(data) {
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