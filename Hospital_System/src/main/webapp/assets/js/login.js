function send_login() {

    register_url = "/Hospital_System/login"
    var data = {
        username: $("#username").val(),
        password: $("#pswd").val()

    };
    sendXmlPostRequest(register_url, data, call_back_login, call_back_error_login);
    return false

}
function call_back_login() {
    console.log("succesful login")
    $("#ajaxContent").html("Successful Login");
    $("#error").html('')
    $("#ajaxContent").load("user.html")


}
function call_back_error_login() {
    console.log("failed to login")
    $("#error").html("Wrong Credentials");

}

$(document).ready(function () {
    isLoggedIn();
});

function isLoggedIn() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $("#ajaxContent").html("Welcome again " + xhr.responseText);
            $("#ajaxContent").load("user.html")

        } else if (xhr.status !== 200) {
        }
    };
    xhr.open('GET', 'login');
    xhr.send();
}



