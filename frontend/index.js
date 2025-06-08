
function isTokenValid(token) {
    try {
        const payload = JSON.parse(atob(token.split('.')[1])); // Декодируем payload
        const currentTime = Math.floor(Date.now() / 1000); // Текущее время в секундах
        return payload.exp > currentTime; // true если токен не истёк
    } catch (e) {
        return false; // Невалидный токен
    }
}

function searchClick() {
    const searchValue = document.getElementById('searchInput').value.trim().toUpperCase();
    const contentPanels = document.querySelectorAll('.course-card');
    let count = 0;

    contentPanels.forEach(panel => {

        if (!searchValue) return;
        const linkElement = panel.querySelector('.main-information a');
        if (!linkElement) return;

        if (!linkElement.dataset.originalHtml) {
            linkElement.dataset.originalHtml = linkElement.innerHTML;
        }

        const courseNumber = linkElement.querySelector('.number-of-course')?.textContent.trim().toUpperCase() || '';
        const courseName = linkElement.textContent.replace(courseNumber, '').trim().toUpperCase();

        const matchesName = courseName.includes(searchValue);
        const matchesNumber = courseNumber.includes(searchValue);

        if (matchesNumber || matchesName) {
            panel.style.display = 'block';
            count++;
        } else {
            panel.style.display = 'none';
        }
    });
    updateCount(count); // Обновляем счетчик один раз после цикла
}


function updateCount(count){
    document.querySelector('.search-bar .resultOfSearch .count').textContent = count;
}

function clearInput() {

    const searchValue = document.getElementById('searchInput')
    searchValue.value = '';

    document.querySelector('.search-bar .resultOfSearch .count').textContent = '0';

    document.querySelectorAll('.course-card').forEach(panel => {
        panel.style.display = 'block';
        const linkElement = panel.querySelector('.main-information a');
        if (linkElement && linkElement.dataset.originalHtml) {
            linkElement.innerHTML = linkElement.dataset.originalHtml;
        }

    });

}

document.addEventListener('DOMContentLoaded', function() {
    checkAutorization();
});

async function checkAutorization(){

    if (localStorage.getItem('jwtToken'))
    {
        curToken= localStorage.getItem('jwtToken');
        if (isTokenValid(curToken)){
            setExitButton();
            await updateFavCoursesList();
            updateCourseButtons();
            document.body.classList.remove('unauth');
            document.body.classList.add('auth');
        }
        else {
            exit()
        }
    }
    else {
        document.body.classList.remove('auth');
        document.body.classList.add('unauth');
    }
}

function setExitButton() {

    const element = document.getElementById('personal-account-id');
    const elementForDel = element.childNodes[0];
    elementForDel.remove();
    if( element.childNodes.length===0){
        const newElement = document.createElement("button");
        newElement.id='new-button-id';
        newElement.onclick = exit;
        newElement.textContent='Выход'
        element.appendChild(newElement);
        const panelBar = document.getElementsByClassName('top-right-section')[0];
        const newTextMessage = document.createElement('span');
        newTextMessage.id='info-enter-mes';
        newTextMessage.textContent='Вход выполнен. Добавление в избранное доступно';
        panelBar.append(newTextMessage);

    }
    else (setExitButton())

}


function  exit() {
    console.log('Кнопка выхода нажата');
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('favCoursesList');


    const element = document.getElementById('personal-account-id');
    const elementForDel = element.childNodes[0];
    elementForDel.remove();
    document.getElementById('info-enter-mes')?.remove();

    if (element.childNodes.length === 0) {
        const newElement = document.createElement("a");
        newElement.id = 'login_button';
        newElement.className = 'cabinet';
        newElement.textContent = 'Вход';
        newElement.href = "/auth/login.html";
        newElement.target = "_blank";
        element.appendChild(newElement);
    }

    location.reload();
}

async function updateFavCoursesList(){
    try{

        const jwtToken = localStorage.getItem('jwtToken');

        if (!jwtToken) {
            // massage('Требуется авторизация. Пожалуйста, войдите в систему.');
            console.error('Требуется авторизация. Пожалуйста, войдите в систему.');
        }

        const response =await fetch( '/programs/favourite/ ',{

            method: 'GET',
            headers:{'Content-Type': 'application/json',
                'Authorization': `Bearer ${jwtToken}`},

        });

        const favCourse = await response.json();
        localStorage.setItem('favCoursesList',JSON.stringify(favCourse));
        console.log(favCourse);
        return(favCourse);

    }
    catch (error){

    }
}

