/* global Swal */

// - Navegacion entre secciones -
function showSec(id, el) {
    const secActiva = document.getElementById('sec-' + id);
    if (!secActiva) return false;

    document.querySelectorAll('.sec').forEach(s => s.classList.remove('active'));
    document.querySelectorAll('.sb-nav a').forEach(a => a.classList.remove('active'));
    secActiva.classList.add('active');
    if (el) el.classList.add('active');
    return false;
}

function togSub(id, el) {
    const mapa = {
        'sub-cat': 'catalogo',
        'sub-coc': 'cocina',
        'sub-ped': 'pedidos',
        'sub-pag': 'pagos',
        'sub-ent': 'entregas',
        'sub-rep': 'reportes'
    };

    const s = document.getElementById(id);
    if (!s) return false;

    s.classList.toggle('open');
    const k = mapa[id] || id.replace('sub-', '');
    const ic = document.getElementById('ic-' + id.replace('sub-', ''));
    if (ic) ic.style.transform = s.classList.contains('open') ? 'rotate(180deg)' : '';

    document.querySelectorAll('.sec').forEach(sc => sc.classList.remove('active'));
    const sec = document.getElementById('sec-' + k);
    if (sec) {
        sec.classList.add('active');
        sec.querySelectorAll('.tab-pane').forEach(t => t.classList.remove('active'));
        const primerTab = sec.querySelector('.tab-pane');
        if (primerTab) primerTab.classList.add('active');
        sec.querySelectorAll('.sec-tab').forEach(b => b.classList.remove('active'));
        const primerBtn = sec.querySelector('.sec-tab');
        if (primerBtn) primerBtn.classList.add('active');
    }

    document.querySelectorAll('.sb-nav a').forEach(a => a.classList.remove('active'));
    if (el) el.classList.add('active');
    const primerLink = s.querySelector('a');
    if (primerLink) primerLink.classList.add('active');

    return false;
}

// - Tabs -
function showTab(sec, tab, el) {
    showSec(sec, null);
    sTabById(sec, tab);
    activarBotonTab(sec, tab);

    document.querySelectorAll('.sb-nav a').forEach(a => a.classList.remove('active'));
    if (el) el.classList.add('active');
    return false;
}

function sTab(sec, tab, btnEl) {
    sTabById(sec, tab);
    activarBotonTab(sec, tab, btnEl);
}

function sTabById(sec, tab) {
    const s = document.getElementById('sec-' + sec);
    if (!s) return;

    const pfx = sec.substring(0, 3);
    s.querySelectorAll('.tab-pane').forEach(t => t.classList.remove('active'));
    const el = document.getElementById(pfx + '-' + tab);
    if (el) el.classList.add('active');
}

function activarBotonTab(sec, tab, btnEl) {
    const s = document.getElementById('sec-' + sec);
    if (!s) return;

    s.querySelectorAll('.sec-tab').forEach(b => b.classList.remove('active'));
    if (btnEl) {
        btnEl.classList.add('active');
        return;
    }

    const btn = Array.from(s.querySelectorAll('.sec-tab')).find(b => {
        const onclick = b.getAttribute('onclick') || '';
        return onclick.includes("'" + tab + "'");
    });
    if (btn) btn.classList.add('active');
}

function mostrarMensajeDashboard(msg) {
    if (!msg || typeof Swal === 'undefined') return;

    const mensajes = {
        usrOk: ['Usuario registrado correctamente', 'success'],
        usrEdit: ['Usuario actualizado correctamente', 'success'],
        usrDes: ['Usuario desactivado correctamente', 'success'],
        usrAct: ['Usuario activado correctamente', 'success'],
        usrDup: ['Ese usuario ya existe', 'error'],
        usrErr: ['No se pudo procesar el usuario', 'error'],

        aliOk: ['Plato registrado correctamente', 'success'],
        aliEdit: ['Plato actualizado correctamente', 'success'],
        aliDes: ['Plato ocultado de la carta', 'success'],
        aliAct: ['Plato visible nuevamente', 'success'],
        aliDup: ['Ese plato ya existe', 'error'],
        aliErr: ['No se pudo procesar el plato', 'error'],

        catOk: ['Categoria registrada correctamente', 'success'],
        catDup: ['Esa categoria ya existe', 'error'],

        mesaOk: ['Mesa registrada correctamente', 'success'],
        mesaEdit: ['Mesa actualizada correctamente', 'success'],
        mesaDes: ['Mesa desactivada correctamente', 'success'],
        mesaAct: ['Mesa activada correctamente', 'success'],
        mesaDup: ['Ya existe una mesa con ese numero', 'error'],

        resOk: ['Reserva registrada correctamente', 'success'],
        resCan: ['Reserva cancelada correctamente', 'success'],
        resAct: ['Reserva reactivada correctamente', 'success'],
        resDup: ['Esa reserva ya existe', 'error']
    };

    const data = mensajes[msg];
    if (data) {
        Swal.fire(data[0], '', data[1]);
        limpiarParametroMsg();
    }
}

function verDetallePedido(nroPedido) {
    const cont = document.getElementById('ped-detalle');
    if (!cont) return;

    cont.innerHTML = `
        <i class="fas fa-spinner fa-spin" style="font-size:2rem;margin-bottom:.5rem;display:block;color:var(--bd);"></i>
        Cargando detalle...`;

    fetch('/pedidos/detalle/' + encodeURIComponent(nroPedido), { cache: 'no-store' })
        .then(res => res.json().then(data => ({ ok: res.ok, data })))
        .then(({ ok, data }) => {
            if (!ok) {
                cont.innerHTML = '<p style="color:var(--mu);">No se pudo cargar el detalle del pedido.</p>';
                return;
            }

            const detalles = (data.detalles || []).map(d => `
                <tr>
                    <td>${d.nombre}</td>
                    <td>${d.categoria || ''}</td>
                    <td>${d.cantidad}</td>
                    <td>S/ ${Number(d.subTotal || 0).toFixed(2)}</td>
                </tr>
            `).join('');

            cont.innerHTML = `
                <div style="text-align:left;margin-bottom:1rem;">
                    <p><strong>Pedido:</strong> ${data.nroPedido}</p>
                    <p><strong>Cliente:</strong> ${data.codCliente}</p>
                    <p><strong>Tipo:</strong> ${data.esLocal == 1 ? 'Local' : 'Delivery'}</p>
                    <p><strong>Total:</strong> S/ ${Number(data.total || 0).toFixed(2)}</p>
                    ${data.esLocal == 0 ? `<p><strong>Direccion:</strong> ${data.direccion || ''}</p>` : ''}
                </div>
                <div class="table-wrap">
                    <table>
                        <thead>
                            <tr>
                                <th>Plato</th>
                                <th>Categoria</th>
                                <th>Cantidad</th>
                                <th>Subtotal</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${detalles || '<tr><td colspan="4" style="text-align:center;color:var(--mu);padding:1rem;">Sin detalles</td></tr>'}
                        </tbody>
                    </table>
                </div>`;
        })
        .catch(() => {
            cont.innerHTML = '<p style="color:var(--mu);">No se pudo cargar el detalle del pedido.</p>';
        });
}

