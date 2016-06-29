
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <title>Google Maps AJAX + mySQL/PHP Example</title>
    <script src="https://maps.googleapis.com/maps/api/js?key=${apiKey}" type="text/javascript">
    </script>
    <script type="text/javascript">
    //<![CDATA[
    var map;
    var markers = [];
    var infoWindow;
    var locationSelect;

    function load() {
      map = new google.maps.Map(document.getElementById("map"), {
        center: new google.maps.LatLng(40, -100),
        zoom: 4,
        mapTypeId: 'roadmap',
        mapTypeControlOptions: {style: google.maps.MapTypeControlStyle.DROPDOWN_MENU}
      });
      infoWindow = new google.maps.InfoWindow();

      locationSelect = document.getElementById("locationSelect");
      locationSelect.onchange = function() {
        var markerNum = locationSelect.options[locationSelect.selectedIndex].value;
        if (markerNum != "none"){
          google.maps.event.trigger(markers[markerNum], 'click');
        }
      };
   }

   /*
     The PHP script takes latitude and longitude parameters in order to perform the search. 
     Since most people who use your map will know their address but not their coordinates, 
     you can use the Geocoder class to turn their address into a coordinate. 
     Passes in the address from the textbox to the asynchronous Geocoder.doGeocode function, 
     gets a LatLng in response, and sends it off to the searchLocationsNear function 
     if the geocode was successful. 
     
     searchLocations is triggered by clicking the search button. 
   */
   function searchLocations() {
     var address = document.getElementById("addressInput").value;
     var geocoder = new google.maps.Geocoder();
     geocoder.geocode({address: address}, function(results, status) {
       if (status == google.maps.GeocoderStatus.OK) {
        searchLocationsNear(results[0].geometry.location);
       } else {
         alert('address: '+address + ' not found');
       }
     });
   }

   /*
     Between searches, you need to remove the previously displayed infowindow, markers 
     and dropdown options. 
   */
   function clearLocations() {
     infoWindow.close();
     for (var i = 0; i < markers.length; i++) {
       markers[i].setMap(null);
     }
     markers.length = 0;

     locationSelect.innerHTML = "";
     var option = document.createElement("option");
     option.value = "none";
     option.innerHTML = "See all results:";
     locationSelect.appendChild(option);
   }

   function searchLocationsNear(center) {
     clearLocations();

     var radius = document.getElementById('radiusSelect').value;
     var searchUrl = 'mystoreSearch?lat=' + center.lat() + '&lng=' + center.lng() + '&radius=' + radius;
     downloadUrl(searchUrl, function(data) {
       // The function body will be triggered only after the asynchrounou response for XML is 
       // completed and data is fully populated. 	 
       var xml = parseXml(data.trim());
       var markerNodes = xml.documentElement.getElementsByTagName("marker");
       if( markerNodes.length == 0 ) {
    	   alert('Nothing found.');
    	   return;
       }
       var bounds = new google.maps.LatLngBounds();
       for (var i = 0; i < markerNodes.length; i++) {
         var name = markerNodes[i].getAttribute("name");
         var address = markerNodes[i].getAttribute("address");
         var distance = parseFloat(markerNodes[i].getAttribute("distance"));
         var latlng = new google.maps.LatLng(
              parseFloat(markerNodes[i].getAttribute("lat")),
              parseFloat(markerNodes[i].getAttribute("lng")));

         createOption(name, distance, i);
         createMarker(latlng, name, address);
         bounds.extend(latlng);
       }
       map.fitBounds(bounds);
       locationSelect.style.visibility = "visible";
       locationSelect.onchange = function() {
         var markerNum = locationSelect.options[locationSelect.selectedIndex].value;
         google.maps.event.trigger(markers[markerNum], 'click');
       };
      });
    }

    /*
      create a marker at the given LatLng, and add an event listener to the marker so that 
      when clicked, an info window is displayed showing the name and address. Note that 
      since API v3 permits multiple infowindows to be displayed on the map at once, 
      you can re-use the same infowindow variable throughout the code to ensure that only 
      one infowindow ever displays. 
    */
    function createMarker(latlng, name, address) {
      var html = "<b>" + name + "</b> <br/>" + address;
      var marker = new google.maps.Marker({
        map: map,
        position: latlng
      });
      google.maps.event.addListener(marker, 'click', function() {
        infoWindow.setContent(html);
        infoWindow.open(map, marker);
      });
      markers.push(marker);
    }

    /*
       create an option element that displays the name and distance in parantheses. 
       The value of the option is the index of the marker in the global markers array. 
       You can use this value to open the infowindow over the marker when the option is selected. 
    */
    function createOption(name, distance, num) {
      var option = document.createElement("option");
      option.value = num;
      option.innerHTML = name + "(" + distance.toFixed(1) + ")";
      locationSelect.appendChild(option);
    }

    /*
       To load the XML file into the page, you can take advantage of the browser-provided 
       XMLHttpRequest object. This object lets you retrieve a file that resides on the same domain 
       as the requesting webpage, and is the basis of "AJAX" programming. 
       
       Since XMLHttpRequest is asynchronous, the callback function won't be called as soon as 
       you invoke downloadUrl. The bigger your XML file, the longer it may take. Don't put any 
       code after downloadUrl that relies on the markers existing already—put it inside the 
       callback function instead. 
    */
    function downloadUrl(url, callback) {
      var request = window.ActiveXObject ?
          new ActiveXObject('Microsoft.XMLHTTP') :
          new XMLHttpRequest;

      request.onreadystatechange = function() {
        if (request.readyState == 4) {
          request.onreadystatechange = doNothing;
          callback(request.responseText, request.status);
        }
      };

      request.open('GET', url, true);
      request.send(null);
    }

    function parseXml(str) {
      if (window.ActiveXObject) {
        var doc = new ActiveXObject('Microsoft.XMLDOM');
        doc.loadXML(str);
        return doc;
      } else if (window.DOMParser) {
        return (new DOMParser).parseFromString(str, 'text/xml');
      }
    }

    function doNothing() {}

    //]]>
  </script>
  </head>

  <body style="margin:0px; padding:0px;" onload="load()">
    <div>
     <input type="text" id="addressInput" size="10"/>
    <select id="radiusSelect">
      <option value="25" selected>25mi</option>
      <option value="100">100mi</option>
      <option value="200">200mi</option>
    </select>

    <input type="button" onclick="searchLocations()" value="Search"/>
    </div>
    <div><select id="locationSelect" style="width:100%;visibility:hidden"></select></div>
    <div id="map" style="width: 100%; height: 80%"></div>
  </body>
</html>