async function toggleAddButton(idProgram)
{

    const jwtToken = localStorage.getItem('jwtToken');

    // Проверка наличия токена
    if (!jwtToken) {
        massage('Требуется авторизация. Пожалуйста, войдите в систему', idProgram);
        return;
    }

    const courseAlreadyAdded = courseAlreadyAdd(idProgram);

    try {
        if(!courseAlreadyAdded){
            await addFavCourses(idProgram);
        }
        else {
            await deleteFavCourse(idProgram);
        }
    }
    catch (erorr){}
}

async function deleteFavCourse(idProgram){


    console.log('Отправляем ID программы:', idProgram);
    const jwtToken = localStorage.getItem('jwtToken');

    // Проверка наличия токена
    if (!jwtToken) {
        massage('Требуется авторизация. Пожалуйста, войдите в систему', idProgram);
        return;
    }
    const response = await fetch(`/programs/favourite/${idProgram}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${jwtToken}`
        },
        body: JSON.stringify({ programId: idProgram }),
    });

    if (response) {
        if (response.status === 200) {
            massage('Программа удалена из избранного', idProgram);
            const text = 'Добавить в избранное';
            setButtonText(text,idProgram);
            await updateFavCoursesList();
            console.log('Программа удалена из избранного (200)');
        }
    }
}

async function addFavCourses(idProgram) {
    try {
        console.log('Отправляем ID программы:', idProgram);
        const jwtToken = localStorage.getItem('jwtToken');

        // Проверка наличия токена
        if (!jwtToken) {
            massage('Требуется авторизация. Пожалуйста, войдите в систему', idProgram);
            return;
        }

        if (courseAlreadyAdd(idProgram)) {
            massage('Программа уже добавлена',idProgram);
            return;

        }

        const response = await fetch(`/programs/favourite/${idProgram}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${jwtToken}`
            },
            body: JSON.stringify({ programId: idProgram }),
        });

        if (response) {
            if (response.status === 200) {
                 massage('Программа добавлена в избранное =)', idProgram);
                 setDeleteText(idProgram);
                 await updateFavCoursesList();
                 console.log('Программа добавлена в избранное (200)');
            }
        }
        else{

        }

    } catch (error) {
        console.error('Ошибка:', error);
        // Можно добавить пользовательское уведомление
        alert(`Ошибка: ${error.message}`);
    }
}

function setDeleteText(idProgram){
    if(!courseAlreadyAdd(idProgram)){
        console.error('новый курс');
        setButtonText('Удалить из избранного',idProgram);
    }
}

function setButtonText(text,idProgram){
    const curCourseCard = document.getElementById(idProgram);
    const but = curCourseCard.querySelector('.insert-button');
    but.textContent = text;
}

function  courseAlreadyAdd(idProgram){
    const addedCourse = localStorage.getItem('favCoursesList');
    if (!addedCourse) return false;
    const coursesAr = JSON.parse(addedCourse);
    console.log(coursesAr);
    const status = coursesAr.some(coursesAr => coursesAr.code ===idProgram);
    console.log(status);
    return status;
}

function massage(text,idProgram){
    const curCourseCard = document.getElementById(idProgram);
    const span = curCourseCard.querySelector('.span-for-mess');
    span.textContent='';
    span.insertAdjacentText("afterbegin",text);
}

async function getCoursesList(){

    // const jwtToken = localStorage.getItem('jwtToken');
    const response = await  fetch(`/programs`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
        },

    });

    if(!response.ok) throw new Error('');

    const data = await response.json();
    localStorage.setItem('list',JSON.stringify(data));
    console.log(data);
    await adder();
    return data;
}

