function certifyDocFunc() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById('msg').innerHTML = JSON.stringify(xhr.responseText);

        } else if (xhr.status !== 200) {
            document.getElementById('msg')
                    .innerHTML = 'Request failed. Returned status of ' + xhr.status + "<br>" +
                    JSON.stringify(xhr.responseText);
        }
    };
    var id = document.getElementById("certThisDoc").value;

    xhr.open('PUT', 'http://localhost:8080/PersonalizedHealth/examinations/Certify/' + id);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}

function deleteUserFunc() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById('msg').innerHTML = JSON.stringify(xhr.responseText);
        } else if (xhr.status !== 200) {
            document.getElementById('msg')
                    .innerHTML = 'Request failed. Returned status of ' + xhr.status + "<br>" +
                    JSON.stringify(xhr.responseText);
        }
    };
    var username = document.getElementById("deleteThisUser").value;
    var isdoc = document.adminForm.isADoctor.value;
    xhr.open('DELETE', 'http://localhost:8080/PersonalizedHealth/examinations/UserDeletion/' + username + '/' + isdoc);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}

function get_uncertified_doctors()
{
    const data = null;

    const xhr = new XMLHttpRequest();
    xhr.withCredentials = true;

    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === this.DONE) {
            document.getElementById("uncert_docs_but").disabled = true;
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
        document.getElementById("all_users_but").disabled = true;
        var json = JSON.parse(this.responseText);
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
        for (var j = 0; j < 5; j++) {
            cell[j] = row.insertCell(j);
        }
        cell[0].innerHTML = json[i].username;
        cell[1].innerHTML = json[i].firstname;
        cell[2].innerHTML = json[i].lastname;
        cell[3].innerHTML = json[i].birthdate;
        if (json[i].specialty === undefined) {
            cell[4].innerHTML = "Simple User";
        } else {
            cell[4].innerHTML = "Doctor";
        }
    }
}