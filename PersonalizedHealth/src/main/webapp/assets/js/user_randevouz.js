function close_div_3() {
    $("#print_avail_rand").addClass("d-none");
    $("#responseMess").addClass("d-none");
    $("#select_randevouz").addClass("d-none");

    document.getElementById("close_div_but_3").style.display = "none";
    document.getElementById("getDoctorsID").value = "";
}

function make_appointment() {
    var id = document.getElementById("getDoctorsID").value;
    document.getElementById("close_div_but_3").style.display = "block";

    var URL = "http://localhost:8080/PersonalizedHealth/examinations/randevouz/getRandevouz/" + id;
    sendXmlGetRequest(URL, print_free_randevouz, error);
}

function print_free_randevouz(response) {
    $("#print_avail_rand").removeClass("d-none");
    $("#responseMess").removeClass("d-none");
    $("#select_randevouz").removeClass("d-none");

    var jsonArray = JSON.parse(response);
    document.getElementById("print_avail_rand").innerHTML = "";
    for (id in jsonArray) {
        console.log(jsonArray[id]);

        if (jsonArray[id]["status"] === "free")
            document.getElementById("print_avail_rand").innerHTML += createTable(jsonArray[id]);
    }
    if (document.getElementById("print_avail_rand").innerHTML === "") {
        document.getElementById("print_avail_rand").innerHTML = "There are no available randevouz.";
    } else {
        document.getElementById("select_randevouz").style.display = "block";
    }

}

function reserve_appointment() {
    var user_info = document.getElementById("user_text_randevouz").value;
    console.log(user_info);
    var rand_id = document.getElementById("getRandevouzID").value;
    var data = window.localStorage.getItem('userData');
    data = JSON.parse(data);

    var URL = "http://localhost:8080/PersonalizedHealth/examinations/randevouz/reserveRandevouz/" + rand_id + "/" + data["user_id"] + "?user_info=" + user_info;
    sendXmlPutRequest(URL, reservation, error);
}

function reservation(responseData) {
    var response = JSON.parse(responseData);

    if (response["response"] === "Randevouz was reserved succesfully.")
        document.getElementById("responseMess").innerHTML = "Your randevouz has been successfully reserved!";
    else
        document.getElementById("responseMess").innerHTML = "An error occured and your randevouz couldn't be reserved. Please try again.";
}

function close_div_4() {
    $("#show_Randevouz").addClass("d-none");
    $("#responseMessCancel").addClass("d-none");
    $("#cancel_randevouz").addClass("d-none");

    document.getElementById("close_div_but_4").style.display = "none";
}

function show_all_randevouz() {
    var username = document.getElementById("username").value;
    document.getElementById("close_div_but_4").style.display = "block";

    var URL = "http://localhost:8080/PersonalizedHealth/examinations/randevouz/showAllRandevouz/" + username;
    sendXmlGetRequest(URL, print_all_randevouz, error);
}

function print_all_randevouz(response) {
    $("#show_Randevouz").removeClass("d-none");
    $("#responseMessCancel").removeClass("d-none");
    $("#cancel_randevouz").removeClass("d-none");

    var jsonArray = JSON.parse(response);

    document.getElementById("show_Randevouz").innerHTML = "";
    for (id in jsonArray) {
        if (jsonArray[id]["status"] === "selected") {
            document.getElementById("show_Randevouz").innerHTML += createTable(jsonArray[id]);
        } else
            continue;
    }
    if (document.getElementById("show_Randevouz").innerHTML === "") {
        document.getElementById("show_Randevouz").innerHTML = "There are no available randevouz.";
    } else {
        document.getElementById("cancel_randevouz").style.display = "block";
    }
}

function cancel_appointment() {
    var id = document.getElementById("getRandevouzID_cancel").value;
    var URL = "http://localhost:8080/PersonalizedHealth/examinations/randevouz/cancelRandevouz/" + id;
    sendXmlDeleteRequest(URL, cancel_rand, error);
}

function cancel_rand(response) {
    var response = JSON.parse(response);

    if (response["response"] === "Randevouz cancelled successfully")
        document.getElementById("responseMessCancel").innerHTML = "Your randevouz has been successfully canceled.";
    else
        document.getElementById("responseMessCancel").innerHTML = "An error occured and your randevouz couldn't be canceled. Please try again.";

}

function error() {}

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
