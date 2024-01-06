const path2 = "https://zapolzachet.onrender.com/admin"
const path = "http://localhost:8080/admin";

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

function updateUsersBySearch() {
    var search = document.querySelector("#username-search");
    var usersTbody = document.querySelector(".users-tbody");
    fetch(path+'/api/users?'+search).then(res => res.json())
        .then(users => {
            users.forEach(user => {
                var tr = document.createElement("tr");
                var td = document.createElement("td");
                td.className = "text-nowrap align-middle";
                td.textContent = user.name;
                tr.appendChild(td);
                td.textContent = user.email;
                tr.appendChild(td);
                td.textContent = user.role;
                tr.appendChild(td);
                tr.appendChild(createRoleChooseForm());
                usersTbody.appendChild(tr);
            })
        })
        .catch(error => console.log(error));
}

function createRoleChooseForm(){
    var form = document.createElement("form");
    form.setAttribute("id", "form-submit-onchange");
    form.setAttribute("th:method", "patch");
    form.setAttribute("th:object", "${userForEdit}");
    form.setAttribute("th:action", "@{/admin/users/{id}/newRole(id=${user.getId()})}");
    var select = document.querySelector("select");
    select.setAttribute("onchange", "submitOnchange()");
    select.setAttribute("th:field", "${userForEdit.role}");
    var option = document.createElement("option");
    option.setAttribute("th:value", "${role}");
    option.setAttribute("th:each", "role : ${roles}");
    option.setAttribute("th:text", "${role}");
    select.appendChild(option);
    form.appendChild(select);
    return form;
}

// <tbody className="users-tbody">
// <tr th:each="user : ${users}">
//     <td th:text="${user.getName()}" className="text-nowrap align-middle"><span>Имя пользователя</span></td>
//     <td th:text="${user.getEmail()}" className="text-nowrap align-middle"><span>Почта</span></td>
//     <td th:text="${user.getRole()}" className="text-nowrap align-middle"><span>Роль</span></td>
//     <td className="text-center align-middle">
//         <form id="form-submit-onchange" th:method="patch" th:object="${userForEdit}"
//               th:action="@{/admin/users/{id}/newRole(id=${user.getId()})}">
//             <select onChange="submitOnchange()" th:field="${userForEdit.role}">
//                 <option th:selected="${role == user.getRole()}" th:value="${role}" th:each="role : ${roles}"
//                         th:text="${role}"></option>
//             </select>
//         </form>
//         <!--                                                <div class="btn-group align-top">-->
//         <!--                                                    <form th:method = "delete" th:action="@{/admin/groups/{id}/delete(id = ${group.getId()})}">-->
//         <!--                                                        <button class="btn btn-sm btn-outline-secondary badge" type="submit"><i class="fa fa-trash"></i></button>-->
//         <!--                                                    </form>-->
//         <!--                                                </div>-->
//     </td>
// </tr>
// </tbody>

function submitOnchange(select) {
    var parentForm = select.closest('.dynamic-form');
    if (parentForm) {
        console.log('Submitting form:', parentForm);
        parentForm.submit();
    }
}
function submitOnEnter(){
    if (event.key === 'Enter') {
        document.getElementById("form-submit-onenter").submit();
    }
}