function verDetallePedidoCocina(nroPedido) {
    const cont = document.getElementById('cocina-detalle');
    if (!cont) return;

    cont.innerHTML = `
        <i class="fas fa-spinner fa-spin" style="font-size:2rem;margin-bottom:.5rem;display:block;color:var(--bd);"></i>
        Cargando detalle...`;

    fetch('/pedidos/detalle/' + encodeURIComponent(nroPedido), { cache: 'no-store' })
        .then(res => res.json().then(data => ({ ok: res.ok, data })))
        .then(({ ok, data }) => {
            if (!ok) {
                cont.innerHTML = '<p style="color:var(--mu);">No se pudo cargar el detalle del pedido.</p>';
                return;
            }

            const detalles = (data.detalles || []).map(d => `
                <tr>
                    <td>${d.nombre}</td>
                    <td>${d.categoria || ''}</td>
                    <td>${d.cantidad}</td>
                    <td>S/ ${Number(d.precio || 0).toFixed(2)}</td>
                    <td>S/ ${Number(d.subTotal || 0).toFixed(2)}</td>
                </tr>
            `).join('') || `
                <tr>
                    <td colspan="5" style="text-align:center;color:var(--mu);padding:1rem;">Sin detalles registrados.</td>
                </tr>`;

            cont.innerHTML = `
                <div style="text-align:left;margin-bottom:1rem;">
                    <p><strong>Pedido:</strong> ${data.nroPedido}</p>
                    <p><strong>Cliente:</strong> ${data.codCliente}</p>
                    <p><strong>Tipo:</strong> ${data.esLocal == 1 ? 'Local' : 'Delivery'}</p>
                    <p><strong>Estado:</strong> ${data.estado || '-'}</p>
                    ${data.esLocal == 1 ? `<p><strong>Mesa:</strong> ${data.nroMesa || '-'}</p>` : `<p><strong>Direccion:</strong> ${data.direccion || '-'}</p>`}
                    ${data.descripcion ? `<p><strong>Notas:</strong> ${data.descripcion}</p>` : ''}
                </div>
                <div class="table-wrap">
                    <table>
                        <thead>
                            <tr>
                                <th>Plato</th>
                                <th>Categoria</th>
                                <th>Cantidad</th>
                                <th>Precio</th>
                                <th>Subtotal</th>
                            </tr>
                        </thead>
                        <tbody>${detalles}</tbody>
                    </table>
                </div>`;
        })
        .catch(() => {
            cont.innerHTML = '<p style="color:var(--mu);">No se pudo cargar el detalle del pedido.</p>';
        });
}

function limpiarParametroMsg() {
    const url = new URL(window.location.href);
    if (!url.searchParams.has('msg')) return;

    url.searchParams.delete('msg');
    const nuevaUrl = url.pathname + (url.searchParams.toString() ? '?' + url.searchParams.toString() : '') + url.hash;
    window.history.replaceState({}, document.title, nuevaUrl);
}

// - Logout -
function doLogout() {
    Swal.fire({
        title: 'Cerrar sesion?',
        text: 'Tu sesion se cerrara',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Si, salir',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = '/logout';
        }
    });
}

function cambiarEstadoPedido(nroPedido, estado) {
    if (estado !== 'PREPARANDO' && estado !== 'LISTO') return;
    const textos = {
        PREPARANDO: 'pasar el pedido a preparacion',
        LISTO: 'marcar el pedido como listo',
        ENTREGADO: 'marcar el pedido como entregado'
    };
    Swal.fire({
        title: 'Cambiar estado?',
        text: 'Vas a ' + (textos[estado] || 'cambiar el estado del pedido') + '.',
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: 'Si, continuar',
        cancelButtonText: 'Cancelar',
        confirmButtonColor: '#3a5ba0'
    }).then(r => {
        if (!r.isConfirmed) return;
        fetch('/pedidos/estado', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ nroPedido, estado })
        })
        .then(async res => {
            const data = await res.json().catch(() => ({}));
            if (!res.ok) throw new Error(data.error || 'No se pudo actualizar el pedido');
            return data;
        })
        .then(() => {
            Swal.fire({
                icon: 'success',
                title: 'Pedido actualizado',
                text: 'El pedido fue actualizado correctamente.'
            }).then(() => window.location.reload());
        })
        .catch(err => {
            Swal.fire({ icon: 'error', title: 'Error', text: err.message || 'No se pudo actualizar el pedido' });
        });
    });
}

function filtrarCocinaPorEstado() {
    const sel = document.getElementById('cocina-filtro-estado');
    const estado = (sel?.value || 'TODOS').toUpperCase();
    const rows = document.querySelectorAll('#cocina-tb tr');
    let visibles = 0;

    rows.forEach(row => {
        const badge = row.querySelector('.bdg');
        const txt = (badge?.textContent || '').trim().toUpperCase();
        const isEmpty = row.querySelector('td[colspan]');
        if (isEmpty) return;

        const show = estado === 'TODOS' || txt === estado || (estado === 'PENDIENTE' && txt === 'PENDIENTE') || (estado === 'PREPARANDO' && txt === 'PREPARANDO') || (estado === 'LISTO' && txt === 'LISTO');
        row.style.display = show ? '' : 'none';
        if (show) visibles++;
    });

    const info = document.getElementById('cocina-info');
    if (info) info.textContent = visibles + ' registros';
}

