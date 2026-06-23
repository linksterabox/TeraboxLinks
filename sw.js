const CACHE_NAME = 'retro-os-cores-v1';

// ✅ RUTAS RELATIVAS OBLIGATORIAS EN GITHUB PAGES
const INITIAL_ASSETS = [
    './',
    './index.html',
    'https://cdn.tailwindcss.com',
    'https://unpkg.com/lucide@latest'
];

self.addEventListener('install', e => {
    e.waitUntil(
        caches.open(CACHE_NAME).then(cache => cache.addAll(INITIAL_ASSETS))
             .then(() => self.skipWaiting())
    );
});

self.addEventListener('activate', e => {
    e.waitUntil(self.clients.claim());
});

self.addEventListener('fetch', e => {
    const url = e.request.url;
    // ✅ Guardar dinámicamente emulador y archivos locales
    if (url.includes('emulatorjs.org') || url.includes(location.origin)) {
        e.respondWith(
            caches.match(e.request).then(cached => {
                if (cached) return cached;
                return fetch(e.request).then(net => {
                    if (!net || net.status !== 200) return net;
                    const copy = net.clone();
                    caches.open(CACHE_NAME).then(cache => cache.put(e.request, copy));
                    return net;
                }).catch(() => new Response("Sin conexión", { status: 503 }));
            })
        );
    }
});
