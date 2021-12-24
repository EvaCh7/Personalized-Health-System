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


/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function update_blood_test()
{

    var amka = $("#blood-id").val()
    var measure = $("#measure").val();
    console.log(measure)
    var val = $("#value").val();


    var url = "examinations/bloodTest/" + amka + "/" + measure + "/" + val
    console.log(url)

    sendXmlPutRequest(url, callback, callback_error)
}
function callback(response)
{
    $("#content").html(response)
}
function callback_error(response)
{
    $("#error").html(response)
}