// - Cerrar modal -
function cMo(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) modal.classList.remove('open');
}

function oMo(title, bodyHtml) {
    const modal = document.getElementById('mo-main');
    const titleEl = document.getElementById('mo-title');
    const bodyEl = document.getElementById('mo-body');
    const saveBtn = document.getElementById('mo-btn');

    if (!modal || !titleEl || !bodyEl) return false;
    titleEl.textContent = title;
    bodyEl.innerHTML = bodyHtml;
    if (saveBtn) {
        saveBtn.onclick = () => cMo('mo-main');
    }
    modal.classList.add('open');
    return false;
}

function fComp() {
    return `
        <div class="fg"><label>Codigo</label><input type="text" placeholder="TC004"></div>
        <div class="fg"><label>Nombre</label><input type="text" placeholder="Nota de venta"></div>
        <p style="font-size:.75rem;color:var(--mu);">Formulario visual listo para conectar con tipo_comprobante.</p>`;
}

function fDoc() {
    return `
        <div class="fg"><label>Codigo</label><input type="text" placeholder="TD003"></div>
        <div class="fg"><label>Nombre</label><input type="text" placeholder="Carnet extranjeria"></div>
        <p style="font-size:.75rem;color:var(--mu);">Formulario visual listo para conectar con tipo_documento.</p>`;
}

function fMP() {
    return `
        <div class="fg"><label>Codigo</label><input type="text" placeholder="MP005"></div>
        <div class="fg"><label>Nombre</label><input type="text" placeholder="Transferencia"></div>
        <p style="font-size:.75rem;color:var(--mu);">Formulario visual listo para conectar con metodo_pago.</p>`;
}

// - Filtrar usuarios por nombre o usuario
function filtrarUsuarios() {
    const q    = document.getElementById('usr-buscar').value.toLowerCase();
    const rows = document.querySelectorAll('#tb-usuarios tr');
    let count  = 0;
    rows.forEach(row => {
        const txt = row.textContent.toLowerCase();
        const show = txt.includes(q);
        row.style.display = show ? '' : 'none';
        if (show) count++;
    });
    const c = document.getElementById('usr-count');
    if (c) c.textContent = count + ' registros';
}

// - Abrir modal editar usuario
function abrirEditar(cod, nombre, dni, rol, estado) {
    document.getElementById('edit-cod-orig').value = cod;
    document.getElementById('edit-cod').value      = cod;
    document.getElementById('edit-nombre').value   = nombre;
    document.getElementById('edit-dni').value      = dni;
    document.getElementById('edit-rol').value      = rol;
    document.getElementById('mo-editar-usr').classList.add('open');
}

// - Confirmar desactivar usuario
function confirmarDesactivar(cod) {
    Swal.fire({
        title: 'Desactivar usuario?',
        text: 'El usuario no podra iniciar sesion.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#e63946',
        cancelButtonColor: '#6b7280',
        confirmButtonText: 'Si, desactivar',
        cancelButtonText: 'Cancelar'
    }).then(r => {
        if (r.isConfirmed) {
            window.location.href = '/usuarios/desactivar?cod=' + encodeURIComponent(cod);
        }
    });
}

// - Confirmar activar usuario
function confirmarActivar(cod) {
    Swal.fire({
        title: 'Activar usuario?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#2dc653',
        cancelButtonColor: '#6b7280',
        confirmButtonText: 'Si, activar',
        cancelButtonText: 'Cancelar'
    }).then(r => {
        if (r.isConfirmed) {
            window.location.href = '/usuarios/activar?cod=' + encodeURIComponent(cod);
        }
    });
}

function filtrarPlatosAdmin() {
    const input = document.getElementById('buscar-plato-admin');
    const rows = document.querySelectorAll('#tb-platos-admin tr');
    const count = document.getElementById('platos-admin-count');
    const q = (input?.value || '').toLowerCase();
    let visibles = 0;

    rows.forEach(row => {
        const show = row.textContent.toLowerCase().includes(q);
        row.style.display = show ? '' : 'none';
        if (show && !row.querySelector('td[colspan]')) visibles++;
    });

    if (count) count.textContent = visibles + ' registros';
}

function confirmarDesactivarAlimento(cod) {
    Swal.fire({
        title: 'Ocultar plato?',
        text: 'El cliente ya no vera este plato en la carta.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#e63946',
        cancelButtonColor: '#6b7280',
        confirmButtonText: 'Si, ocultar',
        cancelButtonText: 'Cancelar'
    }).then(r => {
        if (r.isConfirmed) {
            window.location.href = '/alimentos/desactivar?cod=' + encodeURIComponent(cod);
        }
    });
}

function confirmarActivarAlimento(cod) {
    Swal.fire({
        title: 'Mostrar plato?',
        text: 'El cliente podra verlo nuevamente en la carta.',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#2dc653',
        cancelButtonColor: '#6b7280',
        confirmButtonText: 'Si, mostrar',
        cancelButtonText: 'Cancelar'
    }).then(r => {
        if (r.isConfirmed) {
            window.location.href = '/alimentos/activar?cod=' + encodeURIComponent(cod);
        }
    });
}

function abrirEditarAlimento(data) {
    document.getElementById('edit-ali-cod').value = data.cod || '';
    document.getElementById('edit-ali-cat').value = data.categoria || '';
    document.getElementById('edit-ali-nombre').value = data.nombre || '';
    document.getElementById('edit-ali-descripcion').value = data.descripcion || '';
    document.getElementById('edit-ali-img-actual').value = data.img || '';
    document.getElementById('edit-ali-img-preview').textContent = data.img ? 'Imagen actual: ' + data.img : 'Sin imagen';
    document.getElementById('edit-ali-descuento').value = data.descuento || 0;
    document.getElementById('edit-ali-precio').value = data.precio || 0;
    document.getElementById('edit-ali-estado').value = data.estado || 'ACT';
    document.getElementById('mo-editar-ali').classList.add('open');
}

// - MESAS -

