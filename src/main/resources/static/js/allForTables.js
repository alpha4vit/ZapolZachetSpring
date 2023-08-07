const path = "http://localhost:8080"



// get groups
var groupList = [];
const data = fetch(path+"/api/groups").then(result => result.json()).then(data => data.forEach(
    (el) => {
        groupList.push(el);
    }
));

function selection(type){
    var group_id = document.querySelector(".group-select").value;
    var subject_id = document.querySelector(".subject-select").value;
    var surnameSearch;
    var labaNumFilter;
    if (type === 'teacher-filter'){
        surnameSearch = document.querySelector("#surnameFilter").value;
        labaNumFilter = document.querySelector("#labaNumFilter").value;
    }
    var xhr = new XMLHttpRequest();
    var url = "";
    switch (type){
        case 'teacher-filter':
        case 'teacher': url="/teacher/group/select"; break;
        case 'user': url="/choosegroup"; break;
    }
    xhr.open('post', path+url, true)
    xhr.setRequestHeader('Content-Type', 'application/json');
    var data = JSON.stringify({
        group_id:group_id,
        subject_id:subject_id,
        surnameSearch:surnameSearch,
        labaNumFilter:labaNumFilter
    });
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            if (type === 'teacher-filter'){
                document.querySelector('.data').innerHTML = xhr.responseText;
            }
            else
                document.getElementById('table').innerHTML = xhr.responseText;
        }
    };

    xhr.send(data);
}

function addNewZachet(){
    var xhr = new XMLHttpRequest();

    xhr.open("post", path+"/teacher/group/newZachet", true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    var group_id = document.querySelector(".group-select").value;
    var subject_id = document.querySelector(".subject-select").value;
    var student_id = document.querySelector("#student_idZachet").value;
    var newZachetLabaId = document.querySelector("#labaIdZachet").value;
    var value = document.querySelector("#valueZachet").value;
    var surnameSearch = document.querySelector("#surnameFilter").value;
    var labaNumFilter = document.querySelector("#labaNumFilter").value;

    var data = JSON.stringify({
        group_id:group_id,
        subject_id:subject_id,
        student_id:student_id,
        newZachetLabaId:newZachetLabaId,
        value:value,
        surnameSearch:surnameSearch,
        labaNumFilter:labaNumFilter
    });
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.querySelector('.data').innerHTML = xhr.responseText;
        }
    };
    xhr.send(data);
}


var app = new Vue({
    el: '.form-select',
    data: {
        groups: groupList
    },
});


var prevValue = -1;
var updateSubjectSelect = function (){
    var groupSelect = document.querySelector(".group-select");
    var value = groupSelect.value;
    if (prevValue !== value) {
        prevValue = value;
        var subjectSelect = document.querySelector(".subject-select");
        while (subjectSelect.firstChild) {
            subjectSelect.removeChild(subjectSelect.firstChild);
        }
        fetch(path+'/api/groups').then(res => res.json())
            .then(groups => {
                groups.forEach(group => {
                    if (group.id == value) {
                        group.subjectsDTO.forEach(subject => {
                            var option = document.createElement("option");
                            option.value = subject.id;
                            option.textContent = subject.title;
                            subjectSelect.appendChild(option);
                        })
                    }
                })
            })
            .catch(error => console.log(error));
    }
}



