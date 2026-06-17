const ENVIO = 10;
let tipoPedido = 'local';
let metodoPago = 'efectivo';

function setTipo(tipo) {
    tipoPedido = tipo;
    document.getElementById('btn-local').classList.toggle('active', tipo === 'local');
    document.getElementById('btn-delivery').classList.toggle('active', tipo === 'delivery');
    const deliveryFields = document.getElementById('delivery-fields');
    if (deliveryFields) deliveryFields.style.display = tipo === 'delivery' ? 'block' : 'none';
    actualizarResumen();
}

function setPago(button, metodo) {
    document.querySelectorAll('.payment-opt').forEach(item => item.classList.remove('active'));
    button.classList.add('active');
    metodoPago = metodo;
}

function renderCarrito() {
    const lista = document.getElementById('carrito-lista');
    const resumen = document.getElementById('resumen-col');
    const badge = document.getElementById('badge-total-items');
    const carrito = getCarrito();
    const totalItems = carrito.reduce((sum, item) => sum + item.cantidad, 0);

    badge.textContent = totalItems + (totalItems === 1 ? ' item' : ' items');

    if (carrito.length === 0) {
        resumen.style.display = 'none';
        lista.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-cart-arrow-down"></i>
                <h3>Tu carrito esta vacio</h3>
                <p>Agrega platos desde la carta para armar tu pedido.</p>
                <a class="btn-primary-food" href="/cliente/menu">
                    <i class="fas fa-utensils"></i> Ver carta
                </a>
            </div>`;
        return;
    }

    resumen.style.display = 'block';
    lista.innerHTML = carrito.map((item, index) => {
        const precio = Number(item.precio || 0);
        return `
        <div class="cart-item">
            <img src="${item.img}" alt="${item.nombre}">
            <div>
                <p class="item-name">${item.nombre}</p>
                <p class="item-muted">S/ ${precio.toFixed(2)} c/u</p>
            </div>
            <div class="qty-mini">
                <button onclick="cambiarCantidadItem(${index}, -1)">-</button>
                <input type="number" value="${item.cantidad}" min="1" max="20"
                       onchange="setCantidadItem(${index}, this.value)">
                <button onclick="cambiarCantidadItem(${index}, 1)">+</button>
            </div>
            <span class="item-subtotal">S/ ${(precio * item.cantidad).toFixed(2)}</span>
            <button class="btn-remove" onclick="quitarItem(${index})" title="Quitar">
                <i class="fas fa-trash-alt"></i>
            </button>
        </div>`;
    }).join('');

    actualizarResumen();
}

function actualizarResumen() {
    const carrito = getCarrito();
    const subtotal = carrito.reduce((sum, item) => sum + Number(item.precio || 0) * item.cantidad, 0);
    const envio = tipoPedido === 'delivery' ? ENVIO : 0;
    const total = subtotal + envio;

    document.getElementById('res-subtotal').textContent = `S/ ${subtotal.toFixed(2)}`;
    document.getElementById('res-envio').textContent = envio > 0 ? `S/ ${envio.toFixed(2)}` : 'Gratis';
    document.getElementById('res-total').textContent = `S/ ${total.toFixed(2)}`;
}

function cambiarCantidadItem(index, delta) {
    const carrito = getCarrito();
    carrito[index].cantidad = Math.max(1, Math.min(20, carrito[index].cantidad + delta));
    saveCarrito(carrito);
    renderCarrito();
}

function setCantidadItem(index, value) {
    const carrito = getCarrito();
    let cantidad = Number(value);
    if (!Number.isFinite(cantidad)) cantidad = 1;
    carrito[index].cantidad = Math.max(1, Math.min(20, cantidad));
    saveCarrito(carrito);
    renderCarrito();
}

function quitarItem(index) {
    const carrito = getCarrito();
    const [item] = carrito.splice(index, 1);
    saveCarrito(carrito);
    renderCarrito();

    Swal.fire({
        toast: true,
        position: 'top-end',
        icon: 'info',
        title: `${item.nombre} eliminado`,
        timer: 1200,
        showConfirmButton: false
    });
}

function confirmarOrden() {
    if (!usuarioLogueado()) {
        alertarLogin('Para confirmar tu orden necesitas iniciar sesion.');
        return;
    }

    const carrito = getCarrito();
    if (carrito.length === 0) return;

    const codCliente = typeof CLIENTE_COD !== 'undefined' ? CLIENTE_COD : '';
    if (!codCliente) {
        Swal.fire({
            icon: 'error',
            title: 'Cliente no identificado',
            text: 'No se pudo obtener tu codigo de cliente.',
            confirmButtonColor: '#3a5ba0'
        });
        return;
    }

    const subtotal = carrito.reduce((sum, item) => sum + Number(item.precio || 0) * item.cantidad, 0);
    const envio = tipoPedido === 'delivery' ? ENVIO : 0;
    const total = subtotal + envio;
    const tipoLabel = tipoPedido === 'delivery' ? 'Delivery' : 'En local';
    const pagoLabel = { efectivo: 'Efectivo', tarjeta: 'Tarjeta', yape: 'Yape' }[metodoPago];
    const items = carrito.map(item => `
        <li>${item.nombre} x${item.cantidad} - S/ ${(Number(item.precio || 0) * item.cantidad).toFixed(2)}</li>
    `).join('');

    Swal.fire({
        title: 'Resumen del pedido',
        html: `
            <div style="text-align:left">
                <ul style="padding-left:18px">${items}</ul>
                <hr>
                <p><strong>Tipo:</strong> ${tipoLabel}</p>
                <p><strong>Pago:</strong> ${pagoLabel}</p>
                <p><strong>Total:</strong> S/ ${total.toFixed(2)}</p>
            </div>
        `,
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: 'Confirmar',
        cancelButtonText: 'Cancelar',
        confirmButtonColor: '#3a5ba0'
    }).then(result => {
        if (!result.isConfirmed) return;

        const direccion = document.getElementById('delivery-direccion')?.value.trim() || '';
        const telefono = document.getElementById('delivery-telefono')?.value.trim() || '';
        const descripcion = document.getElementById('delivery-descripcion')?.value.trim() || '';

        if (tipoPedido === 'delivery' && (!direccion || !telefono)) {
            Swal.fire({
                icon: 'warning',
                title: 'Faltan datos de delivery',
                text: 'Debes indicar direccion y telefono.',
                confirmButtonColor: '#3a5ba0'
            });
            return;
        }

        const payload = {
            codCliente,
            esLocal: tipoPedido === 'local' ? 1 : 0,
            nroMesa: null,
            direccion: tipoPedido === 'delivery' ? direccion : null,
            telefono: tipoPedido === 'delivery' ? telefono : null,
            descripcion: tipoPedido === 'delivery' ? descripcion : 'Pedido online para local',
            items: carrito.map(item => ({
                id: item.id,
                precio: item.precio,
                cantidad: item.cantidad
            }))
        };

        fetch('/pedidos/registrar', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    saveCarrito([]);
                    Swal.fire({
                        icon: 'success',
                        title: 'Pedido confirmado',
                        html: `Tu pedido <strong>${data.nroPedido}</strong> fue registrado.<br>En breve lo estaremos preparando.`,
                        confirmButtonColor: '#3a5ba0'
                    }).then(() => {
                        window.location.href = '/cliente/menu';
                    });
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error al registrar',
                        text: data.error || 'Intenta nuevamente.',
                        confirmButtonColor: '#3a5ba0'
                    });
                }
            })
            .catch(() => {
                Swal.fire({
                    icon: 'error',
                    title: 'Error de conexion',
                    text: 'No se pudo conectar con el servidor.',
                    confirmButtonColor: '#3a5ba0'
                });
            });
    });
}

document.addEventListener('DOMContentLoaded', () => {
    if (!usuarioLogueado()) {
        const lista = document.getElementById('carrito-lista');
        const resumen = document.getElementById('resumen-col');
        const badge = document.getElementById('badge-total-items');

        if (resumen) resumen.style.display = 'none';
        if (badge) badge.textContent = '0 items';
        if (lista) {
            lista.innerHTML = `
                <div class="empty-state">
                    <i class="fas fa-user-lock"></i>
                    <h3>Inicia sesion para ver tu carrito</h3>
                    <p>Tu pedido se prepara desde una cuenta de cliente.</p>
                    <button class="btn-primary-food" onclick="alertarLogin('Para ver tu carrito necesitas iniciar sesion.')">
                        <i class="fas fa-sign-in-alt"></i> Ir al login
                    </button>
                </div>`;
        }
        return;
    }

    renderCarrito();
    setTipo(tipoPedido);
});
