document.addEventListener("DOMContentLoaded", () => {

    // --- Login ---
    if (LOGIN && LOGIN.trim() === "ok") {
        Swal.fire({
            icon: 'success',
            title: 'Bienvenido',
            text: 'Inicio de sesión correcto',
            confirmButtonText: 'Ingresar'
        }).then(() => {
            window.location.href = typeof REDIRECT_URL !== 'undefined' && REDIRECT_URL ? REDIRECT_URL : "/dashboard";
        });
    }

    if (ERROR === "ERROR_USER" || ERROR === "ERROR_PASS") {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Usuario o contraseña incorrectos'
        });
    }

    if (ERROR === "INACTIVO") {
        Swal.fire({
            icon: 'warning',
            title: 'Usuario inactivo',
            text: 'Tu cuenta está desactivada'
        });
    }

    if (ERROR === "ERROR") {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Ocurrió un problema inesperado'
        });
    }

    // --- Recuperar contraseña ---
    if (MSG === "pass") {
        Swal.fire({
            icon: 'success',
            title: 'Contraseña actualizada',
            text: 'Ya puedes iniciar sesión'
        }).then(() => {
            window.location.href = "/login";
        });
    }

    if (MSG === "errR") {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'DNI no encontrado o las contraseñas no coinciden'
        });
    }

    // NUEVAS ALERTAS PARA REGISTRO
    if (MSG === "ok") { // Registro exitoso
        Swal.fire({
            icon: 'success',
            title: 'Registro exitoso',
            text: 'Ya puedes iniciar sesión'
        }).then(() => {
            window.location.href = "/login"; // Redirige a login
        });
    }

    if (MSG === "err") { // Error general al registrar
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Ocurrió un problema al registrar el usuario'
        });
    }

    if (MSG === "noMatch") { // Contraseñas no coinciden
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Las contraseñas no coinciden'
        });
    }

    if (MSG === "dup") { // Usuario ya existe
        Swal.fire({
            icon: 'warning',
            title: 'Usuario duplicado',
            text: 'Ese nombre de usuario ya existe'
        });
    }

});
