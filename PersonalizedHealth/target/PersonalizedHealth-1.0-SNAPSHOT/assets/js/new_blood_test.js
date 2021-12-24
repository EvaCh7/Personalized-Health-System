/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function lala(data, key, value)
{
    if (value !== "")
    {
        data[key] = value
    }
}
function send_new_blood_test()
{
    let myForm = document.getElementById('myForm');
    let formData = new FormData(myForm);
    const data = {};
    formData.forEach((value, key) => (lala(data, key, value)));
    console.log(data)
    sendXmlPostRequest("examinations/newBloodTest", data, call_back_new_blood_test, call_back_error_new_blood_test)

}
function call_back_new_blood_test(response)
{
    $("#content").html(response)
}
function call_back_error_new_blood_test(response)
{
    $("#error").html(response)
}
