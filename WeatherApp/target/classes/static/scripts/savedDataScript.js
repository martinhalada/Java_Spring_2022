document.addEventListener("DOMContentLoaded", () => {
    let stateCode = localStorage.getItem("code");
    let uri = "/api/search/state/"+stateCode;
    let zpet = document.getElementById("zpet");

    let xhttp = new XMLHttpRequest();
    xhttp.onload = function () {
        if (xhttp.status === 200) {
            let nadpisStat = document.createElement("h1");
            nadpisStat.innerHTML = "Naměřená data pro stát: "+ stateCode;
            zpet.parentNode.insertBefore(nadpisStat, zpet.nextSibling);

            data = JSON.parse(xhttp.responseText);

            for(let i = 0; i < data.length; i++){
                let div = document.createElement("div");
                let nadpisMesto = document.createElement("h2");
                nadpisMesto.innerHTML = data[i][0]["locationName"];
                nadpisStat.parentNode.insertBefore(div,nadpisStat.nextSibling);
                div.appendChild(nadpisMesto);

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
                for(let j = 0; j < data[i].length; j++){
                    let tbodyTr = document.createElement("tr");
                    for(let k = 0; k < 8; k++){
                        let tbodyTd = document.createElement("td");
                        tbodyTd.innerHTML = data[i][j][udaje[k]];
                        tbodyTr.appendChild(tbodyTd);
                    }
                    tbody.appendChild(tbodyTr);
                }
                table.appendChild(tbody);
                div.appendChild(table);
            }
        }
    }
    xhttp.open("GET", uri, true);
    xhttp.send();
});