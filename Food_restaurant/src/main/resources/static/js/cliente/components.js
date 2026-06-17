// Usamos los platos enviados por Thymeleaf; el arreglo fijo queda solo como respaldo visual.
const platosData = window.platosData || [
    {
        id: 1,
        nombre: 'Pollo a la Brasa',
        precio: 25.00,
        categoria: 'Platos de fondo',
        descripcion: 'Pollo marinado con especias, papas fritas y ensalada fresca.',
        img: 'https://images.unsplash.com/photo-1598515214211-89d3c73ae83b?auto=format&fit=crop&w=900&q=80'
    },
    {
        id: 2,
        nombre: 'Lomo Saltado',
        precio: 32.00,
        categoria: 'Platos de fondo',
        descripcion: 'Salteado de carne, tomate, cebolla, papas fritas y arroz blanco.',
        img: 'https://images.unsplash.com/photo-1625944525533-473f1a3d54e7?auto=format&fit=crop&w=900&q=80'
    },
    {
        id: 3,
        nombre: 'Chicha Morada',
        precio: 6.00,
        categoria: 'Bebidas',
        descripcion: 'Bebida refrescante de maiz morado con frutas y especias.',
        img: 'https://images.unsplash.com/photo-1544145945-f90425340c7e?auto=format&fit=crop&w=900&q=80'
    }
];

function getCarrito() {
    return JSON.parse(localStorage.getItem('fe_carrito_cliente') || '[]');
}

function saveCarrito(carrito) {
    localStorage.setItem('fe_carrito_cliente', JSON.stringify(carrito));
    updateCartBadge();
}

function updateCartBadge() {
    const badge = document.getElementById('cart-badge');
    if (!badge) return;

    const total = getCarrito().reduce((sum, item) => sum + item.cantidad, 0);
    badge.textContent = total;
    badge.classList.toggle('hidden', total === 0);
}

function usuarioLogueado() {
    return typeof CLIENTE_LOGUEADO !== 'undefined' && CLIENTE_LOGUEADO === true;
}

function nombreCliente() {
    return typeof CLIENTE_NOMBRE !== 'undefined' && CLIENTE_NOMBRE ? CLIENTE_NOMBRE : 'Cliente';
}

function alertarLogin(mensaje) {
    Swal.fire({
        icon: 'warning',
        title: 'Debes iniciar sesion',
        text: mensaje || 'Para continuar necesitas iniciar sesion.',
        confirmButtonText: 'Ir al login',
        cancelButtonText: 'Cancelar',
        confirmButtonColor: '#3a5ba0',
        showCancelButton: true
    }).then(result => {
        if (result.isConfirmed) {
            window.location.href = '/login';
        }
    });
}

function confirmarLogoutCliente() {
    Swal.fire({
        title: 'Cerrar sesion?',
        text: 'Tu sesion se cerrara',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Si, salir',
        cancelButtonText: 'Cancelar',
        confirmButtonColor: '#3a5ba0'
    }).then(result => {
        if (result.isConfirmed) {
            window.location.href = '/logout';
        }
    });
    return false;
}

function clienteHeaderHTML() {
    const accionSesion = usuarioLogueado()
        ? `<a href="#" onclick="return confirmarLogoutCliente()" class="icon-link session-active" title="Sesion activa: ${nombreCliente()}">
                <i class="fas fa-user"></i>
                <span class="session-check"><i class="fas fa-check"></i></span>
           </a>`
        : `<a href="/login" class="icon-link" title="Iniciar sesion"><i class="fas fa-user"></i></a>`;

    return `
        <nav class="client-nav">
            <div class="wrap nav-inner">
                <a href="/cliente" class="brand">
                    <span class="brand-icon"><i class="fas fa-burger"></i></span>
                    <span>FOOD EXPRESS</span>
                </a>
                <div class="nav-links">
                    <a href="/cliente">Inicio</a>
                    <a href="/cliente/menu">Carta</a>
                    <a href="/cliente/carrito">Carrito</a>
                </div>
                <div class="nav-actions">
                    ${accionSesion}
                    <a href="/cliente/carrito" class="icon-link" title="Carrito">
                        <i class="fas fa-shopping-cart"></i>
                        <span id="cart-badge" class="cart-badge hidden">0</span>
                    </a>
                </div>
            </div>
        </nav>`;
}