function confirmarDesactivarMesa(nro) {
    Swal.fire({
        title: 'Desactivar mesa?',
        text: 'La mesa ' + nro + ' quedara inactiva.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Si, desactivar',
        cancelButtonText: 'Cancelar',
        confirmButtonColor: '#e74c3c'
    }).then(result => {
        if (result.isConfirmed) {
            window.location.href = '/mesas/desactivar/' + nro;
        }
    });
}

function confirmarActivarMesa(nro) {
    Swal.fire({
        title: 'Activar mesa?',
        text: 'La mesa ' + nro + ' quedara activa.',
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: 'Si, activar',
        cancelButtonText: 'Cancelar',
        confirmButtonColor: '#2ecc71'
    }).then(result => {
        if (result.isConfirmed) {
            window.location.href = '/mesas/activar/' + nro;
        }
    });
}

// - DOMContentLoaded -
document.addEventListener('DOMContentLoaded', () => {

    // Editar usuario
    document.querySelectorAll('.btn-editar-usr').forEach(btn => {
        btn.addEventListener('click', () => {
            abrirEditar(
                btn.dataset.cod,
                btn.dataset.nombre,
                btn.dataset.dni,
                btn.dataset.rol,
                btn.dataset.estado
            );
        });
    });

    // Editar alimento
    document.querySelectorAll('.btn-editar-ali').forEach(btn => {
        btn.addEventListener('click', () => abrirEditarAlimento(btn.dataset));
    });

    // Editar mesa
    document.querySelectorAll('.btn-editar-mesa').forEach(btn => {
        btn.addEventListener('click', function () {
            document.getElementById('edit-mesa-nro').value  = this.dataset.nro;
            document.getElementById('edit-mesa-desc').value = this.dataset.desc;
            document.getElementById('edit-mesa-cap').value  = this.dataset.cap;
            document.getElementById('edit-mesa-est').value  = this.dataset.est;
            document.getElementById('mo-editar-mesa').classList.add('open');
        });
    });

    // Cargar siguiente NroMesa al abrir modal nueva mesa
    const btnNuevaMesa = document.getElementById('btn-nueva-mesa');
    if (btnNuevaMesa) {
        btnNuevaMesa.addEventListener('click', function () {
            fetch('/mesas/siguiente-nro')
                .then(res => res.text())
                .then(nro => {
                    document.querySelector('#mo-mesa input[name="nroMesa"]').value = nro;
                });
            document.getElementById('mo-mesa').classList.add('open');
        });
    }

    // Navegacion posterior a un CRUD.
    const params = new URLSearchParams(window.location.search);
    const sec = params.get('sec');
    const tab = params.get('tab');
    const msg = params.get('msg');

    // Ir a seccion segun parametro URL.
    if (sec) {
        const link = document.querySelector(`[onclick*="showSec('${sec}'"]`)
            || document.querySelector(`[onclick*="showTab('${sec}'"]`);
        if (link) link.click();

        // Si ademas viene tab, activar ese tab especifico.
        if (tab) {
            setTimeout(() => {
                sTabById(sec, tab);
                activarBotonTab(sec, tab);
            }, 50);
        }
    }
});

function verDetallePedidoDelivery(nroPedido) {
    const cont = document.getElementById('delivery-detalle');
    if (!cont) return;

    cont.innerHTML = `
        <i class="fas fa-spinner fa-spin" style="font-size:2rem;margin-bottom:.5rem;display:block;color:var(--bd);"></i>
        Cargando detalle...`;

    fetch('/pedidos/detalle/' + encodeURIComponent(nroPedido), { cache: 'no-store' })
        .then(res => res.json().then(data => ({ ok: res.ok, data })))
        .then(({ ok, data }) => {
            if (!ok) {
                cont.innerHTML = '<p style="color:var(--mu);">No se pudo cargar el detalle del pedido.</p>';
                return;
            }

            const detalles = (data.detalles || []).map(d => `
                <tr>
                    <td>${d.nombre}</td>
                    <td>${d.categoria || ''}</td>
                    <td>${d.cantidad}</td>
                    <td>S/ ${Number(d.precio || 0).toFixed(2)}</td>
                    <td>S/ ${Number(d.subTotal || 0).toFixed(2)}</td>
                </tr>
            `).join('') || `
                <tr>
                    <td colspan="5" style="text-align:center;color:var(--mu);padding:1rem;">Sin detalles registrados.</td>
                </tr>`;

            cont.innerHTML = `
                <div style="text-align:left;margin-bottom:1rem;">
                    <p><strong>Pedido:</strong> ${data.nroPedido}</p>
                    <p><strong>Cliente:</strong> ${data.codCliente}</p>
                    <p><strong>Direccion:</strong> ${data.direccion || '-'}</p>
                    <p><strong>Telefono:</strong> ${data.telefono || '-'}</p>
                    <p><strong>Estado:</strong> ${data.estado || '-'}</p>
                    ${data.descripcion ? `<p><strong>Referencia:</strong> ${data.descripcion}</p>` : ''}
                    <p><strong>Total:</strong> S/ ${Number(data.total || 0).toFixed(2)}</p>
                </div>
                <div class="table-wrap">
                    <table>
                        <thead>
                            <tr>
                                <th>Plato</th>
                                <th>Categoria</th>
                                <th>Cantidad</th>
                                <th>Precio</th>
                                <th>Subtotal</th>
                            </tr>
                        </thead>
                        <tbody>${detalles}</tbody>
                    </table>
                </div>`;
        })
        .catch(() => {
            cont.innerHTML = '<p style="color:var(--mu);">No se pudo cargar el detalle del pedido.</p>';
        });
}

document.addEventListener('DOMContentLoaded', () => {
    const msg = new URLSearchParams(window.location.search).get('msg');
    mostrarMensajeDashboard(msg);

    const cocinaFiltro = document.getElementById('cocina-filtro-estado');
    if (cocinaFiltro) {
        cocinaFiltro.addEventListener('change', filtrarCocinaPorEstado);
        filtrarCocinaPorEstado();
    }

    /*SECCION PEDIDOS - RESERVAS */
    // Cargar siguiente CodReserva al abrir modal
    const btnNuevaReserva = document.getElementById('btn-nueva-reserva');
    if (btnNuevaReserva) {
        btnNuevaReserva.addEventListener('click', function () {
            fetch('/reservas/siguiente-cod')
                .then(res => res.text())
                .then(cod => {
                    document.getElementById('res-cod').value = cod;
                });
            document.getElementById('mo-reserva').classList.add('open');
        });
    }

});

