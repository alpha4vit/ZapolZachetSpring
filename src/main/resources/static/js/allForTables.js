const path2 = "https://zapolzachet.onrender.com"
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
        surname_search:surnameSearch,
        laba_num_filter:labaNumFilter
    });
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            if (type === 'teacher-filter'){
                document.querySelector('.data').innerHTML = xhr.responseText;
                updateTeacherPage();
            }
            else {
                document.getElementById('table').innerHTML = xhr.responseText;
                updateLabaSelectZachetForm();
            }
        }
    };

    xhr.send(data);
}

function updateTeacherPage(){
    updateSubjectSelect();
    updateStudentSelectZachetForm();
    updateLabaSelectZachetForm();
}

function addNewZachet(){
    var xhr = new XMLHttpRequest();

    xhr.open("post", path+"/teacher/zachets/new", true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    var group_id = document.querySelector(".group-select").value;
    var subject_id = document.querySelector(".subject-select").value;
    var student_id = document.querySelector(".student-select").value;
    var newZachetLabaId = document.querySelector(".laba-select").value;
    var value = document.querySelector("#valueZachet").value;
    var surnameSearch = document.querySelector("#surnameFilter").value;
    var labaNumFilter = document.querySelector("#labaNumFilter").value;

    var data = JSON.stringify({
        group_id:group_id,
        subject_id:subject_id,
        student_id:student_id,
        new_zachet_laba_id:newZachetLabaId,
        value:value,
        surname_search:surnameSearch,
        laba_num_filter:labaNumFilter
    });
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.querySelector('.data').innerHTML = xhr.responseText;
            updateLabaSelectZachetForm();
        }
    };
    xhr.send(data);
}

var prevValue = -1;
function updateSubjectSelect(){
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
                        group.subjects.forEach(subject => {
                            var option = document.createElement("option");
                            option.value = subject.id;
                            option.textContent = subject.title;
                            subjectSelect.appendChild(option);
                        })
                    }
                })
            })
            .then(updateStudentSelectZachetForm)
            .catch(error => console.log(error));
    }
}

function updateStudentSelectZachetForm(){
    var group_id = document.querySelector(".group-select").value;
    var studentSelect = document.querySelector(".student-select")
    while(studentSelect.firstChild){
        studentSelect.removeChild(studentSelect.firstChild)
    }
    fetch(path+'/api/students/'+group_id).then(res => res.json())
        .then(students => {
            students.forEach(student => {
                var option = document.createElement("option");
                option.setAttribute("value", student.id);
                option.textContent = student.fio;
                studentSelect.appendChild(option);
            })
        })
        .then(updateLabaSelectZachetForm)
        .catch(error => console.log(error));

}

function updateLabaSelectZachetForm(){
    var subject_id = document.querySelector(".subject-select").value;
    var student_id = document.querySelector(".student-select").value;
    var labaSelect = document.querySelector(".laba-select");
    while(labaSelect.firstChild){
        labaSelect.removeChild(labaSelect.firstChild)
    }
    fetch(path+'/api/subjects/'+subject_id+"/labas/"+student_id).then(res => res.json())
        .then(labas => {
            labas.forEach(laba => {
                console.log(laba);
                var option = document.createElement("option");
                option.setAttribute("value", laba.id);
                option.textContent = laba.number+") "+laba.title;
                labaSelect.appendChild(option);
            })
        })
        .catch(error => console.log(error));
}

function sendExcelToEmail(){
    var xhr = new XMLHttpRequest();
    xhr.open("post", path+"/teacher/group/send/excel", true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    var subject_id = document.querySelector(".subject-select").value;
    var group_id = document.querySelector(".group-select").value;
    var data = JSON.stringify({
        subject_id:subject_id,
        group_id:group_id
    })
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.querySelector('.data').innerHTML = xhr.responseText;
            updateLabaSelectZachetForm();
        }
    };
    xhr.send(data);
}



