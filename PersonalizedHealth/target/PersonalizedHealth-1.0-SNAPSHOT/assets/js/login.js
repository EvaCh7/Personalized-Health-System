function send_login() {

    login_url = "login"
    var data = {
        username: $("#username").val(),
        password: $("#pswd").val()

    };
    sendXmlPostRequest(login_url, data, call_back_login, call_back_error_login);
    return false

}

function call_back_login(response) {
        console.log("succesful login")

    json = JSON.parse(response)
    console.log(json)

    $("#error").html('')
    if (json.type === "admin") {
        $("#ajaxContent").load("admin.html")
    } else if (json.type === "user") {
        $("#ajaxContent").load("user.html")
    } else if (json.type === "doctor") {
        $("#ajaxContent").load("doctor.html")
    } else if (json.type = "uncertified_doctor")
    {
        $("#ajaxContent").html("admin needs to certifie you")
    }
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
            json = JSON.parse(xhr.responseText)
            console.log(json)
            $("#ajaxContent").html("Welcome again " + json.username);



            if (json.type === "admin") {
                $("#ajaxContent").load("admin.html")
            } else if (json.type === "user") {
                $("#ajaxContent").load("user.html")
            } else if (json.type === "doctor") {
                $("#ajaxContent").load("doctor.html")
            } else if (json.type = "uncertified_doctor")
            {
                $("#ajaxContent").html("admin needs to certifie you")
            }

        } else if (xhr.status !== 200) {
            
        }
    };
    xhr.open('GET', 'login');
    xhr.send();
}



