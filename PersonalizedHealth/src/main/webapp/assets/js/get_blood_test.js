/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


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
function get_blood_tests()
{

    var amka = $("#amka_rest").val()
    var url = "examinations/bloodTests/" + amka
    var from_date = $("#fromDate").val();
    var to_date = $("#toDate").val();
    if (from_date != "")
    {
        url += "?fromDate=" + from_date
    }
    if (from_date != "" && to_date != "")
    {
        url += "&toDate=" + to_date

    }

    sendXmlGetRequest(url, callback, callback_error)
}
function callback(response)
{
    $("#content").html(response)
}
function callback_error(response)
{
    $("#error").html(response)
}
