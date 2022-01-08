var lat = 0
var lon = 0
function update_doctor_data(e) {
    var gender
    if ($('#man').is(':checked')) {
        gender = "male"
    } else if ($('#woman').is(':checked')) {
        gender = "female"
    } else {
        gender = "other"
    }
    var blood_donor
    if ($('#blood-giver').is(':checked')) {
        blood_donor = 1
    } else {
        blood_donor = 0
    }



    var data = {
        username: $("#username").val(),
        email: $("#email").val(),
        password: $("#pswd-doc").val(),
        firstname: $("#firstname").val(),
        lastname: $("#surname").val(),
        birthdate: $("#birth-date").val(),
        gender: gender,
        amka: $("#doc-amka").val(),
        country: $("#country").val(),
        city: $("#city").val(),
        address: $("#address").val(),
        lat: lat,
        lon: lon,
        telephone: $("#telephone").val(),
        height: $("#height").val(),
        weight: $("#weight").val(),
        blooddonor: blood_donor,
        bloodtype: $("#blood-type-doc").val(),
        specialty: get_doctor_specialty(),
        doctor_info: get_doctor_info()

    };
    e.preventDefault();

    console.log(data)
    sendXmlPostRequest("/PersonalizedHealth/doctor", data, call_back_update_data, call_back_error_update_data);
}

function create_randevouz_element(rand_obj)
{
    var element = `

                                                <div class=" mt-5 container form-card row">

                        <div class="col-1">

                            <input type="number" id="randevouz_id" value=` + rand_obj.randevouz_id + ` name="randevouz_id" class="form-control" placeholder="Randevouz ID">
                        </div>

                        <div class="col-1">
                            <input type="number" id="user_id" name="user_id" value=` + rand_obj.user_id + ` class="form-control" placeholder="User ID">
                        </div>
                     
                 
                    

                    <div class="col-2">
    

    <input type="text" class="form-control" id="user_info" name="user_info" placeholder="User Info"

               value="` + rand_obj.user_info + `"   >  
</div>
      <div class="col-2">
    

    <input type="text" class="form-control" id="status" name="status" placeholder="status"  value="`

            + rand_obj.status + `"   >  
</div>

                    <div class="col-2">

    <input type="text" id="date_time" name="date_time" class="form-control" value="` + rand_obj.date_time + `"  required disabled>
</div> </div>

`
    return element
}
let proc_date_bit = true;
function spawn_process_date() {
    if (proc_date_bit) {
        $("#process-date-class").removeClass("d-none")
        proc_date_bit = false
        console.log("xaxa")
    } else
    {
        $("#process-date-class").addClass("d-none")
        proc_date_bit = true

        $("#process-randevouz-form").html('')
        $("#save-rand-but").addClass("d-none")
        $("#download-rand-but").addClass("d-none")



    }
}
function spawn_randevouz()
{

    $("#save-rand-but").removeClass("d-none")
    $("#download-rand-but").removeClass("d-none")

    $("#process-randevouz-form").html('')
    var date = $("#process-date").val()
    url = "examinations/randevouz/getRandevouz" + "/" + doctorData.doctor_id + "?date=" + date
    console.log(url)
    sendXmlGetRequest(url, call_back_get_randevouz, call_back_error_get_randevouz)

}




function call_back_get_randevouz(response)
{
    var json = JSON.parse(response)
    console.log(json)
    for (let x in json) {
        var prev_html = $("#process-randevouz-form").html()
        prev_html += create_randevouz_element(json[x])
        $("#process-randevouz-form").html(prev_html)
    }

    get_form_data_to_json_array("process-randevouz-form")
    array.push(data)

}

function save_randevouz() {
    url = "examinations/randevouz/updateRandevouz"
    data = {}
    array = []
    get_form_data_to_json_array("process-randevouz-form")

    array.push(data)
    console.log(JSON.stringify(array))

    sendXmlPutRequestJsonData(url, array, call_back_save_randevouz, call_back_error_save_randevouz)

}
function call_back_save_randevouz(response) {
    js = JSON.parse(response)
    console.log(js.response)

}
function call_back_error_save_randevouz(response) {
    js = JSON.parse(response)
    console.log(js.response)

}
function fill_array(key, value)
{
    if (key in data) {
        array.push(data)
        console.log("inserted data:", data)
        data = {}
    }
    if (value == "")
    {
        data[key] = null;
    } else
    {
        data[key] = value
    }
}
let array = new Array();
let data = {};

function get_form_data_to_json_array(form_id)
{
    data = {}
    array = new Array();
    let myForm = document.getElementById(form_id);
    const formData = new FormData(myForm);

    formData.forEach((value, key) => (fill_array(key, value)));

    array.push(data)
    console.log("inserted data on end :", data)

    return JSON.parse(JSON.stringify(array));

}
function call_back_error_get_randevouz(response)
{

    console.log("error", response)

}

var show_add_randev_form = true
function show_add_randevouz()
{
    if (show_add_randev_form)
    {
        $("#add-randevuz-form").removeClass("d-none")
        show_add_randev_form = false
    } else {
        $("#add-randevuz-form").addClass("d-none")
        show_add_randev_form = true
    }
}

var show_delete_randev_form = true
function show_delete_randevouz()
{
    if (show_delete_randev_form)
    {
        $("#delete-randevouz-form").removeClass("d-none")
        show_delete_randev_form = false
    } else {
        $("#delete-randevouz-form").addClass("d-none")
        show_delete_randev_form = true
    }
}

