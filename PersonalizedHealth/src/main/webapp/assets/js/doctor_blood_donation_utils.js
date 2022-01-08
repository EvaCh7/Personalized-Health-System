/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



function send_massive_msg() {
    var data = get_form_data_to_json("blood_donation_form")
    data["doctor_id"] = doctorData.doctor_id
    console.log(data["bloodtype"])

    data["bloodtype"] = data["bloodtype"].replace('P', '+')
    data["bloodtype"] = data["bloodtype"].replace('N', '-')

    console.log(data["bloodtype"])

    data["blood_donation"] = "1"


    var url = "examinations/Messages/sendMessageToBloodDonors"
    data["date_time"] = get_date_time();


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