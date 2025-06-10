async function checklogin() {

    const username = document.getElementById('username-input').value;
    const password = document.getElementById('password-input').value;

    // Базовая проверка
    if (!username || !password) {

        message('Все поля обязательны для заполнения!');
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


    const response = await fetch('http://localhost:8080/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password }),
    });

    if (!response.ok) throw new Error('Ошибка входа');

    if (response.status === 401) {
        message('Неверные учетные данные');
        throw new Error('Неверные учетные данные');

    }

    const { token } = await response.json(); // Сервер возвращает JWT
    localStorage.setItem('jwtToken', token); // Сохраняем токен

    console.log('Ответ сервера:', token);

    message('Вход успешен');
    window.location.href = '../index.html';
    return token;




}




function  message(text){
    const b =document.getElementById("login-message-span");


    if (text === "Вход успешен"){
        b.style.color=("#21a649");
    }
    b.textContent = text;
}

