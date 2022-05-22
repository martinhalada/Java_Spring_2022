document.addEventListener("DOMContentLoaded", () => {
    let cityName = localStorage.getItem("name");
    let stateCode = localStorage.getItem("code");
    let uri = "/api/search/city/" + cityName + "/" + stateCode;
    let zpet = document.getElementById("zpetCW");

    let xhttp = new XMLHttpRequest();
    xhttp.onload = function () {
        if (xhttp.status === 200) {
            let nadpisStat = document.createElement("h1");
            nadpisStat.innerHTML = "Aktuální naměřená data pro lokaci: " + cityName;
            zpet.parentNode.insertBefore(nadpisStat, zpet.nextSibling);

            data = JSON.parse(xhttp.responseText);

            let div = document.createElement("div");
            nadpisStat.parentNode.insertBefore(div,nadpisStat.nextSibling);

            let table = document.createElement("table");
            let thead = document.createElement("thead");
            let tbody = document.createElement("tbody");
            let theadTr = document.createElement("tr");
            let theadTh = document.createElement("th");
            theadTh.innerHTML = "Datum";
            theadTr.appendChild(theadTh);

            theadTh = document.createElement("th");
            theadTh.innerHTML = "Teplota";
            theadTr.appendChild(theadTh);

            theadTh = document.createElement("th");
            theadTh.innerHTML = "Tlak";
            theadTr.appendChild(theadTh);

            theadTh = document.createElement("th");
            theadTh.innerHTML = "Vlhkost";
            theadTr.appendChild(theadTh);

            theadTh = document.createElement("th");
            theadTh.innerHTML = "Oblačnost";
            theadTr.appendChild(theadTh);

            theadTh = document.createElement("th");
            theadTh.innerHTML = "Dohlednost";
            theadTr.appendChild(theadTh);

            theadTh = document.createElement("th");
            theadTh.innerHTML = "Rychlost větru";
            theadTr.appendChild(theadTh);

            theadTh = document.createElement("th");
            theadTh.innerHTML = "Směr větru";
            theadTr.appendChild(theadTh);

            thead.appendChild(theadTr);
            table.appendChild(thead);

            let udaje = ["date", "temp", "pressure", "humidity", "clouds", "visibility", "windSpeed", "windDegree"];
            for (let j = 0; j < data.length; j++) {
                let tbodyTr = document.createElement("tr");
                for (let k = 0; k < 8; k++) {
                    let tbodyTd = document.createElement("td");
                    tbodyTd.innerHTML = data[j][udaje[k]];
                    tbodyTr.appendChild(tbodyTd);
                }
                tbody.appendChild(tbodyTr);
            }
            table.appendChild(tbody);
            div.appendChild(table);
        }
    }
    xhttp.open("GET", uri, false);
    xhttp.send();

    let xhttp2 = new XMLHttpRequest();
    xhttp2.onload = function (){
        if(xhttp2.status===200){
            let udaje = ["teplota", "tlak", "vlhkost", "rychlost větru"];
            let avg1day = document.createElement("p");
            let avg7day = document.createElement("p");
            let avg14days = document.createElement("p");
            data = JSON.parse(xhttp2.responseText);

            let table = document.createElement("table");
            let thead = document.createElement("thead");
            let tbody = document.createElement("tbody");
            let theadTr = document.createElement("tr");
            let theadTh = document.createElement("th");
            theadTh.innerHTML = "";
            theadTr.appendChild(theadTh);

            theadTh = document.createElement("th");
            theadTh.innerHTML = "Teplota";
            theadTr.appendChild(theadTh);

            theadTh = document.createElement("th");
            theadTh.innerHTML = "Tlak";
            theadTr.appendChild(theadTh);

            theadTh = document.createElement("th");
            theadTh.innerHTML = "Vlhkost";
            theadTr.appendChild(theadTh);

            theadTh = document.createElement("th");
            theadTh.innerHTML = "Směr větru";
            theadTr.appendChild(theadTh);

            thead.appendChild(theadTr);
            table.appendChild(thead);

            let avgTit = ["Průměry za 1 den", "Průměry za 7 dní", "Průměry za 14 dní"];
            for (let j = 0; j < data.length; j++) {
                let tbodyTr = document.createElement("tr");

                let tbodyTd = document.createElement("td");
                tbodyTd.innerHTML = avgTit[j];
                tbodyTr.appendChild(tbodyTd);

                for (let k = 0; k < 4; k++) {
                    let tbodyTd = document.createElement("td");
                    tbodyTd.innerHTML = data[j][k];
                    tbodyTr.appendChild(tbodyTd);
                }
                tbody.appendChild(tbodyTr);
            }
            table.appendChild(tbody);
            zpet.parentNode.insertBefore(table,zpet.nextSibling);
        }
    }
    let uriAvg = "/api/avg/"+cityName;
    xhttp2.open("GET", uriAvg, false);
    xhttp2.send();

});