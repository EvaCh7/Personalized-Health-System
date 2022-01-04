/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



function send_massive_msg() {
    var data = get_form_data_to_json("blood_donation_form")
    data["doctor_id"] = doctorData.doctor_id
    var url = "examinations/Messages/sendMessageToBloodDonors"
    data["date_time"] = get_date_time();
    console.log(typeof $("#blood_donation_form #blood-type").val())
    if ($("#blood_donation_form #blood-type").val() === "A+") {
        console.log("xoxox")
        data["bloodtype"] = "A+"

    }
    console.log(data)
    sendXmlPostRequest(url, data, call_back_send_massive_msg, call_back_error_send_massive_msg)
}

function call_back_send_massive_msg(response) {
    var json = JSON.parse(response)
    $("blod-don-msg-response").html(json.response)

}
function call_back_error_send_massive_msg(response) {
    var json = JSON.parse(response)
    $("blod-don-msg-response").html(json.response)

}