// - Sidebar -
function togSidebar() {
    const sb = document.querySelector('.sidebar');
    const overlay = document.querySelector('.sb-overlay');
    sb.classList.toggle('open');
    overlay.classList.toggle('open');
}

window.addEventListener('pageshow', event => {
    if (event.persisted) {
        window.location.reload();
    }
    verificarSesionDashboard();
});

function verificarSesionDashboard() {
    fetch('/session-status', { cache: 'no-store' })
        .then(res => res.json())
        .then(data => {
            if (!data.logged) {
                window.location.replace('/login');
            } else if (data.rol === 'CLI') {
                window.location.replace('/cliente');
            }
        })
        .catch(() => {});
}

/*SECCION RESERVAS*/

function confirmarCancelarReserva(cod) {
    Swal.fire({
        title: 'Cancelar reserva?',
        text: 'La reserva ' + cod + ' quedara cancelada.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Si, cancelar',
        cancelButtonText: 'No',
        confirmButtonColor: '#e74c3c'
    }).then(result => {
        if (result.isConfirmed) {
            window.location.href = '/reservas/cancelar/' + cod;
        }
    });
}

function confirmarReactivarReserva(cod) {
    Swal.fire({
        title: 'Reactivar reserva?',
        text: 'La reserva ' + cod + ' quedara activa.',
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: 'Si, reactivar',
        cancelButtonText: 'No',
        confirmButtonColor: '#2ecc71'
    }).then(result => {
        if (result.isConfirmed) {
            window.location.href = '/reservas/reactivar/' + cod;
        }
    });
}

let reservaPedidoActual = null;
let reservaPedidoItems = [];

function abrirTomarPedidoReserva(data) {
    reservaPedidoActual = {
        cod: data.cod || '',
        mesa: data.mesa || '',
        cliente: data.cliente || '',
        fecha: data.fecha || ''
    };
    reservaPedidoItems = [];
    document.getElementById('rp-cod').textContent = reservaPedidoActual.cod;
    document.getElementById('rp-mesa').textContent = reservaPedidoActual.mesa;
    document.getElementById('rp-cli').textContent = reservaPedidoActual.cliente;
    document.getElementById('rp-plato').value = '';
    document.getElementById('rp-cantidad').value = 1;
    renderItemsReservaPedido();
    document.getElementById('mo-res-pedido').classList.add('open');
}

function agregarItemReservaPedido() {
    const sel = document.getElementById('rp-plato');
    const cantidad = parseInt(document.getElementById('rp-cantidad').value || '0', 10);
    const opt = sel?.selectedOptions?.[0];
    if (!opt || !opt.value) {
        Swal.fire({icon:'warning', title:'Selecciona un plato'});
        return;
    }
    if (!cantidad || cantidad < 1) {
        Swal.fire({icon:'warning', title:'Cantidad invalida'});
        return;
    }
    const precio = parseFloat(opt.dataset.precio || '0');
    const item = {
        id: opt.value,
        nombre: opt.dataset.nombre || opt.textContent,
        categoria: opt.dataset.categoria || '',
        precio,
        cantidad
    };
    const idx = reservaPedidoItems.findIndex(x => x.id === item.id);
    if (idx >= 0) {
        reservaPedidoItems[idx].cantidad += item.cantidad;
    } else {
        reservaPedidoItems.push(item);
    }
    renderItemsReservaPedido();
}

function quitarItemReservaPedido(id) {
    reservaPedidoItems = reservaPedidoItems.filter(x => x.id !== id);
    renderItemsReservaPedido();
}

function renderItemsReservaPedido() {
    const tb = document.getElementById('rp-items');
    const totalEl = document.getElementById('rp-total');
    if (!tb) return;
    if (!reservaPedidoItems.length) {
        tb.innerHTML = `<tr><td colspan="6" style="text-align:center;color:var(--mu);padding:1.5rem;">Todavia no agregaste platos.</td></tr>`;
        if (totalEl) totalEl.textContent = 'S/ 0.00';
        return;
    }
    let total = 0;
    tb.innerHTML = reservaPedidoItems.map(it => {
        const sub = it.precio * it.cantidad;
        total += sub;
        return `<tr>
            <td>${it.nombre}</td>
            <td>${it.categoria || '-'}</td>
            <td>${it.cantidad}</td>
            <td>S/ ${it.precio.toFixed(2)}</td>
            <td>S/ ${sub.toFixed(2)}</td>
            <td><button type="button" class="bta del" onclick="quitarItemReservaPedido('${it.id}')"><i class="fas fa-trash"></i></button></td>
        </tr>`;
    }).join('');
    if (totalEl) totalEl.textContent = 'S/ ' + total.toFixed(2);
}

function confirmarTomarPedidoReserva() {
    if (!reservaPedidoActual?.cod) {
        Swal.fire({icon:'error', title:'Reserva no valida'});
        return;
    }
    if (!reservaPedidoItems.length) {
        Swal.fire({icon:'warning', title:'Agrega al menos un plato'});
        return;
    }

    Swal.fire({
        title: 'Tomar pedido?',
        text: 'Se generara el pedido y la reserva pasara a atendida.',
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: 'Si, generar',
        cancelButtonText: 'Cancelar',
        confirmButtonColor: '#3a5ba0'
    }).then(r => {
        if (!r.isConfirmed) return;
        fetch('/reservas/tomar-pedido', {
            method: 'POST',
            headers: {'Content-Type':'application/json'},
            body: JSON.stringify({
                codReserva: reservaPedidoActual.cod,
                items: reservaPedidoItems.map(it => ({
                    id: it.id,
                    precio: it.precio,
                    cantidad: it.cantidad
                }))
            })
        })
        .then(async res => {
            const data = await res.json().catch(() => ({}));
            if (!res.ok) throw new Error(data.error || 'No se pudo generar el pedido');
            return data;
        })
        .then(data => {
            cMo('mo-res-pedido');
            Swal.fire({
                icon: 'success',
                title: 'Pedido generado',
                text: 'Se creo el pedido ' + data.nroPedido + ' desde la reserva.'
            }).then(() => window.location.reload());
        })
        .catch(err => {
            Swal.fire({icon:'error', title:'Error', text: err.message || 'No se pudo generar el pedido'});
        });
    });
}

