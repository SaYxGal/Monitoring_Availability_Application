# Инструкция по запуску приложения без docker

1. В файле application.properties для основного приложения надо указать информацию о PostgreSQL
   (пустая бд с указанным в этом файле названием должна быть создана вручную).
   Путь к файлу: /application/src/main/resources
2. Находясь в cmd в папке проекта(application) ввести ./gradlew build. Аналогичную операцию сделать для приложения-компаньона
3. Вернуться в корневую папку. Перейти по пути /application/build/libs
4. java -jar application-0.0.1-SNAPSHOT.jar
5. Действия аналогичные пунктам 3, 4 выполнить с приложением-компаньоном

# Инструкция по запуску с помощью docker
1. Необходимо заранее сгенерировать jar файлы (т.е выполнить команду ./gradlew build в папках каждого проекта(application и subapplication))
2. В корневой папке (где хранится docker-compose.yml) выполнить команду docker-compose up  

## URL для взаимодействия с приложением: http://localhost:8000/swagger-ui/index.html  
# Советы по работе 
Все эндпоинты (кроме api/users/signIn) недоступны неавторизованным пользователям (без токена). Войти можно через эндпоинт api/users/signIn. Имя пользователя и пароль - admin. Приложение отдаст токен, который надо вставить, нажав на кнопку Authorize, находящуюся в правом углу перед всеми эндпоинтами. Путь до эндпоинта компаньона для правила - "http://localhost:8080/get"  
P.S Для проверки правила на компаньоне при использовании докера в URL вместо localhost надо указывать название контейнера приложения-компаньона из docker-compose файла. "http://subapp:8080/get".  
Было проверено на JDK 17 (coretto) и Docker desktop для windows 11.  
Токен для бота телеграмм уже есть в проекте. При необходимости его можно поменять в файле application.properties
