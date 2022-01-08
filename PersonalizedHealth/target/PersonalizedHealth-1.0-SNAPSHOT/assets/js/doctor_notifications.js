/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
setInterval(function () {
    isLoggedIn_notfications()

}, 5000);

$(document).ready(function () {
    $('#notifcations').popover({
        container: 'body',
        content: ' ',
        html: true
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
    update_notification_text()
    $('#notifcations').attr('data-bs-content', notif_str);
    update_notification_text()
}
function error_show_notifications_about_cacnceled_randevouz() {

}
function handle_doctor_notifications(json) {
    var url = "examinations/randevouz/getRandevouz/" + json.id;

    sendXmlGetRequest(url, show_notifications_about_cacnceled_randevouz, error_show_notifications_about_cacnceled_randevouz)

}
function handle_user_notifications(json) {
    var url = "examinations/randevouz/getBloodDonationRandevouz/" + json.id;

    sendXmlGetRequest(url, show_notifications_about_blood_donation_randevouz, error_show_notifications_about_blood_donation_randevouz)
  
    
    //edw stelneis request gia to an exei 4 wres gia to rantebou 

}
function show_notifications_about_blood_donation_randevouz(response) {

    var js = JSON.parse(response);
    console.log(js)
    var notif_str = ""
    for (index in js) {

        var msg = " dear blood donator doctor says: " + js[index].message
        notif_str += msg

    }
    update_notification_text()
    $('#notifcations').attr('data-bs-content', notif_str);
    update_notification_text()
}
function error_show_notifications_about_blood_donation_randevouz() {

}






function update_notification_text() {
    if (isNotificationClicked) {

        $('#notifcations').popover("show");


    } else {
        $('#notifcations').popover("hide");

    }
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
            $('#notifcations').attr('data-bs-content', "you must be logged in to see notifcations");
            update_notification_text()

        }
    };
    xhr.open('GET', 'login');
    xhr.send();
}


let isNotificationClicked = false;
function button_clicked() {
    if (isNotificationClicked)
    {
        isNotificationClicked = false

    } else {
        isNotificationClicked = true

    }
}