// =============================================================================
// SECCIÓN PAGOS — pegar al final de dashboard2.js
// =============================================================================

// ── Cargar todo al entrar a la sección Pagos ─────────────────────────────────
function cargarSeccionPagos() {
    cargarPedidosCobrables();
    cargarSelectTiposComprobante();
    cargarSelectMetodosPago();
    cargarPagosEmitidos();
}

// ── Tabla izquierda: pedidos en estado LISTO ──────────────────────────────────
function cargarPedidosCobrables() {
    const tb   = document.getElementById('pag-tb');
    const info = document.getElementById('pag-info');
    if (!tb) return;

    tb.innerHTML = `<tr><td colspan="6" style="text-align:center;padding:1.5rem;">
        <i class="fas fa-spinner fa-spin"></i> Cargando...</td></tr>`;

    fetch('/pagos/pedidos-cobrables', { cache: 'no-store' })
        .then(res => res.json())
        .then(data => {
            if (!data.length) {
                tb.innerHTML = `<tr><td colspan="6" style="text-align:center;color:var(--mu);padding:2rem;">
                    No hay pedidos listos para cobrar.</td></tr>`;
                if (info) info.textContent = '0 registros';

                // Actualizar tarjeta "Por cobrar"
                const tarjeta = document.querySelector('#sec-pagos .stat-num');
                if (tarjeta) tarjeta.textContent = '0';
                return;
            }

            tb.innerHTML = data.map(p => `
                <tr>
                    <td><strong>${p.nroPedido}</strong></td>
                    <td>${p.codCliente}</td>
                    <td>${p.esLocal}</td>
                    <td>S/ ${Number(p.total || 0).toFixed(2)}</td>
                    <td><span class="bdg grn">LISTO</span></td>
                    <td>
                        <button class="bta" title="Seleccionar para cobrar"
                            onclick="seleccionarPedidoCobro('${p.nroPedido}', ${p.total})">
                            <i class="fas fa-hand-pointer"></i> Cobrar
                        </button>
                    </td>
                </tr>`).join('');

            if (info) info.textContent = data.length + ' registros';

            // Actualizar tarjeta "Por cobrar"
            const tarjeta = document.querySelector('#sec-pagos .stat-num');
            if (tarjeta) tarjeta.textContent = data.length;
        })
        .catch(() => {
            tb.innerHTML = `<tr><td colspan="6" style="text-align:center;color:var(--mu);padding:2rem;">
                No se pudo cargar los pedidos.</td></tr>`;
        });
}

// ── Select tipos de comprobante desde BD ─────────────────────────────────────
function cargarSelectTiposComprobante() {
    const sel = document.getElementById('cp-tipo');
    if (!sel) return;

    fetch('/pagos/tipos-comprobante')
        .then(res => res.json())
        .then(data => {
            sel.innerHTML = data.map(t =>
                `<option value="${t.codTipoComprobante}">${t.nombre}</option>`
            ).join('');
            // Al cargar, actualizar preview del NroComprobante
            actualizarNroComprobante();
        })
        .catch(() => { /* mantiene opciones estáticas del HTML */ });
}

// ── Select métodos de pago desde BD ──────────────────────────────────────────
function cargarSelectMetodosPago() {
    const sel = document.getElementById('cp-mp');
    if (!sel) return;

    fetch('/pagos/metodos-pago')
        .then(res => res.json())
        .then(data => {
            sel.innerHTML = data.map(m =>
                `<option value="${m.codMetodoPago}">${m.nombre}</option>`
            ).join('');
        })
        .catch(() => { /* mantiene opciones estáticas del HTML */ });
}

// ── Al cambiar tipo de comprobante, actualizar NroComprobante preview ─────────
function actualizarNroComprobante() {
    const sel  = document.getElementById('cp-tipo');
    const inp  = document.getElementById('cp-nro');
    if (!sel || !inp) return;

    const tipo = sel.value;
    fetch('/pagos/nro-comprobante?tipo=' + encodeURIComponent(tipo))
        .then(res => res.json())
        .then(data => { inp.value = data.nroComprobante || inp.value; })
        .catch(() => {});
}

// ── Seleccionar pedido desde tabla y cargar en formulario ─────────────────────
function seleccionarPedidoCobro(nroPedido, total) {
    const selPed   = document.getElementById('cp-ped');
    const inpMonto = document.getElementById('cp-monto');

    // Insertar/actualizar option con el pedido seleccionado
    if (selPed) {
        // Limpiar opciones anteriores y agregar la seleccionada
        selPed.innerHTML = `<option value="${nroPedido}">${nroPedido} — S/ ${Number(total).toFixed(2)}</option>`;
    }
    // Pre-llenar monto con el total del pedido
    if (inpMonto) inpMonto.value = Number(total).toFixed(2);

    // Actualizar NroComprobante
    actualizarNroComprobante();

    // Scroll al formulario
    document.getElementById('cp-ped')?.scrollIntoView({ behavior: 'smooth', block: 'center' });
}

