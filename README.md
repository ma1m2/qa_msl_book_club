Настраивае UI часть

1) Скачали репозиторий в IDEA https://github.com/qa-guru/book-club.git


2) Зашли в терминал IDEA и выполниил

   ```java
   docker compose -f docker-compose.yml -f docker-compose.local.yml up -d --build
   ```


3) Проект подгрузился в докер


4) Проверяем что регистрация не работает на ui -> ОШИБКА ( клубы не найдены)


5) Открыаем папку со скаченным репозиторием

   - находим папку frontend -> Dockerfile

   Открываем его через текстовый редактор

   Вставляем следующее содержимое

   ```java
   FROM node:22 AS build-stage
   WORKDIR /app
   
   ENV CI=true
   RUN corepack enable && corepack prepare pnpm@latest --activate
   
   COPY package.json pnpm-lock.yaml .npmrc* ./
   RUN pnpm install --frozen-lockfile --prefer-offline --ignore-scripts
   RUN pnpm approve-builds
   
   COPY . .
   
   # ОБЯЗАТЕЛЬНО ОБЪЯВЛЯЕМ АРГУМЕНТ, КОТОРЫЙ ПЕРЕДАЕМ ИЗ COMPOSE
   # Благодаря этому Vite увидит правильный URL во время pnpm run build
   ARG VITE_API_BASE_URL
   ENV VITE_API_BASE_URL=$VITE_API_BASE_URL
   
   RUN pnpm run build
   
   # Production stage
   FROM nginx:stable-alpine AS production-stage
   COPY --from=build-stage /app/dist /usr/share/nginx/html
   COPY nginx.conf /etc/nginx/conf.d/default.conf
   
   EXPOSE 80
   CMD ["nginx", "-g", "daemon off;"]
   ```

   Не забываем сохранить!!!


6) Ищем и открываем файл -> Docker-copose.local.yml

   меняем его содержимое на следующее

   ```java
   # Local development
   services:
     frontend:
       build:
         context: ./frontend
         dockerfile: ./Dockerfile
         args:
           VITE_API_BASE_URL: /   # <--- МЕНЯЕМ ТУТ! Просто слэш
       ports:
         - "8100:80"
       networks:
         - bookclub
       depends_on:
         - backend
   
     backend:
       ports:
         - "8000:8000"
       environment:
         - CORS_ORIGINS=http://localhost:8100
       networks:
         - bookclub
   
     postgres:
       ports:
         - "5432:5432"
       networks:
         - bookclub
   ```

   Не забываем сохранить!!!


7) Открываем терминал и переходим в корень проекта мой пример ->

   cd /Users/motec/IdeaProjects/book-club


8. Выполняем поочередно 2 команды на пересобрать проект

   ```java
   docker compose -f docker-compose.yml -f docker-compose.local.yml build frontend --no-cache
   docker compose -f docker-compose.yml -f docker-compose.local.yml up -d
   ```
   
9. Либо уже сразу должны открыться клубы

   Либо после того как контейнеры поднимутся, открывай `http://localhost:8100`, делай жесткую перезагрузку страницы через `Cmd + Shift + R` (чтобы браузер не вздумал взять старый закешированный скрипт с CORS-ошибкой) 


10. Для очистки базы:
* Все удаляла: контейнеры, вольюм, имеджы и заново собирала
```java
docker compose -f docker-compose.yml -f docker-compose.local.yml up -d --build
```




