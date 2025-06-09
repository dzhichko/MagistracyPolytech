async function checklogin() {

    const username = document.getElementById('username-input').value;
    const password = document.getElementById('password-input').value;

    // Базовая проверка
    if (!username || !password) {

        message('Все поля обязательны для заполнения');
        return;
    }

    // Дополнительные проверки
    if (username.length < 4) {

        message('Логин должен содержать минимум 4 символа');
        return;
    }

    if (password.length < 6) {

        message('Пароль должен быть не менее 6 символов');
        return;
    }

    await login(username, password);
}

async function login(username,password) {

    try{
        const response = await fetch('http://localhost:2222/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password }),
        });

        if (!response.ok) {
            const errorMessage = await getErrorMessage(response);
            message(errorMessage);
            throw new Error(errorMessage);
        }

        const { token } = await response.json(); // Сервер возвращает JWT
        localStorage.setItem('jwtToken', token); // Сохраняем токен

        console.log('Ответ сервера:', token);
        message('Вход успешен');
        if (window.opener && !window.opener.closed) {
            window.opener.location.reload();
            window.close();

        }
        else{
            window.location.href='../index.html';
        }
        return token;
    }
    catch (error) {
        console.error('Ошибка входа:',error);
        message('Ошибка подключения к серверу')
    }
}

async function getErrorMessage(response){
    switch (response.status){
        case 401: return 'Неверные учетные данные';
        case 404: return 'Пользователь не найден';
        case 409: return 'Проверьте корректность введенных данных';
        case 500: return 'Ошибка на сервере, попробуйте позже';
    }
}

function  message(text){
    const b =document.getElementById("login-message-span");


    if (text === "Вход успешен"){
        b.style.color=("#21a649");
    }
    b.textContent = text;
}

