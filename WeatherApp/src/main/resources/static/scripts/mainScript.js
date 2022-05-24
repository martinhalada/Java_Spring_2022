const createNewStateForm = document.getElementById("createNewStateForm");
const deleteStateForm = document.getElementById("deleteStateForm");
const createNewCityForm = document.getElementById("newCityForm");
const deleteCityForm = document.getElementById("deleteCityForm");
const forecastStateForm = document.getElementById("forecastStateForm");
const forecastOnlyCityForm = document.getElementById("forecastOnlyCityForm");
const updateStateForm = document.getElementById("updateStateForm");
const updateCityForm = document.getElementById("updateCityForm");

forecastStateForm.addEventListener("submit", ()=>{
   let stateCode = forecastStateForm.elements["code"].value;
   localStorage.setItem("code", stateCode);
});

forecastOnlyCityForm.addEventListener("submit",()=>{
    let name = forecastOnlyCityForm.elements["name"].value;
    let code = forecastOnlyCityForm.elements["code"].value;
    localStorage.setItem("name", name);
    localStorage.setItem("code",code);
});

createNewStateForm.addEventListener("submit", () => {
    let stateName = createNewStateForm.elements["name"].value;
    let stateCode = createNewStateForm.elements["code"].value;
    let xhr = new XMLHttpRequest();
    xhr.open("POST", "/api/states", false);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(JSON.stringify({
        "name": stateName,
        "code": stateCode
    }));
    location.reload();
});

deleteStateForm.addEventListener("submit", () => {
    let stateCode = deleteStateForm.elements["code"].value;
    let xhr = new XMLHttpRequest();
    xhr.open("DELETE", "/api/states/"+stateCode, false);
    xhr.send();
    location.reload();
});

updateStateForm.addEventListener("submit", () => {
    let newName = updateStateForm.elements["name"].value;
    let code = updateStateForm.elements["code"].value;
    let xhr = new XMLHttpRequest();
    xhr.open("POST", "/api/updateState", false);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(JSON.stringify([
        newName,
        code
    ]));
    location.reload();
});

updateCityForm.addEventListener("submit", () => {
    let oldName = updateCityForm.elements["oldName"].value;
    let newName = updateCityForm.elements["name"].value;
    let code = updateCityForm.elements["region"].value;
    let xhr = new XMLHttpRequest();
    xhr.open("POST", "/api/updateCity", false);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(JSON.stringify([
        oldName,
        newName,
        code
    ]));
    location.reload();
});

createNewCityForm.addEventListener("submit", () => {
    let cityName = createNewCityForm.elements["name"].value;
    let cityCode = createNewCityForm.elements["region"].value;
    let xhr = new XMLHttpRequest();
    xhr.open("POST", "/api/cities", false);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(JSON.stringify({
        "name": cityName,
        "region": cityCode,
        "state": {
            "name": null,
            "code": cityCode
        }
    }));
    location.reload();
});

deleteCityForm.addEventListener("submit", () => {
    let cityName = deleteCityForm.elements["name"].value;
    console.log(cityName);
    let xhr = new XMLHttpRequest();
    xhr.open("DELETE", "/api/cities/"+cityName, false);
    xhr.send();
    location.reload();
});

document.addEventListener("DOMContentLoaded", getStatesAndCities);

function vytvorTabulku(nazevPrvniSloupec, nazevDruhySloupec, atrPrvni, atrDruhy, xhttp){
    let table = document.createElement("table");
    let thead = document.createElement("thead");
    let tbody = document.createElement("tbody");
    let theadTr = document.createElement("tr");
    let theadTh = document.createElement("th");
    theadTh.innerHTML = nazevPrvniSloupec;
    theadTr.appendChild(theadTh);
    theadTh = document.createElement("th");
    theadTh.innerHTML = nazevDruhySloupec;
    theadTr.appendChild(theadTh);
    thead.appendChild(theadTr);
    table.appendChild(thead);
    data = JSON.parse(xhttp.responseText);

    for (let i = 0; i < data.length; i++) {
        let tbodyTr = document.createElement("tr");
        let tbodyTd = document.createElement("td");
        tbodyTd.innerHTML = data[i][atrPrvni];
        tbodyTr.appendChild(tbodyTd);
        tbodyTd = document.createElement("td");
        tbodyTd.innerHTML = data[i][atrDruhy];
        tbodyTr.appendChild(tbodyTd);
        tbody.appendChild(tbodyTr);
    }
    table.appendChild(tbody);
    return table;
}

function getStatesAndCities() {
    let uri_states = "api/states";
    let uri_cities = "api/cities";

    let xhttp = new XMLHttpRequest();
    xhttp.onload = function () {
        if (xhttp.status === 200) {
            let nadpisStaty = document.getElementById("nadpisStaty");
            let table = vytvorTabulku("Název", "Zkratka", "name", "code", xhttp);
            nadpisStaty.parentNode.insertBefore(table, nadpisStaty.nextSibling);
        }
    }
    xhttp.open("GET", uri_states, true);
    xhttp.send();

    let xhttp2 = new XMLHttpRequest();
    xhttp2.onload = function () {
        if (xhttp2.status === 200) {
            let nadpisMesta = document.getElementById("nadpisMesta");
            let table = vytvorTabulku("Název", "Stát", "name", "region", xhttp2);
            nadpisMesta.parentNode.insertBefore(table, nadpisMesta.nextSibling);
        }
    }
    xhttp2.open("GET", uri_cities, true);
    xhttp2.send();
}

