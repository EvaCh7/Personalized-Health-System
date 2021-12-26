/* @Authors George Kokolakis (gkokol@ics.forth.gr) */
"use strict";

function find_location_by_lat_lon(lat, lon, callback) {
    const data = null;

    const xhr = new XMLHttpRequest();
    xhr.withCredentials = true;

    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === this.DONE) {
            callback(this)
        }
    });

    xhr.open("GET", "https://forward-reverse-geocoding.p.rapidapi.com/v1/reverse?lat=" + lat + "&lon=" + lon + "&format=json&accept-language=en&polygon_threshold=0.0");
    xhr.setRequestHeader("x-rapidapi-host", "forward-reverse-geocoding.p.rapidapi.com");
    xhr.setRequestHeader("x-rapidapi-key", "59dd881c7cmsh891f1f8f669b670p125db3jsn78f61fef5840");

    xhr.send(data);
}

$(document).ready(function () {

    $("#show-location").click(handle_location)
    $("#find-location").click(handle_my_location)
})
function handle_my_location() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(get_location);
    } else {
        $("#find-location").prop('disabled', true);
    }
}
function call_back_find_my_location(location_info) {
    var map_info = JSON.parse(location_info.responseText)
    console.log(map_info)
    $("div.countries select").val(map_info.address.country);
    var neighbourhood = ""
    if (map_info.address.neighbourhood) {
        neighbourhood = map_info.address.neighbourhood
    }
    $("#address").val(neighbourhood + " " + map_info.address.postcode);
    $("#city").val(neighbourhood + " " + map_info.address.city);
    $("#error-map").hide();
    show_map("#Map", map_info.lat, map_info.lon)

    lon = map_info.lon
    lat = map_info.lat


}
function get_location(location) {
    var lat = location.coords.latitude
    var lon = location.coords.longitude
    find_location_by_lat_lon(lat, lon, call_back_find_my_location)
}
function handle_location() {
    var country = $('#country').find(":selected").text();
    var address = $('#address').val();
    var city = $('#city').val();
    show_location(country, address, city)
}

function show_location(country, addressName, city) {

    var address = addressName + " " + city + " " + country;
    console.log(address)
    request_maps(address, call_back)
}

function setPosition(lat, lon) {
    var fromProjection = new OpenLayers.Projection("EPSG:4326"); // Transform fromWGS 1984
    var toProjection = new OpenLayers.Projection("EPSG:900913"); // to SphericalMercator Projection
    var position = new OpenLayers.LonLat(lon, lat).transform(fromProjection,
            toProjection);
    return position;
}

function handler(position, message, map) {
    var popup = new OpenLayers.Popup.FramedCloud("Popup",
            position, null,
            message, null,
            true // <-- true if we want a close (X) button, false otherwise
            );
    map.addPopup(popup);
}


function request_maps(address, callback) {
    var data = null
    const xhr = new XMLHttpRequest();
    xhr.withCredentials = true;

    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === this.DONE) {
            callback(this)
        }
    });

    xhr.open("GET", "https://forward-reverse-geocoding.p.rapidapi.com/v1/search?q=" + address + "&acceptlanguage=en&polygon_threshold=0.0");

    xhr.setRequestHeader("x-rapidapi-host", "forward-reverse-geocoding.p.rapidapi.com");
    xhr.setRequestHeader("x-rapidapi-key", "59dd881c7cmsh891f1f8f669b670p125db3jsn78f61fef5840");

    xhr.send(data);
}

function check_for_error(map_id, error_id, map_info) {
    if (jQuery.isEmptyObject(map_info)) {
        $(map_id).hide();
        $(error_id).text("Error can't find the location")
        $(error_id).show();
        return true

    } else if (!map_info[0].display_name.includes("Crete") && !map_info[0].display_name.includes("Κρήτης")) {
        $(map_id).hide();
        $(error_id).text("Error location isn't in Crete")
        $(error_id).show();
        return true
    }
    return false
}
function show_map(map_id, lat, lon) {
    $(map_id).empty();
    $(map_id).show();
    var map = create_map()

    var position = place_marker(map, lat, lon, 'home')
    const zoom = 11;
    map.setCenter(position, zoom);
}
function call_back(param) {

    var map_info = JSON.parse(param.responseText)
    if (!check_for_error("#Map", "#error-map", map_info)) {
        $("#error-map").hide();
        show_map("#Map", map_info[0].lat, map_info[0].lon)
        lon = map_info[0].lon
        lat = map_info[0].lat
    }
    console.log(map_info)
}
function place_marker(map, lat, lon, msg) {
    var markers = new OpenLayers.Layer.Markers("Markers");
    map.addLayer(markers);
    var position = setPosition(lat, lon)
    var mar = new OpenLayers.Marker(position);
    markers.addMarker(mar);
    mar.events.register('mousedown', mar, function (evt) {
        handler(position, msg, map);
    });
    return position

}
function create_map() {
    var map = new OpenLayers.Map("Map");
    var mapnik = new OpenLayers.Layer.OSM();
    map.addLayer(mapnik);
    return map
}