/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function spawn_answers(messages_array) {
    var str = "";
    var count_doc_msgs = 0;
    for (var i = 0; i < messages_array.length; ++i) {
        if (messages_array[i].sender === "doctor") {
            ++count_doc_msgs;
        }
    }
    console.log(count_doc_msgs);
    for (var i = 0; i < messages_array.length; ++i) {
        if (messages_array[i].sender === "user") {
            console.log("here");
            str += ` <input type="text"  class="form-control" name="stored_msg" placeholder="Answer" value="` + messages_array[i].message + `"
                         disabled      >`;
            --count_doc_msgs;

        }

    }
    for (i = 0; i < count_doc_msgs; ++i) {
        console.log("e");
        str += ` <input type="text"  class="form-control" name="message" placeholder="Answer"
                               >`;
    }
    console.log(str);

    return str;

}
function spawn_doc_messages(messages_array) {
    var str = "";

    for (var msg in messages_array) {
        if (messages_array[msg].sender === "doctor") {

            str += `<input type="text"  class="form-control" name="doctor_message" placeholder="doctor message" value="` + messages_array[msg].message + `"
                               disabled>`;
        }

    }
    return str;
}




function store_messages() {
    console.log("before before", data);

    var data = get_form_data_to_json_array("user-answers");
    console.log("before", data);
    for (var index in data) {

        if (!data[index].message) {
            delete data[index];
            data.length--;
        }
    }

    if (data.length === 0) {
        data = null;
    }
    //var data = $('form').serializeArray("doctor-answers")

    for (var index in data) {

        if (data[index]) {

            data[index]["doctor_id"] = $("#doc_id_msg_but").val();
            data[index]["user_id"] = userData.user_id;
            data[index]["sender"] = "user";
            data[index]["blood_donation"] = 0;
            data[index]["bloodtype"] = userData.bloodtype;
            data[index]["date_time"] = get_date_time();
        }


    }
    var url = "examinations/Messages/AddUserAnswers";

    console.log(data);
    sendXmlPostRequest(url, data, call_back_store_msgs, call_back_error_store_msgs);



}
function call_back_store_msgs(response) {
    var json = JSON.parse(response);
    show_element("store-response");

    console.log(json);
    $("#store-response").html(json.response);
    if ($("#doc_id_msg_but").val()) {
        spawn_messages_on_change();
    }
}
function call_back_error_store_msgs(response) {
    show_element("store-response");

    var json = JSON.parse(response);
    console.log(json);
    $("#store-response").html(json.response);
    $("#store-response").html(json.response);
    if ($("#doc_id_msg_but").val()) {
        spawn_messages_on_change();
    }
}
function spawn_messages_on_change() {

    var doctor_id = $("#doc_id_msg_but").val();
    var url = "examinations/Messages/getUserMessages/" + userData.user_id + "/" + doctor_id;
    sendXmlGetRequest(url, call_back_spawn_messages, call_back_error_spawn_messages);

    $("#store-messages").removeClass("d-none");

}
function call_back_spawn_messages(response) {
    var json = JSON.parse(response);
    console.log(json);
    $("#doctor-messages").html(spawn_doc_messages(json));
    $("#user-answers").html(spawn_answers(json));
    $("#user-answers").append('<input type="text"  class="form-control" name="message" placeholder="Answer">');

}
function hide_element(id) {
    id = "#" + id;
    console.log(id);
    $(id).addClass("d-none");
}
function show_element(id) {
    id = "#" + id;
    console.log(id);

    $(id).removeClass("d-none");

}
function call_back_error_spawn_messages(response) {
    var json = JSON.parse(response);
    $("#doctor-messages").html(json.response);
    $("#user-answers").html(json.response);
    console.log(json.response);
}

var bit_user_select = true;

function show_doc_id_select() {

    if (bit_user_select) {
        $("#doc-id-messages").removeClass("d-none");
        bit_user_select = false;
        if ($("#doc_id_msg_but").val()) {
            spawn_messages_on_change();
        }
        show_element("doctor-mes-label");
        show_element("user-ans-label");


    } else {
        $("#doc-id-messages").addClass("d-none");
        bit_user_select = true;
        $("#doctor-messages").html("");
        $("#user-answers").html("");
        $("#store-messages").addClass("d-none");

        hide_element("doctor-mes-label");
        hide_element("user-ans-label");
        hide_element("store-response");

    }
}