/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function spawn_answers(messages_array) {
    var str = ""
    var count_user_msgs = 0
    for (var i = 0; i < messages_array.length; ++i) {
        if (messages_array[i].sender === "user") {
            ++count_user_msgs;
        }
    }

    for (var i = 0; i < messages_array.length; ++i) {
        if (messages_array[i].sender === "doctor") {
            str += ` <input type="text"  class="form-control" name="stored_msg" placeholder="Answer" value="` + messages_array[i].message + `"
                         disabled      >`
            --count_user_msgs;

        }

    }
    for (i = 0; i < count_user_msgs; ++i) {
        str += ` <input type="text"  class="form-control" name="message" placeholder="Answer"
                               >`
    }
    return str

}
function spawn_patient_messages(messages_array) {
    var str = ""

    for (var msg in messages_array) {
        if (messages_array[msg].sender === "user") {

            str += `<input type="text"  class="form-control" name="patient_message" placeholder="patient message" value="` + messages_array[msg].message + `"
                               disabled>`
        }

    }
    return str;
}




function store_messages() {
    console.log("before before", data)

    var data = get_form_data_to_json_array("doctor-answers")
    console.log("before", data)
    for (var index in data) {

        if (!data[index].message) {
            delete data[index]
            data.length--;
        }
    }

    if (data.length === 0) {
        data = null;
    }
    //var data = $('form').serializeArray("doctor-answers")

    for (var index in data) {

        if (data[index]) {

            data[index]["doctor_id"] = doctorData.doctor_id
            data[index]["user_id"] = $("#user_id_msg_but").val()
            data[index]["sender"] = "doctor"
            data[index]["blood_donation"] = 0
            data[index]["bloodtype"] = doctorData.bloodtype
            data[index]["date_time"] = get_date_time();
        }


    }
    var url = "examinations/Messages/AddDoctorAnswers"

    console.log(data)
    sendXmlPostRequest(url, data, call_back_store_msgs, call_back_error_store_msgs)



}
function call_back_store_msgs(response) {
    var json = JSON.parse(response)
    show_element("store-response")

    console.log(json)
    $("#store-response").html(json.response)
    if ($("#user_id_msg_but").val()) {
        spawn_messages_on_change()
    }
}
function call_back_error_store_msgs(response) {
    show_element("store-response")

    var json = JSON.parse(response)
    console.log(json)
    $("#store-response").html(json.response)
    $("#store-response").html(json.response)
    if ($("#user_id_msg_but").val()) {
        spawn_messages_on_change()
    }
}
function spawn_messages_on_change() {

    var user_id = $("#user_id_msg_but").val()
    var url = "examinations/Messages/getDoctorMessages/" + doctorData.doctor_id + "/" + user_id
    sendXmlGetRequest(url, call_back_spawn_messages, call_back_error_spawn_messages)

    $("#store-messages").removeClass("d-none")

}
function call_back_spawn_messages(response) {
    var json = JSON.parse(response)
    $("#patient-messages").html(spawn_patient_messages(json))
    $("#doctor-answers").html(spawn_answers(json))
}
function hide_element(id) {
    id = "#" + id
    console.log(id)
    $(id).addClass("d-none")
}
function show_element(id) {
    id = "#" + id
    console.log(id)

    $(id).removeClass("d-none")

}
function call_back_error_spawn_messages(response) {
    var json = JSON.parse(response)
    $("#patient-messages").html(json.response)
    $("#doctor-answers").html(json.response)
    console.log(json.response)
}

var bit_user_select = true
function show_user_id_select() {

    if (bit_user_select) {
        $("#user-id-messages").removeClass("d-none")
        bit_user_select = false
        if ($("#user_id_msg_but").val()) {
            spawn_messages_on_change()
        }
        show_element("patient-msg-label")
        show_element("doctor-ans-label")


    } else {
        $("#user-id-messages").addClass("d-none")
        bit_user_select = true
        $("#patient-messages").html("")
        $("#doctor-answers").html("")
        $("#store-messages").addClass("d-none")

        hide_element("patient-msg-label")
        hide_element("doctor-ans-label")
        hide_element("store-response")

    }
}