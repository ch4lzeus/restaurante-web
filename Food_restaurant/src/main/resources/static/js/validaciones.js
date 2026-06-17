
// ── 1. FUNCIONES BASE DE VALIDACIÓN ──────────────────────────

function soloNumeros(valor) {
    return /^\d+$/.test(valor.trim());
}

function soloLetras(valor) {
    return /^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\s]+$/.test(valor.trim());
}

function soloAlfanumerico(valor) {
    return /^[a-zA-Z0-9_]+$/.test(valor.trim());
}

function esCorreoValido(valor) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(valor.trim());
}

function esDniValido(valor) {
    return /^\d{8}$/.test(valor.trim());
}

function esContrasenaValida(valor) {
    return valor.length >= 6;
}

function esContrasenaFuerte(valor) {
    return valor.length >= 8 &&
           /[A-Z]/.test(valor) &&
           /[0-9]/.test(valor);
}

function esPrecioValido(valor) {
    return /^\d+(\.\d{1,2})?$/.test(valor.trim()) && parseFloat(valor) > 0;
}

function noVacio(valor) {
    return valor.trim().length > 0;
}

function sonIguales(valor1, valor2) {
    return valor1 === valor2;
}


// ── 2. BLOQUEO DE TECLAS EN TIEMPO REAL ──────────────────────

function aplicarBloqueoTeclas(campo) {
    const tipo = campo.getAttribute('data-validar');
    const max  = campo.getAttribute('data-max');

    switch (tipo) {

        case 'solo-numeros':
            campo.addEventListener('keypress', e => {
                if (!/[0-9]/.test(e.key)) e.preventDefault();
            });
            campo.addEventListener('input', () => {
                campo.value = campo.value.replace(/[^0-9]/g, '');
                if (max && campo.value.length > parseInt(max)) {
                    campo.value = campo.value.slice(0, parseInt(max));
                }
            });
            break;

        case 'dni':
            campo.addEventListener('keypress', e => {
                if (!/[0-9]/.test(e.key)) e.preventDefault();
            });
            campo.addEventListener('input', () => {
                campo.value = campo.value.replace(/[^0-9]/g, '');
                if (campo.value.length > 8) {
                    campo.value = campo.value.slice(0, 8);
                }
            });
            break;

        case 'solo-letras':
            campo.addEventListener('keypress', e => {
                if (!/[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\s]/.test(e.key)) e.preventDefault();
            });
            campo.addEventListener('input', () => {
                campo.value = campo.value.replace(/[^a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\s]/g, '');
            });
            break;

        case 'alfanumerico':
            campo.addEventListener('keypress', e => {
                if (!/[a-zA-Z0-9_]/.test(e.key)) e.preventDefault();
            });
            campo.addEventListener('input', () => {
                campo.value = campo.value.replace(/[^a-zA-Z0-9_]/g, '');
                if (max && campo.value.length > parseInt(max)) {
                    campo.value = campo.value.slice(0, parseInt(max));
                }
            });
            break;

        case 'precio':
            campo.addEventListener('keypress', e => {
                const permitido = /[0-9.]/.test(e.key);
                const yaTienePunto = campo.value.includes('.');
                if (!permitido || (e.key === '.' && yaTienePunto)) {
                    e.preventDefault();
                }
            });
            campo.addEventListener('input', () => {
                let val = campo.value.replace(/[^0-9.]/g, '');
                const partes = val.split('.');
                if (partes.length > 2) val = partes[0] + '.' + partes[1];
                if (partes[1] && partes[1].length > 2) {
                    val = partes[0] + '.' + partes[1].slice(0, 2);
                }
                campo.value = val;
            });
            break;

        // correo, contrasena, contrasena-fuerte, requerido — sin bloqueo
        default:
            break;
    }
}


// ── 3. MOSTRAR / LIMPIAR ERRORES ─────────────────────────────

function mostrarError(campo, mensaje) {
    limpiarError(campo);
    campo.classList.add('input-error');
    const span = document.createElement('span');
    span.className = 'msg-error';
    span.textContent = mensaje;
    campo.parentNode.appendChild(span);
}