// ── Botón principal: Cobrar y generar comprobante ─────────────────────────────
function genComp() {
    const nroPedido     = document.getElementById('cp-ped')?.value;
    const codTipo       = document.getElementById('cp-tipo')?.value;
    const codMetodo     = document.getElementById('cp-mp')?.value;
    const montoRecibido = parseFloat(document.getElementById('cp-monto')?.value || '0');

    if (!nroPedido) {
        Swal.fire({ icon: 'warning', title: 'Selecciona un pedido', text: 'Haz clic en "Cobrar" en la tabla de la izquierda.' });
        return;
    }
    if (!montoRecibido || montoRecibido <= 0) {
        Swal.fire({ icon: 'warning', title: 'Monto inválido', text: 'Ingresa el monto recibido.' });
        return;
    }

    Swal.fire({
        title: 'Confirmar cobro',
        html: `
            <p><strong>Pedido:</strong> ${nroPedido}</p>
            <p><strong>Monto recibido:</strong> S/ ${montoRecibido.toFixed(2)}</p>
            <p>¿Deseas generar el comprobante?</p>`,
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: 'Sí, cobrar',
        cancelButtonText: 'Cancelar',
        confirmButtonColor: '#059669'
    }).then(r => {
        if (!r.isConfirmed) return;

        fetch('/pagos/cobrar', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                nroPedido,
                codTipoComprobante: codTipo,
                codMetodoPago: codMetodo,
                montoRecibido
            })
        })
            .then(async res => {
                const data = await res.json().catch(() => ({}));
                if (!res.ok) throw new Error(data.error || 'No se pudo registrar el pago');
                return data;
            })
            .then(data => {
                Swal.fire({
                    icon: 'success',
                    title: 'Cobro registrado',
                    html: `
                    <p><strong>Comprobante:</strong> ${data.nroComprobante}</p>
                    <p><strong>Total:</strong> S/ ${Number(data.total).toFixed(2)}</p>
                    <p><strong>Vuelto:</strong> S/ ${Number(data.vuelto).toFixed(2)}</p>`
                }).then(() => {
                    // Resetear formulario y recargar tablas
                    document.getElementById('cp-ped').innerHTML = '<option value="">Seleccionar pedido...</option>';
                    document.getElementById('cp-monto').value = '';
                    cargarPedidosCobrables();
                    cargarPagosEmitidos();
                    actualizarNroComprobante();
                });
            })
            .catch(err => {
                Swal.fire({ icon: 'error', title: 'Error', text: err.message || 'No se pudo registrar el pago' });
            });
    });
}

// ── Tabla inferior: pagos emitidos ────────────────────────────────────────────
function cargarPagosEmitidos() {
    // Buscar el tbody de la tabla "Pagos emitidos" (segunda tabla en la sección)
    const secPagos = document.getElementById('sec-pagos');
    if (!secPagos) return;

    const tablas = secPagos.querySelectorAll('table');
    const tablaEmitidos = tablas[1]; // segunda tabla = Pagos emitidos
    if (!tablaEmitidos) return;

    const tb = tablaEmitidos.querySelector('tbody');
    if (!tb) return;

    tb.innerHTML = `<tr><td colspan="6" style="text-align:center;padding:1.5rem;">
        <i class="fas fa-spinner fa-spin"></i> Cargando...</td></tr>`;

    fetch('/pagos/emitidos', { cache: 'no-store' })
        .then(res => res.json())
        .then(data => {
            if (!data.length) {
                tb.innerHTML = `<tr><td colspan="6" style="text-align:center;color:var(--mu);padding:2rem;">
                    No hay pagos emitidos.</td></tr>`;

                // Actualizar tarjetas
                actualizarTarjetasPagos(data);
                return;
            }

            tb.innerHTML = data.map(p => `
                <tr>
                    <td><strong>${p.nroComprobante}</strong><br>
                        <small style="color:var(--mu)">${p.tipoComprobante}</small></td>
                    <td>${p.nroPedido}</td>
                    <td>${p.codCliente}</td>
                    <td>${p.metodoPago}</td>
                    <td>S/ ${Number(p.montoTotal || 0).toFixed(2)}</td>
                    <td>${formatearFecha(p.fecEmision)}</td>
                </tr>`).join('');

            actualizarTarjetasPagos(data);
        })
        .catch(() => {
            tb.innerHTML = `<tr><td colspan="6" style="text-align:center;color:var(--mu);padding:2rem;">
                No se pudo cargar los pagos.</td></tr>`;
        });
}

// ── Actualizar tarjetas del header de Pagos ───────────────────────────────────
function actualizarTarjetasPagos(pagosEmitidos) {
    const statNums = document.querySelectorAll('#sec-pagos .stat-num');
    if (statNums.length >= 2) {
        // Cobrado hoy
        const hoy = new Date().toDateString();
        const cobradoHoy = pagosEmitidos
            .filter(p => new Date(p.fecEmision).toDateString() === hoy)
            .reduce((sum, p) => sum + (p.montoTotal || 0), 0);
        statNums[1].textContent = 'S/ ' + cobradoHoy.toFixed(2);
    }
    if (statNums.length >= 3) {
        statNums[2].textContent = pagosEmitidos.length;
    }
}

// ── Utilidad: formatear fecha ─────────────────────────────────────────────────
function formatearFecha(fechaStr) {
    if (!fechaStr) return '-';
    const d = new Date(fechaStr);
    if (isNaN(d)) return fechaStr;
    return d.toLocaleDateString('es-PE', { day: '2-digit', month: '2-digit', year: 'numeric' })
        + ' ' + d.toLocaleTimeString('es-PE', { hour: '2-digit', minute: '2-digit' });
}

// ── Listeners: inicializar cuando se activa la sección ───────────────────────
document.addEventListener('DOMContentLoaded', () => {

    // Listener en el select de tipo comprobante para actualizar NroComprobante
    const selTipo = document.getElementById('cp-tipo');
    if (selTipo) {
        selTipo.addEventListener('change', actualizarNroComprobante);
    }

    // Si la URL trae ?sec=pagos, cargar datos al iniciar
    const params = new URLSearchParams(window.location.search);
    if (params.get('sec') === 'pagos') {
        cargarSeccionPagos();
    }
});


// =============================================================================
// SECCIÓN REPORTES — pegar al final de dashboard2.js
// =============================================================================

// ── Cargar todo al entrar a la sección Reportes ───────────────────────────────
function cargarSeccionReportes() {
    const desde = document.getElementById('rf-desde')?.value || '';
    const hasta = document.getElementById('rf-hasta')?.value || '';
    cargarResumenReportes(desde, hasta);
    cargarVentasPorPedido(desde, hasta);
    cargarPlatosVendidos(desde, hasta);
    cargarComprobantesReporte(desde, hasta);
}