function clienteFooterHTML() {
    const accesoSesion = usuarioLogueado()
        ? `<p><a href="#" onclick="return confirmarLogoutCliente()">Cerrar sesion</a></p>`
        : `<p><a href="/login">Iniciar sesion</a></p><p><a href="/registro">Crear cuenta</a></p>`;

    return `
        <footer class="client-footer">
            <div class="wrap">
                <div class="footer-grid">
                    <div>
                        <h3><i class="fas fa-burger"></i> Food Express</h3>
                        <p>Pedidos para local y delivery con una experiencia simple para el cliente.</p>
                    </div>
                    <div>
                        <h4>Servicios</h4>
                        <p><i class="fas fa-truck"></i> Delivery</p>
                        <p><i class="fas fa-store"></i> Atencion en local</p>
                    </div>
                    <div>
                        <h4>Accesos</h4>
                        <p><a href="/cliente/menu">Carta</a></p>
                        ${accesoSesion}
                    </div>
                </div>
                <div class="footer-copy">2026 Food Express</div>
            </div>
        </footer>`;
}

function loadClienteComponents() {
    const header = document.getElementById('cliente-header');
    const footer = document.getElementById('cliente-footer');

    if (header) header.innerHTML = clienteHeaderHTML();
    if (footer) footer.innerHTML = clienteFooterHTML();

    updateCartBadge();
}

function dishCard(plato) {
    const precio = Number(plato.precio || 0);
    return `
        <article class="dish-card">
            <img src="${plato.img}" alt="${plato.nombre}">
            <div class="dish-body">
                <p class="dish-name">${plato.nombre}</p>
                <div class="dish-meta">
                    <span>${plato.categoria}</span>
                    <span class="price-pill">S/ ${precio.toFixed(2)}</span>
                </div>
                <div class="dish-actions">
                    <button class="btn-green-food" onclick="agregarAlCarrito('${plato.id}', 1)">
                        <i class="fas fa-cart-plus"></i> Agregar
                    </button>
                    <a class="btn-outline-food" href="/cliente/plato/${plato.id}">
                        <i class="fas fa-eye"></i> Ver
                    </a>
                </div>
            </div>
        </article>`;
}

function renderDestacados(targetId) {
    const grid = document.getElementById(targetId);
    if (!grid) return;
    grid.innerHTML = platosData.map(dishCard).join('');
}

function initMenuPage() {
    const grid = document.getElementById('platos-grid');
    const noResultados = document.getElementById('no-resultados');
    const contador = document.getElementById('num-platos');
    const buscador = document.getElementById('buscador');
    const filterRow = document.querySelector('.filter-row');
    let categoriaActiva = 'Todos';
    let busqueda = '';

    if (filterRow) {
        const categorias = [...new Set(platosData.map(plato => plato.categoria))];
        filterRow.innerHTML = `
            <span>Filtrar:</span>
            <button class="filter-btn active" data-cat="Todos">Todos</button>
            ${categorias.map(cat => `<button class="filter-btn" data-cat="${cat}">${cat}</button>`).join('')}
        `;
    }

    function render() {
        const filtrados = platosData.filter(plato => {
            const coincideCategoria = categoriaActiva === 'Todos' || plato.categoria === categoriaActiva;
            const coincideBusqueda = plato.nombre.toLowerCase().includes(busqueda.toLowerCase());
            return coincideCategoria && coincideBusqueda;
        });

        contador.textContent = filtrados.length;
        noResultados.classList.toggle('hidden', filtrados.length > 0);
        grid.innerHTML = filtrados.map(dishCard).join('');
    }

    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            document.querySelectorAll('.filter-btn').forEach(item => item.classList.remove('active'));
            btn.classList.add('active');
            categoriaActiva = btn.dataset.cat;
            render();
        });
    });

    if (buscador) {
        buscador.addEventListener('input', event => {
            busqueda = event.target.value;
            render();
        });
    }

    render();
}

