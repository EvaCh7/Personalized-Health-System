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


/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function get_measure()
{

    var amka = $("#amka").val()
    var measure = $("#measure").val();
    var url = "examinations/bloodTests/" + amka + "/" + measure
    console.log(url)

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