function delete_randevouz()
{
    var id = $("#randevouz_id").val()
    var url = "examinations/randevouz/cancelRandevouz" + "/" + id
    sendXmlDeleteRequest(url, call_back_cancel_randevouz, call_back_error_cancel_randevouz)
}
function download_randevouz() {
    var date = $("#process-date").val()
    url = "examinations/randevouz/getRandevouzPDF" + "/" + doctorData.doctor_id + "?date=" + date
    var request = new XMLHttpRequest();
    request.open("GET", url, true);
    request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    request.responseType = 'blob';

    request.onreadystatechange = function () {
        if (this.readyState === 4 && (this.status >= 200 && this.status < 300)) {
            var a = document.createElement('a');
            a.href = window.URL.createObjectURL(request.response);
            // Give filename you wish to download
            a.download = "randevouz.pdf"
            a.style.display = 'none';
            document.body.appendChild(a);
            a.click();
        } else if (this.status !== 200) {
            console.log(request.response.response)
        }
    };
    request.send();

}


function call_back_cancel_randevouz(_response)
{
    var json = JSON.parse(_response)
    $("#del_rand_response").html(json.response)

}

function call_back_error_cancel_randevouz(_response)
{
    var json = JSON.parse(_response)
    $("#del_rand_response").html(json.response)


}


function send_new_randevouz()
{
    data = get_form_data_to_json("add-randevuz-form")
    data["status"] = "free"
    console.log(doctorData)
    data["doctor_id"] = doctorData.doctor_id
    sendXmlPostRequest("examinations/randevouz/addRandevouz", data, call_back_new_randev, call_back_error_new_randev)


}

function call_back_new_randev(response)
{
    var response = JSON.parse(response)
    console.log(response.response)
    $("#add_rand_response").html(response.response)
}

function call_back_error_new_randev(response)
{
    var response = JSON.parse(response)

    $("#add_rand_response").html(response.response)


}

function get_doctor_specialty()
{
    return  $("#dc-speciality").val()
}

function get_doctor_info()
{

    return  $("#doctor-text-area").val()

}

function call_back_update_data(response)
{
    get_doctor_data()
    $("#after_register").html("data updated succesfully")
    console.log(response)
}

function call_back_error_update_data(response)
{
    $("#error").html("error occured " + response)
}

function fill_doctor_info(responseData) {
    console.log(responseData.bloodtype)
    $("#dc-speciality").val(responseData.specialty).change()
    $("#doctor-text-area").val(responseData.doctor_info)
    $("#username").val(responseData.username)
    $("#email").val(responseData.email)
    $("#pswd-doc").val(responseData.password)
    $("#firstname").val(responseData.firstname)
    $("#surname").val(responseData.lastname)
    $("#birth-date").val(responseData.birthdate)
    if (responseData.gender === "male")
        $("#man").prop("checked", true);
    else if (responseData.gender === "female")
        $("#woman").prop("checked", true);
    else
        $("#other").prop("checked", true);

    if (responseData.blooddonor === "1")
        $("#blood-giver").prop("checked", true);
    else
        $("#no").prop("checked", true);

    $("#birth-date").val(responseData.birthdate)
    $("#doc-amka").val(responseData.amka)
    $("#country").val(responseData.country)
    $("#city").val(responseData.city)
    $("#address").val(responseData.address)
    $("#telephone").val(responseData.telephone)
    $("#height").val(responseData.height)
    $("#weight").val(responseData.weight)
    $("#blood-type").val(responseData.bloodtype)
}

let doctorData;
function get_doctor_data()
{
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const responseData = JSON.parse(xhr.responseText);
            fill_doctor_info(responseData);
            doctorData = responseData;
        } else if (xhr.status !== 200) {
        }
    };
    xhr.open('GET', 'doctor?type=doctor_info');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}

$(document).ready(function () {
    get_doctor_data();
    $("#bmi").click(calculate_and_display_bmi)
    $("#ideal-weight").click(calculate_and_display_ideal_weight)

    $('#update-doctor-form').on('submit', function (e)
    {
        update_doctor_data(e)
    })

});

function calculate_and_display_bmi()

{
    const data = null;

    const xhr = new XMLHttpRequest();
    xhr.withCredentials = true;

    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === this.DONE) {
            var json = JSON.parse(this.responseText)
            console.log(json)
            $("#bmi-text").html("you have " + json.data.health + " BMI!")
        }
    });
    var weight = $("#weight").val()
    var height = $("#height").val()

    xhr.open("GET", "https://fitness-calculator.p.rapidapi.com/bmi?age=25&weight=" + weight + "&height=" + height);
    xhr.setRequestHeader("x-rapidapi-host", "fitness-calculator.p.rapidapi.com");
    xhr.setRequestHeader("x-rapidapi-key", "59dd881c7cmsh891f1f8f669b670p125db3jsn78f61fef5840");

    xhr.send(data);
}


function calculate_and_display_ideal_weight() {
    const data = null;

    const xhr = new XMLHttpRequest();
    xhr.withCredentials = true;

    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === this.DONE) {

            var json = JSON.parse(this.responseText)
            console.log(json)

            $("#ideal-weight-text").html("ideal weight is " + json.data.Devine + " kg")
        }
    });
    var gender = get_gender();
    var height = $("#height").val()


    xhr.open("GET", "https://fitness-calculator.p.rapidapi.com/idealweight?gender=" + gender + "&height=" + height);
    xhr.setRequestHeader("x-rapidapi-host", "fitness-calculator.p.rapidapi.com");
    xhr.setRequestHeader("x-rapidapi-key", "59dd881c7cmsh891f1f8f669b670p125db3jsn78f61fef5840");

    xhr.send(data);
}