function createCoursePanelEl(course){
    return `
        <div class="course-card" id="${course.code}">
        
            <div class="panel-head">
                <img src="img/InstituteLogo/ФМИТ.png" alt="ФМИТ ЛОГОТИП">
                <div class="panel-head-text">
                <span class="text"> ФМИТ <svg class="point" width="5" height="5" viewBox="0 0 5 5" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <circle cx="2.5" cy="2.5" r="2.5" fill="#184180"/>
                </svg>
                    ${course.institution.toUpperCase()}
                  </span>
                </div>
            </div>
            <div class="content-block">
                <div class="main-information">
                    <a href="https://dep.spbstu.ru/edu/${course.code.split('_')[0]}/${course.code}/" id="${course.name}" target="_blank"  >  ${course.name} <span class="number-of-course"> ${course.code.replace('_','-')} </span></a>
                </div>
                <div class="other-information">
                    <div class="content" >
                        <p> ОЧНО </p>
                        <i class="fa-solid fa-school fa-2x"></i>

                    </div>
                    <div class="content">
                        <p> БЮДЖЕТ <br>
                            ${course.budgetPlace} </p>
                    </div>
                    <div class="content">
                        <p> КОНТРАКТ <br>
                            ${course.contractPlace} </p>
                    </div>
                    <div class="content">
                        <p> 2 года </p>
                        <i class="fa-solid fa-calendar-days fa-2x"></i>
                    </div>
                </div>
                <div class=" button-container">
                    <div class="button-for-add-fav"> 
                        <button class="insert-button"  onclick="toggleAddButton(this.parentElement.parentElement.parentElement.parentElement.id)"> Добавить в избранное </button> 
                    </div>
                    <div class="download-information">
                        <button class="download-button" onclick="downloadFile(this.parentElement.parentElement.parentElement.parentElement.id)"> <i class="fa-solid fa-file-arrow-down"></i> </button>
                    </div>
                </div>    
                <span class="span-for-mess"> </span>
            </div>
        </div>
  `;

}
async function adder() {

    const data = localStorage.getItem('list');

    let courses;
    try {
        courses = JSON.parse(data);
    } catch (e) {
        console.error('Ошибка парсинга данных:', e);
        return;
    }

    if (!Array.isArray(courses)) {
        console.error('Данные не являются массивом');
        return;
    }

    const coursesList = document.querySelector('.courses-list');
    coursesList.innerHTML = courses.map(course => createCoursePanelEl(course)).join('');

    console.log(`Загружено ${courses.length} курсов`);

    updateCourseButtons();
}

document.addEventListener('DOMContentLoaded', async function () {
    await getCoursesList();

});





async function downloadFile(elementId) {
    try {
        // 1. Показываем пользователю, что началась загрузка
        const downloadWindow = window.open('', '_blank');
        if (!downloadWindow) {
            throw new Error('Браузер заблокировал открытие окна. Разрешите всплывающие окна для этого сайта.');
        }
        downloadWindow.document.write('<p>Загрузка PDF...</p>');

        // 2. Запрашиваем файл
        const response = await fetch(`/programs/download/${elementId}`, {
            method: 'GET',
            headers: { 'Accept': 'application/pdf' },
        });

        if (!response.ok) {
            const errorText = await response.text();
            downloadWindow.document.write(`<p style="color:red">Ошибка: ${errorText || 'Unknown error'}</p>`);
            throw new Error(`HTTP ${response.status}`);
        }

        // 3. Получаем и отображаем PDF
        const blob = await response.blob();
        const pdfUrl = URL.createObjectURL(blob);

        // Создаем iframe во всплывающем окне для PDF
        downloadWindow.document.body.innerHTML = `
            <iframe 
                src="${pdfUrl}" 
                width="100%" 
                height="100%" 
                style="border:none;"
                title="PDF Viewer"
            ></iframe>
        `;

        // 4. Очистка (после закрытия окна)
        downloadWindow.onbeforeunload = () => {
            URL.revokeObjectURL(pdfUrl);
        };

    } catch (error) {
        console.error('Download error:', error);
        throw error;
    }
}

function updateCourseButtons() {
    // Получаем список избранных курсов из localStorage

    const favCourses = localStorage.getItem('favCoursesList');
    if (!favCourses) return;
    const favCoursesArr = JSON.parse(favCourses);
    // Проходим по всем карточкам курсов (предполагаем, что у них есть data-атрибут `data-course-id`)
    const courseCards = document.querySelectorAll('.course-card');
    courseCards.forEach(card => {
        const courseId = card.id;
        // Если курс в избранном, меняем текст кнопки
        if (courseAlreadyAdd(card.id)) {
            const text = 'Удалить из избранного';
            setButtonText(text,courseId);

        } else {
            const text = 'Добавить в избранное';
            setButtonText(text,courseId)
        }
    });
}