function initDetallePage() {
    const id = decodeURIComponent(location.pathname.split('/').pop());
    const plato = platosData.find(item => String(item.id) === id);
    const detalle = document.getElementById('detalle-contenido');
    const relacionados = document.getElementById('relacionados-grid');

    if (!plato) {
        detalle.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-circle-exclamation"></i>
                <h3>Plato no encontrado</h3>
                <p>Vuelve a la carta para elegir otra opcion.</p>
                <a class="btn-primary-food" href="/cliente/menu">Ver carta</a>
            </div>`;
        return;
    }

    document.title = `Food Express - ${plato.nombre}`;
    detalle.innerHTML = `
        <img class="detail-image" src="${plato.img}" alt="${plato.nombre}">
        <div class="detail-info">
            <span class="eyebrow">${plato.categoria}</span>
            <h1>${plato.nombre}</h1>
            <span class="price-pill">S/ ${Number(plato.precio || 0).toFixed(2)}</span>
            <p>${plato.descripcion}</p>
            <label class="field-title">Cantidad</label>
            <div class="qty-control">
                <button onclick="cambiarCantidadDetalle(-1)">-</button>
                <input type="number" id="cantidad-detalle" value="1" min="1" max="20">
                <button onclick="cambiarCantidadDetalle(1)">+</button>
            </div>
            <div>
                <button class="btn-primary-food" onclick="agregarDetalleAlCarrito('${plato.id}')">
                    <i class="fas fa-cart-plus"></i> Agregar al carrito
                </button>
            </div>
        </div>`;

    relacionados.innerHTML = platosData
        .filter(item => item.id !== plato.id)
        .slice(0, 2)
        .map(dishCard)
        .join('');
}

function cambiarCantidadDetalle(delta) {
    const input = document.getElementById('cantidad-detalle');
    if (!input) return;

    let value = Number(input.value || 1) + delta;
    value = Math.max(1, Math.min(20, value));
    input.value = value;
}

function agregarDetalleAlCarrito(platoId) {
    const input = document.getElementById('cantidad-detalle');
    agregarAlCarrito(platoId, Number(input?.value || 1));
}

function agregarAlCarrito(platoId, cantidad) {
    if (!usuarioLogueado()) {
        alertarLogin('Para agregar platos al carrito necesitas iniciar sesion.');
        return false;
    }

    const plato = platosData.find(item => item.id === platoId);
    if (!plato) return false;

    const carrito = getCarrito();
    const existente = carrito.find(item => item.id === plato.id);

    if (existente) {
        existente.cantidad += cantidad;
    } else {
        carrito.push({
            id: plato.id,
            nombre: plato.nombre,
            precio: Number(plato.precio || 0),
            cantidad,
            img: plato.img
        });
    }

    saveCarrito(carrito);

    Swal.fire({
        toast: true,
        position: 'top-end',
        icon: 'success',
        title: `${plato.nombre} agregado`,
        timer: 1400,
        showConfirmButton: false
    });

    return true;
}

document.addEventListener('DOMContentLoaded', loadClienteComponents);

window.addEventListener('pageshow', event => {
    if (event.persisted) {
        window.location.reload();
    }
    verificarSesionCliente();
});

function verificarSesionCliente() {
    if (!usuarioLogueado()) return;

    fetch('/session-status', { cache: 'no-store' })
        .then(res => res.json())
        .then(data => {
            if (!data.logged || data.rol !== 'CLI') {
                window.location.replace('/cliente');
            }
        })
        .catch(() => {});
}

