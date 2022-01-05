function make_appointment() {
    var id = document.getElementById("getDoctorsID").value;

    var URL = "http://localhost:8080/PersonalizedHealth/examinations/randevouz/getRandevouz/" + id;
    sendXmlGetRequest(URL, print_free_randevouz, error);
}

function print_free_randevouz(response) {
    var jsonArray = JSON.parse(response);
    document.getElementById("print_avail_rand").innerHTML = "";
    console.log(jsonArray[0]["status"]);
    for (id in jsonArray) {
        if (jsonArray[0]["status"] === "free")
            document.getElementById("print_avail_rand").innerHTML += createTable(jsonArray[id]);
    }
    if (document.getElementById("print_avail_rand").innerHTML === "") {
        document.getElementById("print_avail_rand").innerHTML = "There are no available randevouz.";
    } else {
        document.getElementById("select_randevouz").style.display = "block";
    }

}

function reserve_appointment() {
    var id = document.getElementById("getRandevouzID").value;
    var URL = "http://localhost:8080/PersonalizedHealth/examinations/randevouz/reserveRandevouz/" + id;
    sendXmlPutRequest(URL, reservation, error);
}

function reservation(responseData) {
    var response = JSON.parse(responseData);

    if (response["response"] === "Randevouz was reserved succesfully.")
        document.getElementById("responseMess").innerHTML = "Your randevouz has been successfully reserved!";
    else
        document.getElementById("responseMess").innerHTML = "An error occured and your randevouz couldn't be reserved. Please try again.";
}

function show_all_randevouz() {
    var username = document.getElementById("username").value;
    var URL = "http://localhost:8080/PersonalizedHealth/examinations/randevouz/showAllRandevouz/" + username;
    sendXmlGetRequest(URL, print_all_randevouz, error);
}

function print_all_randevouz(response) {
    var jsonArray = JSON.parse(response);

    document.getElementById("show_Randevouz").innerHTML = "";
    for (id in jsonArray) {
        document.getElementById("show_Randevouz").innerHTML += createTable(jsonArray[id]);
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