// ── Tarjetas de resumen ───────────────────────────────────────────────────────
function cargarResumenReportes(desde, hasta) {
    const params = construirParams(desde, hasta);

    fetch('/reportes/resumen' + params, { cache: 'no-store' })
        .then(res => res.json())
        .then(data => {
            const tot  = document.getElementById('rv-tot');
            const ped  = document.getElementById('rv-ped');
            const pop  = document.getElementById('rv-pop');
            const comp = document.getElementById('rv-comp');

            if (tot)  tot.textContent  = 'S/ ' + Number(data.ventasTotales || 0).toFixed(2);
            if (ped)  ped.textContent  = data.pedidos || 0;
            if (pop)  pop.textContent  = data.platoMasVendido || '-';
            if (comp) comp.textContent = data.comprobantes || 0;
        })
        .catch(() => {});
}

// ── Tabla: Ventas por pedido ──────────────────────────────────────────────────
function cargarVentasPorPedido(desde, hasta) {
    const tb   = document.getElementById('rv-tb');
    const info = document.getElementById('rv-info');
    if (!tb) return;

    tb.innerHTML = `<tr><td colspan="5" style="text-align:center;padding:1.5rem;">
        <i class="fas fa-spinner fa-spin"></i> Cargando...</td></tr>`;

    fetch('/reportes/ventas' + construirParams(desde, hasta), { cache: 'no-store' })
        .then(res => res.json())
        .then(data => {
            if (!data.length) {
                tb.innerHTML = `<tr><td colspan="5" style="text-align:center;color:var(--mu);padding:2rem;">
                    No hay ventas en el período seleccionado.</td></tr>`;
                if (info) info.textContent = '0 registros';
                return;
            }
            tb.innerHTML = data.map(v => `
                <tr>
                    <td><strong>${v.nroPedido}</strong></td>
                    <td>${v.codCliente}</td>
                    <td>${v.tipo}</td>
                    <td>S/ ${Number(v.monto || 0).toFixed(2)}</td>
                    <td>${formatearFecha(v.fecha)}</td>
                </tr>`).join('');
            if (info) info.textContent = data.length + ' registros';
        })
        .catch(() => {
            tb.innerHTML = `<tr><td colspan="5" style="text-align:center;color:var(--mu);padding:2rem;">
                No se pudo cargar las ventas.</td></tr>`;
        });
}

// ── Tabla: Platos vendidos ────────────────────────────────────────────────────
function cargarPlatosVendidos(desde, hasta) {
    const tb   = document.getElementById('rp-platos-tb');
    const info = document.getElementById('rp-platos-info');
    if (!tb) return;

    tb.innerHTML = `<tr><td colspan="4" style="text-align:center;padding:1.5rem;">
        <i class="fas fa-spinner fa-spin"></i> Cargando...</td></tr>`;

    fetch('/reportes/platos' + construirParams(desde, hasta), { cache: 'no-store' })
        .then(res => res.json())
        .then(data => {
            if (!data.length) {
                tb.innerHTML = `<tr><td colspan="4" style="text-align:center;color:var(--mu);padding:2rem;">
                    No hay platos vendidos en el período.</td></tr>`;
                if (info) info.textContent = '0 registros';
                return;
            }
            tb.innerHTML = data.map(p => `
                <tr>
                    <td><strong>${p.nombre}</strong></td>
                    <td>${p.categoria}</td>
                    <td>${p.cantidad}</td>
                    <td>S/ ${Number(p.total || 0).toFixed(2)}</td>
                </tr>`).join('');
            if (info) info.textContent = data.length + ' registros';
        })
        .catch(() => {
            tb.innerHTML = `<tr><td colspan="4" style="text-align:center;color:var(--mu);padding:2rem;">
                No se pudo cargar los platos.</td></tr>`;
        });
}

// ── Tabla: Comprobantes emitidos ──────────────────────────────────────────────
function cargarComprobantesReporte(desde, hasta) {
    const tb   = document.getElementById('rlc-tb');
    const info = document.getElementById('rlc-info');
    if (!tb) return;

    tb.innerHTML = `<tr><td colspan="6" style="text-align:center;padding:1.5rem;">
        <i class="fas fa-spinner fa-spin"></i> Cargando...</td></tr>`;

    fetch('/reportes/comprobantes' + construirParams(desde, hasta), { cache: 'no-store' })
        .then(res => res.json())
        .then(data => {
            if (!data.length) {
                tb.innerHTML = `<tr><td colspan="6" style="text-align:center;color:var(--mu);padding:2rem;">
                    No hay comprobantes en el período.</td></tr>`;
                if (info) info.textContent = '0 registros';
                return;
            }
            tb.innerHTML = data.map(c => `
                <tr>
                    <td><strong>${c.nroComprobante}</strong><br>
                        <small style="color:var(--mu)">${c.tipoComprobante}</small></td>
                    <td>${c.nroPedido}</td>
                    <td>${c.codCliente}</td>
                    <td>${c.metodoPago}</td>
                    <td>S/ ${Number(c.monto || 0).toFixed(2)}</td>
                    <td>${formatearFecha(c.fecha)}</td>
                </tr>`).join('');
            if (info) info.textContent = data.length + ' registros';
        })
        .catch(() => {
            tb.innerHTML = `<tr><td colspan="6" style="text-align:center;color:var(--mu);padding:2rem;">
                No se pudo cargar los comprobantes.</td></tr>`;
        });
}

// ── Utilidad: construir query params ─────────────────────────────────────────
function construirParams(desde, hasta) {
    const p = [];
    if (desde) p.push('desde=' + encodeURIComponent(desde));
    if (hasta) p.push('hasta=' + encodeURIComponent(hasta));
    return p.length ? '?' + p.join('&') : '';
}

// ── DOMContentLoaded: listeners de reportes ───────────────────────────────────
document.addEventListener('DOMContentLoaded', () => {

    // Botón filtrar
    const btnFiltrar = document.querySelector('#sec-reportes .btn-n');
    if (btnFiltrar) {
        btnFiltrar.addEventListener('click', () => {
            const desde = document.getElementById('rf-desde')?.value || '';
            const hasta = document.getElementById('rf-hasta')?.value || '';
            cargarSeccionReportes();
        });
    }

    // Si la URL trae ?sec=reportes, cargar al iniciar
    const params = new URLSearchParams(window.location.search);
    if (params.get('sec') === 'reportes') {
        cargarSeccionReportes();
    }
});