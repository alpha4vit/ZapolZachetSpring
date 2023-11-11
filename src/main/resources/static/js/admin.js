const path = "http://localhost:8080/admin"


function saveNewQuant(){
    var quant = document.querySelector("#quant").value;
    var subject_id = document.querySelector("#subject_id").value;
    var xhr = new XMLHttpRequest();
    xhr.open("post", path+`/subjects/${subject_id}/editQuantOfLabas`, true);
    xhr.setRequestHeader("content-type", 'application/json');
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.querySelector('.quant-content').innerHTML = `<p>Количество лабораторных работ: ${xhr.responseText}</p>`;
            document.querySelector(".quant-error").innerHTML = '';
        }
        else if (xhr.status.toString().charAt(0) == '4'){
            document.querySelector(".quant-error").innerHTML = `<p style="color: darkred": darkred">Введено некорректное значение</p>`
        }
    };


    xhr.send(JSON.stringify({
        newQuantOfLabas: quant
    }));
}

function selectTypeOfPage(){
    var type = document.querySelector(".type-selector").value;
    var subject_id = document.querySelector("#subject_id").value;
    var xhr = new XMLHttpRequest();
    xhr.open("post", path+`/subjects/${subject_id}/selectType`, true);
    xhr.setRequestHeader("content-type", 'application/json');
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.querySelector('.content').innerHTML = xhr.responseText;
        }
    };
    xhr.send(JSON.stringify({
        'type':type
    }));

}

function addNewGroupForSubject(){
    var subject_id = document.querySelector("#subject_id").value;
    var group_id = document.querySelector('.newgroup-selector').value;
    var xhr = new XMLHttpRequest();
    xhr.open("post", path+`/subjects/${subject_id}/addGroup`, true);
    xhr.setRequestHeader("content-type", 'application/json');
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            selectTypeOfPage();
        }
    };
    xhr.send(JSON.stringify({
        'group_id':group_id
    }));
}

function deleteGroupFromSubject(){
    var subject_id = document.querySelector("#subject_id").value;
    var group_id = document.querySelector("#group_id").value;
    var xhr = new XMLHttpRequest();
    xhr.open("post", path+`/subjects/${subject_id}/deleteGroup/${group_id}`, true);
    xhr.setRequestHeader("content-type", 'application/json');
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            selectTypeOfPage();
        }
    };
    xhr.send();
}

function saveNewLaba(){
    var subject_id = document.querySelector("#subject_id").value;
    var number = document.querySelector("#newlaba_number").value;
    var title = document.querySelector("#newlaba_title").value;
    var xhr = new XMLHttpRequest();
    xhr.open("post", path+`/subjects/${subject_id}/createLaba`, true);
    xhr.setRequestHeader("content-type", 'application/json');
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.querySelector('.quant-content').innerHTML = `<p>Количество лабораторных работ: ${xhr.responseText}</p>`;
            selectTypeOfPage();
        }
        else if (xhr.status === 400){
            document.querySelector("#number-error").innerHTML = "<p>Введено некорректное значение</p>";
        }
        else if (xhr.status === 404){
            document.querySelector("#number-error").innerHTML = '<p>ЛР с данным номером уже существует</p>';
        }
    };
    xhr.send(JSON.stringify({
        newLabaNum:number,
        newLabaTitle:title
    }));

}

function deleteLaba(){
    var subject_id = document.querySelector("#subject_id").value;
    var laba_id = document.querySelector("#laba_id").value;
    console.log(laba_id);
    var xhr = new XMLHttpRequest();
    xhr.open("post", path+`/subjects/${subject_id}/deleteLaba`, true);
    xhr.setRequestHeader("content-type", 'application/json');
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.querySelector('.content').innerHTML = xhr.responseText;
        }
    };
    xhr.send(JSON.stringify({
        laba_id:laba_id
    }));
}

function submitOnchange() {
    document.getElementById("form-submit-onchange").submit();
}

function submitOnEnter(){
    if (event.key === 'Enter') {
        document.getElementById("form-submit-onenter").submit();
    }
}
