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
                document.getElementById("msg").innerHTML += createTableFromJSON(obj[id], i);
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