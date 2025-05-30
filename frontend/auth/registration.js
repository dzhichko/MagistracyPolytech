async function checkRegistration() {

    const username = document.getElementById('username-input').value;
    const password = document.getElementById('password-input').value;
    const password2 = document.getElementById('password-check-input').value;
    const email = document.getElementById('mail-input').value;

    // Базовая проверка
    if (!username || !password || !password2 || !email) {
        massage('Все поля обязательны для заполнения!');
        return;
    }

    if (password !== password2) {
        massage('Пароли не совпадают');
        return;
    }

    if (username.length < 4) {
        massage('Логин должен содержать минимум 4 символа');
        return;
    }

    if (password.length < 6) {
        massage('Пароль должен быть не менее 6 символов');
        return;
    }

    await registration(username, password,email);

}


async function registration(username,password,email) {

  try {

      const response = await fetch('http://localhost:2222/register', {
          method: 'POST',
          headers: {'Content-Type': 'application/json'},
          body: JSON.stringify({username, email, password}),
      });

      if (!response.ok) {
          const errorData = await response.json(); // Читаем тело ошибки
          throw new Error(errorData.message || 'Ошибка регистрации');
      }

      if (response.status === 409) {
          throw new Error('Пользователь с таким именем уже существует');
      }

      const data = await response.json();
      console.log('Пользователь успешно зарегистрирован:', data);
      window.location.href = '../index.html';
      return data

  }catch (error){
      if (error.message.includes('User already exist')) {
          massage('Это имя пользователя уже занято. Попробуйте другое.');
      } else {
          massage(error.message || 'Ошибка при регистрации');
      }

  }
}

function  massage(text){
    const b =document.getElementById(" massage-span");
    if (text === "Регистрация прошла успешно, авторизация произошла автоматически"){
        b.style.color=("#21a649");
    }
    b.textContent = text;
}