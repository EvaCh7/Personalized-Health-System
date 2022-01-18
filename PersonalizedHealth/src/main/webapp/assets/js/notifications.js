/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


setInterval(function () {
    isLoggedIn_notfications()


}, 3000);

$(document).ready(function () {

   

 $('#notifcations').popover({
        animation: true,
        html: true,
        placement: 'bottom',

    })


});

function show_notifications_about_cacnceled_randevouz(response) {
    var js = JSON.parse(response)
    var notif_str = ""
    for (index in js) {

        if (js[index].status == "cancelled") {
            var msg = "Randevouz with patient: " + js[index].user_id + " got cancelled for date: " + js[index].date_time + "<br>"
            notif_str += msg

        }
    }

    if ($('#notifcations').attr('data-bs-content') !== notif_str) {
        $('#notifcations').attr('data-bs-content', "");
        $('#notifcations').attr('data-bs-content', notif_str);
    }
}

function error_show_notifications_about_cacnceled_randevouz() {

}
function handle_doctor_notifications(json) {
    var url = "examinations/randevouz/getRandevouz/" + json.id;

    sendXmlGetRequest(url, show_notifications_about_cacnceled_randevouz, error_show_notifications_about_cacnceled_randevouz)

}
function handle_user_notifications(json) {
    var URL = "examinations/randevouz/getRandevouzNotification/" + json.id;

    sendXmlGetRequest(URL, show_randevouz_notification, error_show_randevouz_notification);

    var url = "examinations/randevouz/getBloodDonationRandevouz/" + json.id;

    sendXmlGetRequest(url, show_notifications_about_blood_donation_randevouz, error_show_notifications_about_blood_donation_randevouz)
}


function show_notifications_about_blood_donation_randevouz(response) {

    var js = JSON.parse(response);
    var notif_str = ""
    for (index in js) {

        var msg = " dear blood donator doctor says: " + js[index].message
        notif_str += msg

    }
    if ($('#notifcations').attr('data-bs-content') !== notif_str) {
        $('#notifcations').attr('data-bs-content', "");
        $('#notifcations').attr('data-bs-content', notif_str);
    }
}



function show_notifications_about_cacnceled_randevouz(response) {
    var js = JSON.parse(response)
    var notif_str = ""
    for (index in js) {

        if (js[index].status == "cancelled") {
            var msg = "Randevouz with patient: " + js[index].user_id + " got cancelled for date: " + js[index].date_time + "<br>"
            notif_str += msg

        }
    }
    if ($('#notifcations').attr('data-bs-content') !== notif_str) {
        console.log("show doctor notif")
        $('#notifcations').attr('data-bs-content', "");
        $('#notifcations').attr('data-bs-content', notif_str);
    }
}

function error_show_randevouz_notification() {

}

function show_randevouz_notification(response) {
    var js = JSON.parse(response);
    var notif_str = "";
    for (index in js) {
        var msg = "Attention! Don't forget that your randevouz is at: " + js[index].date_time;
        notif_str += msg;
    }
    $('#notifcations').attr('data-bs-content', notif_str);

    if ($('#notifcations').attr('data-bs-content') !== notif_str) {
        $('#notifcations').attr('data-bs-content', "");
        $('#notifcations').attr('data-bs-content', notif_str);
    }
}

function update_notification_text() {

}
function isLoggedIn_notfications() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            json = JSON.parse(xhr.responseText)
            if (json.type === "doctor") {
                handle_doctor_notifications(json)

            } else if (json.type === "user") {
                handle_user_notifications(json)

            }


        } else if (xhr.status !== 200) {
            if ($('#notifcations').attr('data-bs-content') !== "you must be logged in to see notifcations") {
                $('#notifcations').attr('data-bs-content', "");
                $('#notifcations').attr('data-bs-content', "you must be logged in to see notifcations");

            }

        }
    };
    xhr.open('GET', 'login');
    xhr.send();
}

