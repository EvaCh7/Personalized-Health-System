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



function create_table_from_json_array(json, table_id)
{
    var table = document.getElementById(table_id);
    var total = json.length;
    var cell = new Array();
    //add header
    var header = table.createTHead();
    var row = header.insertRow(0);
    var count = 0
    for (var js in json[0]) {
        var cell = row.insertCell(count++);
        cell.innerHTML = js
    }
    for (var i = 0; i < total; i++) {
        var row = table.insertRow(i + 1);
        for (var j = 0; j < Object.keys(json[0]).length; j++) {
            cell[j] = row.insertCell(j);

        }
        var index = 0

        for (var js in json[i]) {
            cell[index++].innerHTML = json[i][js]
        }

    }
}
var show_done = true
function show_done_randevouz() {
    if (show_done) {
        show_done = false;
        var url = "examinations/randevouz/getDoneRandevouz/" + doctorData.doctor_id
        sendXmlGetRequest(url, call_back_show_done_rand, call_back_error_show_done_rand)
    } else {
        $("#done-rand-table tr").remove();

        show_done = true;

    }
}
function call_back_show_done_rand(response) {
    json = JSON.parse(response)
    console.log(json)
    $("#done-rand-table").removeClass("d-none")
    create_table_from_json_array(json, "done-rand-table")
}
function call_back_error_show_done_rand(response) {
    json = JSON.parse(response)
    console.log(json.response)
}
var compare_bit = true
function compare() {
    if (compare_bit) {
        var user_id = $("#compare-user-id").val()
        var doctor_id = doctorData.doctor_id
        compare_exams(doctor_id, user_id)
        compare_bit = false
    } else
    {
        document.getElementById("content").innerHTML = ""
        compare_bit = true

    }


}
function compare_exams(doctor_id, user_id)
{
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const obj = JSON.parse(xhr.responseText);
            document.getElementById("content").innerHTML += createCompareTable(obj);

        } else if (xhr.status !== 200) {
            document.getElementById('content').innerHTML = 'Request failed. Returned status of ' + xhr.status + "<br>"
                    + JSON.stringify(xhr.responseText);
        }
    };
    var URL = "http://localhost:8080/PersonalizedHealth/examinations/compareUsersDoneExams/" + doctor_id + "/" + user_id;

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
var draw_chart_bit = true
function click_draw_chart() {
    if (draw_chart_bit) {

        var user_id = $("#compare-user-id").val()
        var doctor_id = doctorData.doctor_id
        drawChartFunc(doctor_id, user_id)
        draw_chart_bit = false

    } else {
        $("#chart_3d").html("");
        draw_chart_bit = true
    }

}
function drawChartFunc(doctor_id, user_id) {
    var URL = "http://localhost:8080/PersonalizedHealth/examinations/compareUsersDoneExams/" + doctor_id + "/" + user_id;
    sendXmlGetRequest(URL, draw_chart, call_back_error);
}

function draw_chart(response) {
    var json = JSON.parse(response);

    google.charts.load("current", {packages: ["corechart"]});
    google.charts.setOnLoadCallback(drawChart);

    var meas = $("#measurement").val();
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
        $("#chart_3d").html("");
        var chart = new google.visualization.BarChart(document.getElementById('chart_3d'));
        chart.draw(chartdata, options);
    }
}

function call_back_error(response)
{
    console.log(response)
}


let show_therapies_bit = true
function show_therapies() {
    if (show_therapies_bit) {

        const xhr = new XMLHttpRequest();
        xhr.onload = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                const obj = JSON.parse(xhr.responseText);
                var i = 1;
                var count = Object.keys(obj).length;
                for (id in obj) {
                    document.getElementById("therapy_div").innerHTML += createTreatmentTable(obj[id], i);
                    i++;
                }
                show_therapies_bit = false


            } else if (xhr.status !== 200) {
                document.getElementById('therapy_div')
                        .innerHTML = 'Request failed. Returned status of ' + xhr.status + "<br>"
                        + JSON.stringify(xhr.responseText);
                show_therapies_bit = false

            }
        };

        var user_id = $("#compare-user-id").val()

        var URL = "http://localhost:8080/PersonalizedHealth/examinations/Treatments/showTreatmentsDone/" + doctorData.doctor_id + "/" + user_id;

        xhr.open("GET", URL);
        xhr.setRequestHeader("Accept", "application/json");
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.send();
    } else {
        show_therapies_bit = true
        $("#therapy_div").html("")

    }

}
function send_and_store_treatment() {
    var data = get_form_data_to_json("add_treat")
    data["doctor_id"] = doctorData.doctor_id
    url = "http://localhost:8080/PersonalizedHealth/examinations/Treatments/addTreatment"
    sendXmlPostRequest(url, data, call_back_send_treatment, call_back_error_send_treatment)
}
function call_back_send_treatment(response) {
    response = JSON.parse(response)
    $("#add-treatment-response").html(response.response)
}
function call_back_error_send_treatment(response) {
    response = JSON.parse(response)

    $("#add-treatment-response").html(response.response)
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
var bit_add_tret = true
function add_treatment() {
    if (bit_add_tret) {
        $("#add_treat").removeClass("d-none")
        bit_add_tret = false
    } else {
        $("#add_treat").addClass("d-none")

        bit_add_tret = true
    }

}