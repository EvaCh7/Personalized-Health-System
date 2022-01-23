/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function rate_randevouz() {
    var username = document.getElementById("username").value;
    document.getElementById("rating_div").style.display = "block";
    document.getElementById("close_div_but_6").style.display = "block";

    var URL = "http://localhost:8080/PersonalizedHealth/examinations/randevouz/showAllRandevouz/" + username;
    sendXmlGetRequest(URL, print_done_rand, error);
}

function print_done_rand(response) {
    var jsonArray = JSON.parse(response);

    document.getElementById("show_rating").style.display = "block";
    document.getElementById("show_done_Randevouz").innerHTML = "";

    for (id in jsonArray) {
        if (jsonArray[id]["status"] === "done" && jsonArray[id]["rating"] === 0) {
            document.getElementById("show_done_Randevouz").innerHTML += createTable(jsonArray[id]);
        } else
            continue;
    }
    if (document.getElementById("show_done_Randevouz").innerHTML === "") {
        document.getElementById("show_done_Randevouz").innerHTML = "You haven't attended any randevouz.";
        document.getElementById("show_rating").style.display = "none";
    }
}

function submit_rate() {
    var stars;
    if (document.getElementById('rating1').checked)
        stars = 1;
    else if (document.getElementById('rating2').checked)
        stars = 2;
    else if (document.getElementById('rating3').checked)
        stars = 3;
    else if (document.getElementById('rating4').checked)
        stars = 4;
    else if (document.getElementById('rating5').checked)
        stars = 5;

    var rand_id = document.getElementById("rate_rand_id").value;
    var URL = "http://localhost:8080/PersonalizedHealth/examinations/randevouz/rateRandevouz/" + rand_id + "/" + stars;
    sendXmlPutRequest(URL, give_rating_to_randevouz, error);
}

function give_rating_to_randevouz(responseData) {
    var response = JSON.parse(responseData);

    var URL = "http://localhost:8080/PersonalizedHealth/examinations/docs/submitDocRate/" + response["doctor_id"];
    sendXmlPutRequest(URL, calc_doctors_rate, error);
}

function calc_doctors_rate(responseData) {
    var response = JSON.parse(responseData);

    if (response["success"] === "Successful submition of rating.")
        document.getElementById("responseMessRand").innerHTML = "Your review was submitted successfully!";
    else
        document.getElementById("responseMessRand").innerHTML = "An error occured and your randevouz couldn't be rated. Please try again.";

}

function close_div_6() {
    document.getElementById("rating_div").innerHTML = "";
    document.getElementById("rating_div").style.display = "none";
    
    document.getElementById("close_div_but_6").style.display = "none";
}

function error() {}
