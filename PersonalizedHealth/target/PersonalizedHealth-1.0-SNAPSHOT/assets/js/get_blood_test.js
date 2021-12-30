/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function createTableFromJSON(data, i) {
    var html = "<h4>BloodTest " + i + "</h4><table style='border:2px solid white; background-color: rgb(51, 83, 109);'><tr><th>Category</th><th>Value</th></tr>";
    for (const x in data) {
        var category = x;
        var value = data[x];
        html += "<tr><td>" + category + "</td><td>" + value + "</td></tr>";
    }
    html += "</table><br>";
    return html;
}

function createTreatmentTable(data, i) {
    var html = "<table style='border:2px solid white; background-color: rgb(51, 83, 109);'><tr><th>Category</th><th>Value</th></tr>";
    for (const x in data) {
        var category = x;
        var value = data[x];
        html += "<tr><td>" + category + "</td><td>" + value + "</td></tr>";
    }
    html += "</table><br>";
    return html;
}

function get_blood_tests()
{
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const obj = JSON.parse(xhr.responseText);
            var i = 1;
            var count = Object.keys(obj).length;
            document.getElementById("msg").innerHTML = "<h3>" + count + " Exams</h3>";
            for (id in obj) {
                document.getElementById("msg").innerHTML = createTableFromJSON(obj[id], i);
                i++;

            }

        } else if (xhr.status !== 200) {
            document.getElementById('msg')
                    .innerHTML = 'Request failed. Returned status of ' + xhr.status + "<br>"
                    + JSON.stringify(xhr.responseText);

        }
    };

    var amka = document.getElementById("amka_rest").value;
    var URL = "http://localhost:8080/PersonalizedHealth/examinations/bloodTests/" + amka;
    var fD = document.getElementById("fromDate").value;
    var tD = document.getElementById("toDate").value;

    if (fD !== "" && tD === "") {
        URL += "?fromDate=" + fD;
    } else if (fD === "" && tD !== "") {
        URL += "?toDate=" + tD;
    } else if (fD !== "" && tD !== "") {
        URL += "?fromDate=" + fD + "&toDate=" + tD;
    }
    xhr.open("GET", URL);
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}


function compare_exams()
{
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const obj = JSON.parse(xhr.responseText);
            document.getElementById("content").innerHTML = createCompareTable(obj);

        } else if (xhr.status !== 200) {
            document.getElementById('content').innerHTML = 'Request failed. Returned status of ' + xhr.status + "<br>"
                    + JSON.stringify(xhr.responseText);
        }
    };
    var amka = document.getElementById("amka").value;
    var URL = "http://localhost:8080/PersonalizedHealth/examinations/compareExams/" + amka;

    xhr.open("GET", URL);
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}

function 
createCompareTable(data) {
    var html = "<br><table style='border:2px solid white; background-color: rgb(51, 83, 109);'><tr><td>Date</td>";

    for (var i = 0; i < Object.keys(data).length; i++) {
        html += "<td>" + data[i].test_date + "</td>";
    }
    html += "</tr><tr><td>Blood Sugar</td>";
    for (var i = 0; i < Object.keys(data).length; i++) {
        html += "<td>" + data[i].blood_sugar + "</td>";
    }
    html += "</tr><tr><td>Blood Sugar Level</td>";
    for (var i = 0; i < Object.keys(data).length; i++) {
        html += "<td>" + data[i].blood_sugar_level + "</td>";
    }
    html += "</tr><tr><td>Cholesterol</td>";
    for (var i = 0; i < Object.keys(data).length; i++) {
        html += "<td>" + data[i].cholesterol + "</td>";
    }
    html += "</tr><tr><td>Cholesterol Level</td>";
    for (var i = 0; i < Object.keys(data).length; i++) {
        html += "<td>" + data[i].cholesterol_level + "</td>";
    }
    html += "</tr><tr><td>Iron</td>";
    for (var i = 0; i < Object.keys(data).length; i++) {
        html += "<td>" + data[i].iron + "</td>";
    }
    html += "</tr><tr><td>Iron Level</td>";
    for (var i = 0; i < Object.keys(data).length; i++) {
        html += "<td>" + data[i].iron_level + "</td>";
    }
    html += "</tr><tr><td>Vitamin D3</td>";
    for (var i = 0; i < Object.keys(data).length; i++) {
        html += "<td>" + data[i].vitamin_d3 + "</td>";
    }
    html += "</tr><tr><td>Vitamin D3 Level</td>";
    for (var i = 0; i < Object.keys(data).length; i++) {
        html += "<td>" + data[i].vitamin_d3_level + "</td>";
    }
    html += "</tr><tr><td>Vitamin B12</td>";
    for (var i = 0; i < Object.keys(data).length; i++) {
        html += "<td>" + data[i].vitamin_b12 + "</td>";
    }
    html += "</tr><tr><td>Vitamin B12 Level</td>";
    for (var i = 0; i < Object.keys(data).length; i++) {
        html += "<td>" + data[i].vitamin_b12_level + "</td>";
    }
    html += "</tr></table><br>";
    return html;
}

function drawChartFunc() {
    var amka = document.getElementById("amka").value;
    var URL = "http://localhost:8080/PersonalizedHealth/examinations/compareExams/" + amka;
    sendXmlGetRequest(URL, draw_chart, call_back_error);
}

function draw_chart(response) {
    var json = JSON.parse(response);

    google.charts.load("current", {packages: ["corechart"]});
    google.charts.setOnLoadCallback(drawChart);

    var meas = document.userForm.measurement.value;
    function drawChart() {
        var data = [];
        var Header = ['Date', 'Measure'];
        data.push(Header);
        for (var i = 0; i < Object.keys(json).length; i++)
        {
            var temp = [];
            temp.push(json[i].test_date);
            temp.push(json[i][meas]);
            data.push(temp);
        }
        var chartdata = new google.visualization.arrayToDataTable(data);
        var options = {
            title: 'Exams',
            is3D: true
        };

        var chart = new google.visualization.BarChart(document.getElementById('chart_3d'));
        chart.draw(chartdata, options);
    }
}

function call_back_error()
{
    var text = $("#error").html()
    if (!text.includes(" register error"))
        $("#error").html(text + " register error");
}

function show_therapies() {
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const obj = JSON.parse(xhr.responseText);
            var i = 1;
            var count = Object.keys(obj).length;
            for (id in obj) {
                document.getElementById("therapy_div").innerHTML = createTreatmentTable(obj[id], i);
                i++;
            }

        } else if (xhr.status !== 200) {
            document.getElementById('therapy_div')
                    .innerHTML = 'Request failed. Returned status of ' + xhr.status + "<br>"
                    + JSON.stringify(xhr.responseText);

        }
    };

    var amka = document.getElementById("amka").value;
    var URL = "http://localhost:8080/PersonalizedHealth/examinations/Treatments/showTreatments/" + amka;

    xhr.open("GET", URL);
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}