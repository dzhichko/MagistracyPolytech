async function checkRegistration() {

    const username = document.getElementById('username-input').value;
    const password = document.getElementById('password-input').value;
    const password2 = document.getElementById('password-check-input').value;
    const email = document.getElementById('mail-input').value;

    // Базовая проверка
    if (!username || !password || !password2 || !email) {
        message('Все поля обязательны для заполнения!');
        return;
    }

    if (password !== password2) {
        message('Пароли не совпадают');
        return;
    }

    if (username.length < 4) {
        message('Логин должен содержать минимум 4 символа');
        return;
    }

    if (password.length < 6) {
        message('Пароль должен быть не менее 6 символов');
        return;
    }

    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
        message('Некорректный email');
        return;
    }

    await registration(username, password,email);

}


async function registration(username,password,email) {

  try {

      const response = await fetch('http://localhost:8080/register', {
          method: 'POST',
          headers: {'Content-Type': 'application/json'},
          body: JSON.stringify({username, email, password}),
      });

      if (!response.ok) {
          const errorMessage = await getErrorMessage(response);
          message(errorMessage);
          throw new Error(errorMessage);

      }

      const data = await response.json();
      console.log('Пользователь успешно зарегистрирован:', data);
      window.location.href = '../index.html';
      return data

  }catch (error){

      console.error('Ошибка входа:',error);
      message('Ошибка подключения к серверу')

  }
}

function  message(text){
    const b =document.getElementById("registration-message-span");
    if (text === "Регистрация прошла успешно, авторизация произошла автоматически"){
        b.style.color=("#21a649");
    }
    b.textContent = text;
}

async function getErrorMessage(response){
    switch(response.status){
        case 400: return 'Некорректные данные';
        case 401: return 'Unauthorized';
        case 404: return 'Пользователь не найден';
        case 409: return 'Пользователь уже существует';
        case 500: return 'Ошибка на сервере, попробуйте позже';
    }
}