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


/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function delete_blood_test()
{

    var blood_id = $("#blood-id").val()


    var url = "examinations/bloodTestDeletion/" + blood_id
    console.log(url)

    sendXmlDeleteRequest(url, callback, callback_error)
}
function callback(response)
{
    $("#content").html(response)
}
function callback_error(response)
{
    $("#error").html(response)
}
