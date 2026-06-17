# 🍽️ Food Restaurant - Sistema Web

Sistema web para restaurante desarrollado con Spring Boot y Thymeleaf.

---

## ✅ Requisitos previos

Antes de correr el proyecto asegúrate de tener instalado:

- [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/download/)
- [XAMPP](https://www.apachefriends.org/) (para MySQL)
- [Git](https://git-scm.com/download/win)

---

## 📥 Clonar el proyecto

Abre una terminal y ejecuta:

```bash
git clone https://github.com/ch4lzeus/restaurante-web.git
```

Luego abre la carpeta `Food_restaurant` desde IntelliJ: **File → Open → selecciona la carpeta**.

---

## ⚙️ Configurar la base de datos

1. Abre XAMPP y enciende el servicio **MySQL**
2. Abre **phpMyAdmin** en tu navegador: `http://localhost/phpmyadmin`
3. Crea una base de datos llamada `food_express`
4. Importa el script SQL que está en la carpeta `database/food_express_2.sql`

---

## 🔧 Configurar application.properties

Dentro del proyecto ve a:

```
Food_restaurant/src/main/resources/
```

Copia el archivo `application-example.properties` y renómbralo a `application.properties`.

Edítalo con tus datos:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/food_express
spring.datasource.username=root
spring.datasource.password=TU_CONTRASEÑA
```

> Si usas XAMPP la contraseña normalmente está vacía, déjalo en blanco.

---

## ▶️ Correr el proyecto

Desde IntelliJ ejecuta la clase principal:

```
FoodRestaurantApplication.java
```

O con el botón ▶️ verde arriba. El sistema estará disponible en:

```
http://localhost:8080
```

---

## 🌿 Flujo de trabajo con Git (para colaboradores)

### 1. Crear tu rama de trabajo
```bash
git checkout -b feature/mejora-frontend
```

### 2. Hacer cambios y subirlos
```bash
git add .
git commit -m "descripcion de lo que hiciste"
git push origin feature/mejora-frontend
```

### 3. Crear un Pull Request
- Ve a [github.com/ch4lzeus/restaurante-web](https://github.com/ch4lzeus/restaurante-web)
- Click en **Compare & pull request**
- Describe los cambios y envíalo
- El dueño del repo lo revisará y decidirá si lo incluye

> ⚠️ **Nunca trabajes directo en la rama `main`**

---

## 📁 Estructura del proyecto

```
restaurante-web/
├── Food_restaurant/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/        → Código Java (controllers, services, entities)
│   │   │   └── resources/
│   │   │       ├── templates/ → Vistas HTML (Thymeleaf)
│   │   │       └── static/    → CSS, JS, imágenes
│   └── pom.xml
├── database/
│   └── food_express_2.sql   → Script de la base de datos
└── README.md
```

---

## 🛠️ Tecnologías usadas

- Java 17
- Spring Boot
- Thymeleaf
- MySQL (XAMPP)
- Maven
