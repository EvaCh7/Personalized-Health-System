function showLoginForm() {
    console.log("login page load")
    $("#ajaxContent").load("login.html");
    //  $("#page-container").addClass("d-none")



}



function showregisterForm() {
    $("#ajaxContent").load("register.html");
    //   $("#page-container").addClass("d-none")

}
function logout() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $("#ajaxContent").html("Successful Logout");
        } else if (xhr.status !== 200) {
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };
    xhr.open('POST', 'logout');
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    xhr.send();
}