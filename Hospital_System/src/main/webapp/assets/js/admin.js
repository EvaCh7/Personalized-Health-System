function certifyDocFunc() {
    var id = document.getElementById("certThisDoc").value;
}

function get_uncertified_doctors()
{
    const data = null;

    const xhr = new XMLHttpRequest();
    xhr.withCredentials = true;

    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === this.DONE) {
            var json = JSON.parse(this.responseText)
            uncert_docs_table(json);
        }
    });
    document.getElementById("uncert_docs").style.display = "block";

    xhr.open('GET', 'uncertified_doctors', true);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}


function get_all_users() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        var json = JSON.parse(this.responseText);
        console.log(json);
        all_users_table(json);
    }
    document.getElementById("all_users").style.display = "block";

    xhr.open('GET', 'get_Users');
    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.send();
}

function uncert_docs_table(json)
{
    var table = document.getElementById('uncert_docs_table');
    var total = json.length;
    var cell = new Array();
    for (var i = 0; i < total; i++) {
        row = table.insertRow(i + 1);
        for (var j = 0; j < 6; j++) {
            cell[j] = row.insertCell(j);
        }
        cell[0].innerHTML = json[i].doctor_id;
        cell[1].innerHTML = json[i].firstname;
        cell[2].innerHTML = json[i].lastname;
        cell[3].innerHTML = json[i].address;
        cell[4].innerHTML = json[i].doctor_info;
        cell[5].innerHTML = json[i].specialty;
    }
}

function all_users_table(json)
{
    var table = document.getElementById('all_users_table');
    var total = json.length;
    var cell = new Array();
    for (var i = 0; i < total; i++) {
        row = table.insertRow(i + 1);
        for (var j = 0; j < 4; j++) {
            cell[j] = row.insertCell(j);
        }
        cell[0].innerHTML = json[i].username;
        cell[1].innerHTML = json[i].firstname;
        cell[2].innerHTML = json[i].lastname;
        cell[3].innerHTML = json[i].birthdate;
    }
}