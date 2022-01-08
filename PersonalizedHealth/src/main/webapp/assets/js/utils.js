/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function get_user_form_data()
{

    var data = {
        username: $("#username").val(),
        email: $("#email").val(),
        password: $("#pswd").val(),
        firstname: $("#firstname").val(),
        lastname: $("#surname").val(),
        birthdate: $("#birth-date").val(),
        gender: get_gender(),
        amka: $("#amka").val(),
        country: $("#country").val(),
        city: $("#city").val(),
        address: $("#address").val(),
        lat: lat,
        lon: lon,
        telephone: $("#telephone").val(),
        height: $("#height").val(),
        weight: $("#weight").val(),
        blooddonor: get_blood_donor(),
        bloodtype: $("#blood-type").val(),
        specialty: get_doctor_specialty(),
        doctor_info: get_doctor_info()

    };
    return data;

}


function get_gender()
{
    var gender
    if ($('#man').is(':checked')) {
        gender = "male"
    } else if ($('#woman').is(':checked')) {
        gender = "female"
    } else {
        gender = "other"
    }
    return gender
}
function  get_blood_donor()
{
    var blood_donor
    if ($('#blood-giver').is(':checked')) {
        blood_donor = 1
    } else {
        blood_donor = 0
    }
    return blood_donor
}

function get_doctor_specialty()
{
    if ($('#doctor-radio').is(':checked')) {
        return  $("#dc-speciality").val()

    }
    return null;
}
function get_doctor_info()
{
    if ($('#doctor-radio').is(':checked')) {

        return  $("#doctor-text-area").val()
    }
    return null;

}
function fill_empty_with_null(data, key, value)
{
    if (value == "")
    {
        data[key] = null;
    } else
    {
        data[key] = value
    }
}
function get_form_data_to_json(form_id)
{
    let myForm = document.getElementById(form_id);
    const formData = new FormData(myForm);
    const data = {};

    formData.forEach((value, key) => (fill_empty_with_null(data, key, value)));
    return data;

}