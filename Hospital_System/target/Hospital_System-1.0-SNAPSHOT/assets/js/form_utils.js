/* @Authors George Kokolakis (gkokol@ics.forth.gr) */
"use strict";

var isPopoverEnabled = false
var lat = 0
var lon = 0
var valid_email = true;
var valid_username = true;
var valid_amka = true;
function check_if_passwords_equal() {
    var pswd = $("#pswd").val();

    var repswd = $("#re-pswd").val();
    if (pswd !== repswd) {
        console.log("not equals")

        isPopoverEnabled = true
        $('#pswd').popover({
            content: "passwords doesn't match",
            placement: 'left'
        })
                .popover('show');

    } else {
        if (isPopoverEnabled) {
            $("#pswd").popover('hide')
            isPopoverEnabled = false
            console.log(" equals")
        }

    }
}

function handle_pswd_visibility() {

    var pswd = document.getElementById("pswd")
    var re_pswd = document.getElementById("re-pswd")


    if ($("#check-show-pswd").is(':checked')) {
        pswd.type = "text"
        re_pswd.type = "text"

    } else {
        pswd.type = "password"
        re_pswd.type = "password"



    }

}
function check_if_password_is_50_percent_or_higher_with_numbers(password) {
    var pwd_length = password.length
    var count_numbers = 0
    for (var i in password) {
        if (!isNaN(password[i])) {
            count_numbers++;
        }
    }
    if (count_numbers >= (pwd_length) / 2) {
        document.getElementById("pswd-strength").innerHTML = "weak password"
        return true


    }
    return false
}
function characters_frequency(password) {
    var ch_freq = {}
    for (var i in password) {
        if (ch_freq[password[i]] == undefined) {
            ch_freq[password[i]] = 1

        } else {
            ch_freq[password[i]]++;

        }
    }
    return ch_freq
}
function check_if_a_character_shows_up_more_than_50_percent(password) {
    var pwd_length = password.length
    var ch_freq = characters_frequency(password)
    for (var key in ch_freq) {
        if (ch_freq[key] >= (pwd_length / 2)) {
            document.getElementById("pswd-strength").innerHTML = "weak password"
            return true

        }
    }
    return false

}
function count_unique_characters(ch_freq) {
    return Object.keys(ch_freq).length
}
function check_if_password_80_percent_of_characters_are_different(password) {
    var ch_freq = characters_frequency(password)
    var unique_ch = count_unique_characters(ch_freq)


    if (unique_ch / password.length >= 0.8) {
        document.getElementById("pswd-strength").innerHTML = "Strong password"
        return true
    }
    return false

}
function check_password_strength() {
    var pswd = document.getElementById("pswd").value
    if (pswd.length == 0)
        return
    document.getElementById("pswd-strength").classList.remove("d-none")

    if (check_if_password_is_50_percent_or_higher_with_numbers(pswd)) {
    } else if (check_if_a_character_shows_up_more_than_50_percent(pswd)) {
    } else if (check_if_password_80_percent_of_characters_are_different(pswd)) {
    } else {
        document.getElementById("pswd-strength").innerHTML = "medium password"
    }
}
function handle_doctor_radio_button() {
    if ($('#doctor-radio').is(':checked')) {
        document.getElementById("doctor-extra").classList.remove("d-none")
        document.getElementById("address").placeholder = "Enter Doctor Address"
    } else {
        document.getElementById("doctor-extra").classList.add("d-none")
        document.getElementById("address").placeholder = "Enter Address"

    }
}
function on_change() {
    var amka = document.getElementById("amka").value
    var birth_date = document.getElementById("birth-date").value
    check_amka(amka, birth_date)
    check_password_strength()
    check_if_passwords_equal();

}