function limpiarError(campo) {
    campo.classList.remove('input-error');
    campo.classList.remove('input-ok');
    const prev = campo.parentNode.querySelector('.msg-error');
    if (prev) prev.remove();
}

function marcarOk(campo) {
    limpiarError(campo);
    campo.classList.add('input-ok');
}


// ── 4. VALIDAR CAMPO INDIVIDUAL ───────────────────────────────

function validarCampo(campo) {
    const tipo   = campo.getAttribute('data-validar');
    const valor  = campo.value;
    const min    = campo.getAttribute('data-min');
    const max    = campo.getAttribute('data-max');
    const igualA = campo.getAttribute('data-igual-a');

    if (!tipo) return true;

    if (!noVacio(valor)) {
        mostrarError(campo, 'Este campo es obligatorio.');
        return false;
    }

    if (min && valor.trim().length < parseInt(min)) {
        mostrarError(campo, `Mínimo ${min} caracteres.`);
        return false;
    }

    if (max && valor.trim().length > parseInt(max)) {
        mostrarError(campo, `Máximo ${max} caracteres.`);
        return false;
    }

    switch (tipo) {
        case 'solo-numeros':
            if (!soloNumeros(valor)) {
                mostrarError(campo, 'Solo se permiten números.');
                return false;
            }
            break;
        case 'solo-letras':
            if (!soloLetras(valor)) {
                mostrarError(campo, 'Solo se permiten letras y espacios.');
                return false;
            }
            break;
        case 'alfanumerico':
            if (!soloAlfanumerico(valor)) {
                mostrarError(campo, 'Solo letras, números y guion bajo. Sin espacios.');
                return false;
            }
            break;
        case 'correo':
            if (!esCorreoValido(valor)) {
                mostrarError(campo, 'Ingresa un correo electrónico válido.');
                return false;
            }
            break;
        case 'dni':
            if (!esDniValido(valor)) {
                mostrarError(campo, 'El DNI debe tener exactamente 8 dígitos.');
                return false;
            }
            break;
        case 'contrasena':
            if (!esContrasenaValida(valor)) {
                mostrarError(campo, 'La contraseña debe tener al menos 6 caracteres.');
                return false;
            }
            break;
        case 'contrasena-fuerte':
            if (!esContrasenaFuerte(valor)) {
                mostrarError(campo, 'Mínimo 8 caracteres, una mayúscula y un número.');
                return false;
            }
            break;
        case 'precio':
            if (!esPrecioValido(valor)) {
                mostrarError(campo, 'Ingresa un precio válido mayor a 0 (ej: 12.50).');
                return false;
            }
            break;
        case 'requerido':
            break;
    }

    if (igualA) {
        const otroCampo = document.getElementById(igualA);
        if (otroCampo && !sonIguales(valor, otroCampo.value)) {
            mostrarError(campo, 'Las contraseñas no coinciden.');
            return false;
        }
    }

    marcarOk(campo);
    return true;
}


// ── 5. VALIDAR FORMULARIO COMPLETO ───────────────────────────

function validarFormulario(formId) {
    const form   = document.getElementById(formId);
    const campos = form.querySelectorAll('[data-validar]');
    let valido   = true;
    campos.forEach(campo => {
        if (!validarCampo(campo)) valido = false;
    });
    return valido;
}


// ── 6. INICIALIZACIÓN AUTOMÁTICA ─────────────────────────────

document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('[data-validar]').forEach(campo => {
        aplicarBloqueoTeclas(campo);
        campo.addEventListener('blur', () => validarCampo(campo));
        campo.addEventListener('input', () => {
            if (campo.classList.contains('input-error')) {
                validarCampo(campo);
            }
        });
    });
});


// ── 7. CSS INYECTADO AUTOMÁTICAMENTE ─────────────────────────

(function inyectarEstilos() {
    const style = document.createElement('style');
    style.textContent = `
        .input-error {
            border: 1.5px solid #e63946 !important;
            background: #fff5f5 !important;
        }
        .input-ok {
            border: 1.5px solid #2dc653 !important;
            background: #f0fff4 !important;
        }
        .msg-error {
            display: block;
            color: #e63946;
            font-size: 0.75rem;
            margin-top: 3px;
        }
    `;
    document.head.appendChild(style);
})();