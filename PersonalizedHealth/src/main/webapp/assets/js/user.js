
function update_data(e) {
    var gender
    if ($('#man').is(':checked')) {
        gender = "male"
    } else if ($('#woman').is(':checked')) {
        gender = "female"
    } else {
        gender = "other"
    }
    var blood_donor
    if ($('#blood-giver').is(':checked')) {
        blood_donor = 1
    } else {
        blood_donor = 0
    }


    var data = {
        username: $("#username").val(),
        email: $("#email").val(),
        password: $("#pswd").val(),
        firstname: $("#firstname").val(),
        lastname: $("#surname").val(),
        birthdate: $("#birth-date").val(),
        gender: gender,
        amka: $("#amka").val(),
        country: $("#country").val(),
        city: $("#city").val(),
        address: $("#address").val(),
        lat: lat,
        lon: lon,
        telephone: $("#telephone").val(),
        height: $("#height").val(),
        weight: $("#weight").val(),
        blooddonor: blood_donor,
        bloodtype: $("#blood-type").val(),

    };
    e.preventDefault();

    console.log(data)
    sendXmlPostRequest("/PersonalizedHealth/user", data, call_back_update_data, call_back_error_update_data);
}
function call_back_update_data(response)
{
    get_data()
    $("#after_register").html("data updated succesfully")
    console.log(response)
}
function call_back_error_update_data(response)
{
    $("#error").html("error occured " + response)
}
function fill_user_info(responseData) {
    console.log(responseData)

    $("#username").val(responseData.username)
    $("#email").val(responseData.email)
    $("#pswd").val(responseData.password)
    $("#firstname").val(responseData.firstname)
    $("#surname").val(responseData.lastname)
    $("#birth-date").val(responseData.birthdate)
    if (responseData.gender === "Male")
        $("#man").prop("checked", true);
    else
        $("#woman").prop("checked", true);

    if (responseData.blooddonor === "1")
        $("#blood-giver").prop("checked", true);
    else
        $("#no").prop("checked", true);

    $("#birth-date").val(responseData.birthdate)
    $("#amka").val(responseData.amka)
    $("#country").val(responseData.country)
    $("#city").val(responseData.city)
    $("#address").val(responseData.address)
    $("#telephone").val(responseData.telephone)
    $("#height").val(responseData.height)
    $("#weight").val(responseData.weight)
    $("#blood-type").val(responseData.bloodtype)

//    var select_blood = '#blood-type option[value=\"' + responseData.bloodtype + '\"]'
//    console.log(select_blood)
//    $(select_blood).attr('selected', 'selected');
}
function get_data() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const responseData = JSON.parse(xhr.responseText);
            fill_user_info(responseData)
        } else if (xhr.status !== 200) {
        }
    };
    xhr.open('GET', 'user?type=user_info');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}
$(document).ready(function () {
    get_data();
    $("#bmi").click(calculate_and_display_bmi)
    $("#ideal-weight").click(calculate_and_display_ideal_weight)

    $('#update-user-form').on('submit', function (e)
    {
        update_data(e)
    })

});

function calculate_and_display_bmi()
{
    const data = null;

    const xhr = new XMLHttpRequest();
    xhr.withCredentials = true;

    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === this.DONE) {
            var json = JSON.parse(this.responseText)
            console.log(json)
            $("#bmi-text").html("you have " + json.data.health + " BMI!")
        }
    });
    var weight = $("#weight").val()
    var height = $("#height").val()

    xhr.open("GET", "https://fitness-calculator.p.rapidapi.com/bmi?age=25&weight=" + weight + "&height=" + height);
    xhr.setRequestHeader("x-rapidapi-host", "fitness-calculator.p.rapidapi.com");
    xhr.setRequestHeader("x-rapidapi-key", "59dd881c7cmsh891f1f8f669b670p125db3jsn78f61fef5840");

    xhr.send(data);
}

function calculate_and_display_ideal_weight() {
    const data = null;

    const xhr = new XMLHttpRequest();
    xhr.withCredentials = true;

    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === this.DONE) {

            var json = JSON.parse(this.responseText)
            console.log(json)

            $("#ideal-weight-text").html("ideal weight is " + json.data.Devine + " kg")
        }
    });
    var gender = get_gender();
    var height = $("#height").val()


    xhr.open("GET", "https://fitness-calculator.p.rapidapi.com/idealweight?gender=" + gender + "&height=" + height);
    xhr.setRequestHeader("x-rapidapi-host", "fitness-calculator.p.rapidapi.com");
    xhr.setRequestHeader("x-rapidapi-key", "59dd881c7cmsh891f1f8f669b670p125db3jsn78f61fef5840");

    xhr.send(data);
}

function get_certified_doctors()
{
    const data = null;

    const xhr = new XMLHttpRequest();
    xhr.withCredentials = true;

    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === this.DONE) {
            var json = JSON.parse(this.responseText)
            load_doc_data_to_table(json);
        }
    });
    xhr.open('GET', 'certified_doctors', true);
    xhr.setRequestHeader("Content-type", "application/json");
    console.log(data);

    xhr.send(data);
}


function load_doc_data_to_table(json)
{
    var table = document.getElementById('cert-doc-table');
    var total = json.length;
    var cell = new Array();
    for (var i = 0; i < total; i++) {
        row = table.insertRow(i + 1);
        for (var j = 0; j < 8; j++) {
            cell[j] = row.insertCell(j);
        }
        cell[0].innerHTML = json[i].firstname;
        cell[1].innerHTML = json[i].lastname;
        cell[2].innerHTML = json[i].email;
        cell[3].innerHTML = json[i].telephone;
        cell[4].innerHTML = json[i].address;
        cell[5].innerHTML = json[i].city;
        cell[6].innerHTML = json[i].doctor_info;
        cell[7].innerHTML = json[i].specialty;
    }
    document.getElementById("cert-docs").disabled = true;
}

function add_exams() {
    var result = $("#content").load("new_blood_test.html",
            function () {
                var amka = document.getElementById("amka").value;
                document.getElementById("amka_rest").value = amka;
            });
}


function all_exams() {
    var result = $("#content").load("get_blood_tests.html",
            function () {
                var amka = document.getElementById("amka").value;
                document.getElementById("amka_rest").value = amka;
            });
}