function check_amka(amka, date) {
    if (amka.length == 0) {
        valid_amka = false
        return
    }
    var tmp_date = [];
    date = date.split('-').join('') //cut the - 
    var x;
    for (i = 0; i < date.length; i = i + 2) {

        x = date[i]
        tmp_date[i] = date[i + 1]
        tmp_date[i + 1] = x
    }
    date = tmp_date.reverse()

    for (var i = 0; i < 6; i++) {
        if (amka[i] != date[i]) {
            valid_amka = false

            console.log("wrong amka")
            //$("#submit-button").prop("disabled", true);
            return
        }
    }
    // $("#submit-button").removeAttr('disabled');
    console.log("valid amka")
    valid_amka = true;

}
var blood_type = null

$(document).ready(function () {

    $("input").change(on_change);
    $("#username").change(check_if_username_exists);
    $("#email").change(check_if_email_exists);
    $("#amka").change(check_if_amka_exists);

    $('#register-form').on('submit', function (e)
    {
        console.log("omg")
        send_form_to_server(e)
    })

    $("#check-show-pswd").click(handle_pswd_visibility);
    $("input[name=type-of-user-radio]").click(handle_doctor_radio_button);


})
function callback_register(response)
{

    $("#error").html(remove_str($("#error").html(), " register error"))
     document.getElementById("register-form").style.display="none";
    $("#after_register").html("register successful<br>" + response)

}
var url = "http://localhost:8080/Hospital_System/"

function send_form_to_server(e) {
    console.log("omgg")
    /*
     if (!valid_amka || !valid_email || !valid_username)
     {
     console.log("not valid form informations")
     return
     
     }*/
    e.preventDefault();
    var speciality = null;
    var doctor_info = null;
    var register_url = "register"



    var data = get_user_form_data()
    console.log(data)
    sendXmlPostRequest(register_url, data, callback_register, call_back_error_register);

}
function check_if_amka_exists()
{
    var check_amka_url = url + "check_amka"
    var amka = $("#amka").val()
    var data = {amka: amka}
    sendXmlPostRequest(check_amka_url, data, call_back_check_amka, call_back_error_amka);
}

function check_if_username_exists()
{
    var check_username_url = url + "check_username"
    var user_name = $("#username").val()
    var data = {username: user_name}
    sendXmlPostRequest(check_username_url, data, call_back_check_username, call_back_error_username);
}
function check_if_email_exists()
{
    var check_email_url = url + "check_email"
    var email = $("#email").val()
    var data = {email: email}
    sendXmlPostRequest(check_email_url, data, call_back_check_email, call_back_error_email);
}
function call_back_check_email(response)
{
    $("#error").html(remove_str($("#error").html(), " email already exists"))

    console.log(response)
    $("#submit-button").prop("disabled", false);

}
function call_back_check_amka(response)
{
    $("#error").html(remove_str($("#error").html(), " Amka already exists"))
    $("#submit-button").prop("disabled", false);

    console.log(response)
}

function call_back_check_username(response)
{
    $("#error").html(remove_str($("#error").html(), " username already exists"))
    $("#submit-button").prop("disabled", false);

    console.log(response)
}
function call_back_error_username()
{
    var text = $("#error").html()
    valid_username = false
    if (!text.includes(" username already exists"))
        $("#error").html(text + " username already exists");
    $("#submit-button").prop("disabled", true);

}
function call_back_error_email()
{
    var text = $("#error").html()
    valid_email = false
    if (!text.includes(" email already exists"))
        $("#error").html(text + " email already exists");
    $("#submit-button").prop("disabled", true);

}
function call_back_error_amka()
{
    var text = $("#error").html()
    valid_amka = false
    if (!text.includes(" Amka already exists"))
        $("#error").html(text + " Amka already exists");
    $("#submit-button").prop("disabled", true);
}

function call_back_error_register()
{
    var text = $("#error").html()
    if (!text.includes(" register error"))
        $("#error").html(text + " register error");
}
function remove_str(str, to_remove)
{
    var ret = str.replace(to_remove, ' ');
    return ret;
}
