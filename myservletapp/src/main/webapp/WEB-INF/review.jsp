<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Sega Mega Drive — Википедия</title>

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">

    <link rel="stylesheet" type="text/css" href="styles.css">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>

</head>
<body>



<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="#">
        <img src="" width="30" height="30" alt="">
    </a>
    <a class="navbar-brand" href="#"><b>Title</b></a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <div class="dropdown open">
                    <button class="btn dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        Категории
                    </button>
                    <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                        <a class="dropdown-item" href="#">Action</a>
                        <a class="dropdown-item" href="#">Another action</a>
                        <a class="dropdown-item" href="#">Something else here</a>
                    </div>
                </div>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#">Участники</a>
            </li>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#">Создать аккаунт</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#">Войти</a>
            </li>
        </ul>
        <form class="form-inline my-2 my-lg-0">
            <input class="form-control mr-sm-2" type="search" placeholder="Type here to search" aria-label="Search">
            <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
        </form>
    </div>
</nav>


<header class="shadowpanel" style="heigt: 30px;">
    <div class="cmasthead">
        <div class="container h-100">
            <div class="row h-100 align-items-center">
                <div class="col-12 text-center">
                    <h1 class="font-weight-light">Категория</h1>
                </div>
            </div>
        </div>
    </div>


</header>

<div class='hover-window' id='hover-window'>
    <input class="contents" type="text" name="product" placeholder="Продукт" style="width: 200px; margin-top: 50px;"><br>
    <input class="contents" type="text" name="product" placeholder="Оценка 0-5" style="width: 200px; margin-top: 5px;"><br>
    <textarea class="contents" type="text" name="desc"  placeholder="Отзыв" style="width: 95%; height: 45%; margin-top: 15px;">
</textarea>
    <div>
        <button>Отправить</button>
    </div>
</div>


<div id="addButton" class="fixed_button" role="button">
    <div><img src='D:/Infa_3_sem/html/plus.png' width="100" height="100" alt=""></div>
    <div>Добавить отзыв</div>
</div>

<script type="text/javascript">
    let btn = document.getElementById('addButton');
    window.addEventListener('click', function(event){
        if (document.getElementById('addButton').contains(event.target)) {
            document.getElementById('hover-window').style.visibility = 'visible';
        } else if(!document.getElementById('hover-window').contains(event.target)){
            document.getElementById('hover-window').style.visibility = 'hidden';
        }
    });
</script>
</body>
</html>