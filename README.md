# E-Shop

<details open=""><summary><h2>Описание</h2></summary>
  <div>
    <b>E-Shop</b> &ndash; учебный проект предоставляющий интернет магазин поддерживающий следующий функционал:
    <ul>
        <li>панель администрирования с возможностью оправления карточками товаров и пользователями интернет магазина;</li>
        <li>поиск товаров в интернет магазине с помощью фильтров;</li>
        <li>регистрация новых пользователей и утентификация существующих;</li>
        <li>формирование и управление корзиной товаров;</li>
        <li>формирование заказа на основании корзиной товаров;</li>
    </ul>
  </div>
<details><summary><h2>Структура модулей</h2></summary>
<table>
<tr>
<th>Директория</th>
<th>Описание</th>
<th>Стек технологий</th>
</tr>
<tr>
<td>picture-service</td>
<td>Модуль хранения изображений</td>
<td>Spring MVC</td>
</tr>
<tr>
<td>picture-service-app</td>
<td>Сервис хранения изображений</td>
<td>Spring Boot</td>
</tr>
<tr>
<td>shop-admin-app</td>
<td>Панель администрирования</td>
<td>Spring MVC, Spring Security, Thymeleaf</td>
</tr>
<tr>
<td>shop-backend-api-app</td>
<td>Web-сервер</td>
<td>REST API, Spring Security, RabbitMQ, WebSocket</td>
</tr>
<tr>
<td>shop-database</td>
<td>База данных интернет магазина</td>
<td>Spring Data, Hibernate, MySQL, Liquibase</td>
</tr>
<tr>
<td>shop-delivery-service</td>
<td>Сервис рассылки статуса заказа</td>
<td>RabbitMQ</td>
</tr>
<tr>
<td>shop-dto</td>
<td>Модуль DTO</td>
<td>ModelMapper</td>
</tr>
<tr>
<td>shop-frontend-app</td>
<td>Клиентская сторона пользовательского интерфейса</td>
<td>AngularJS</td>
</tr>
<tr>
<td>shop-session-storage</td>
<td>База данных сессии</td>
<td>Redis</td>
</tr>
<tr>
<td>shop-ui-tests</td>
<td>Модуль UI-тестов</td>
<td>Selenium, Cucumber</td>
</tr>
<tr>
<td>spring-cloud-config</td>
<td>Config сервер</td>
<td>Spring Cloud</td>
</tr>
<tr>
<td>spring-cloud-gateway</td>
<td>Шлюз микросервисов</td>
<td>Spring Cloud</td>
</tr>
<tr>
<td>spring-eureka</td>
<td>Discovery сервер</td>
<td>Spring Cloud</td>
</tr>
<tr>
<td>spring-integration-price</td>
<td>Сервис загрузки товаров из CSV файла</td>
<td>Spring Integration</td>
</tr>
</table>
</details>
