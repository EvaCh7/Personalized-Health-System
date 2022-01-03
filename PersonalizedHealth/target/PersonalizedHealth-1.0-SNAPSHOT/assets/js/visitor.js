/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



function  get_certified_doctors_and_display_in_map()
{
    const data = null;

    const xhr = new XMLHttpRequest();
    xhr.withCredentials = true;

    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === this.DONE) {
            var json = JSON.parse(this.responseText)
            console.log(json)
            var map = create_map('map')
            for (var index in json) {

                place_marker_in_map(map, 'Map', json[index].lat, json[index].lon, json[index].firstname + " " + json[index].lastname);
            }

        }
    });
    xhr.open('GET', 'certified_doctors', true);
    xhr.setRequestHeader("Content-type", "application/json");
    console.log(data);

    xhr.send(data);
}
function load_doc_data_to_table(json)
{
    var table = document.getElementById('cert-doc-table');
    var total = json.length;
    var cell = new Array();
    for (var i = 0; i < total; i++) {
        row = table.insertRow(i + 1);
        for (var j = 0; j < 8; j++) {
            cell[j] = row.insertCell(j);
        }
        cell[0].innerHTML = json[i].firstname;
        cell[1].innerHTML = json[i].lastname;
        cell[2].innerHTML = json[i].email;
        cell[3].innerHTML = json[i].telephone;
        cell[4].innerHTML = json[i].address;
        cell[5].innerHTML = json[i].city;
        cell[6].innerHTML = json[i].doctor_info;
        cell[7].innerHTML = json[i].specialty;
    }
    document.getElementById("cert-docs").disabled = true;
}
function map_of_doctors() {

}
function create_map(map_id) {
    $(map_id).empty();
    $(map_id).show();
    return map
}
function place_marker_in_map(map, map_id, lat, lon, msg) {

    var position = place_marker(map, lat, lon, msg)
    const zoom = 11;
    map.setCenter(position, zoom);
}