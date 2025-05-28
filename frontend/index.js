
function searchClick() {
    const searchValue = document.getElementById('searchInput').value.trim().toUpperCase();
    const contentPanels = document.querySelectorAll('.course-panel');
    let count = 0;

    contentPanels.forEach(panel => {
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

    document.querySelector('.search-bar .resultOfSearch .count').textContent = '14';

    document.querySelectorAll('.course-panel').forEach(panel => {
        panel.style.display = 'block';
        const linkElement = panel.querySelector('.main-information a');
        if (linkElement && linkElement.dataset.originalHtml) {
            linkElement.innerHTML = linkElement.dataset.originalHtml;
        }

    });

}

document.addEventListener('DOMContentLoaded', function() {
    updateButton();
});

window.onload = function() {
    // Выполнится после полной загрузки всех ресурсов
    console.log('Страница полностью загружена');
};

function updateButton() {

    const element = document.getElementById('personal-account-id');
    const elementForDel = element.childNodes[0];
    elementForDel.remove();
    if( element.childNodes.length===0){
        const newElement = document.createElement("button");
        newElement.id='new-button-id';
        newElement.onclick = exit;
        newElement.textContent='Выход'
        element.appendChild(newElement);

        creationChoseButton();

    }
    else (updateButton())

}
async function addFavCourses() {
    try {
        const idProgram = '01.04.02_01';
        console.log('Отправляем ID программы:', idProgram);
        const jwtToken = localStorage.getItem('jwtToken');

        // Проверка наличия токена
        if (!jwtToken) {
            throw new Error('Требуется авторизация. Пожалуйста, войдите в систему.');
        }

        const response = await fetch(`http://localhost:2222/programs/favourite/${idProgram}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${jwtToken}`
            },
            body: JSON.stringify({ programId: idProgram }),
        });

        if (!response)

        if (response.status ===200){
            console.log('Программа добавлена в избранное (200)');
            return { success: true }; // Успех, но без JSON
        }


    } catch (error) {
        console.error('Ошибка:', error);
        // Можно добавить пользовательское уведомление
        alert(`Ошибка: ${error.message}`);
        throw error;
    }
}





function  exit() {
    console.log('Кнопка выхода нажата');
    localStorage.clear();


    const element = document.getElementById('personal-account-id');
    const elementForDel = element.childNodes[0];
    elementForDel.remove();

    const topBar=document.getElementsByClassName('top-right-section')[0];
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
}

function creationChoseButton(){
    const panelBar = document.getElementsByClassName('top-right-section')[0];



    const newTextMessage = document.createElement('span');
    newTextMessage.id='info-enter-mes';
    newTextMessage.textContent='Вход выполнен. Добавление в избранное доступно';
    panelBar.append(newTextMessage);
    // const newDivStructure=document.createElement("div");
    // newDivStructure.id='div-insert-button';
    // newDivStructure.className='personal-account';
    // panelBar.append(newDivStructure);
    //
    // const newInsertButton = document.createElement("button");
    // newInsertButton.id='insert-button-id';
    // newInsertButton.onclick = addFavCourses;
    // newInsertButton.textContent='Выбрать любимые дисциплины';
    // newDivStructure.append(newInsertButton);

}

async function getCoursesList(){

    // const jwtToken = localStorage.getItem('jwtToken');
    const response = await  fetch(`http://localhost:2222/programs`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            // 'Authorization': `Bearer ${jwtToken}`,
        },

    });

    if(!response.ok) throw new Error('');

    const data = await response.json();
    localStorage.setItem(data,'list');
    console.log(data);
    return data;




}




