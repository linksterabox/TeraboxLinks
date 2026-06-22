const CACHE_NAME = 'retro-os-cores-v1';

// Recursos estáticos corregidos con rutas relativas para GitHub Pages
const INITIAL_ASSETS = [
    './',
    './index.html',
    'https://cdn.tailwindcss.com',
    'https://unpkg.com/lucide@latest'
];

self.addEventListener('install', (e) => {
    e.waitUntil(
        caches.open(CACHE_NAME).then((cache) => {
            return cache.addAll(INITIAL_ASSETS);
        }).then(() => self.skipWaiting())
    );
});

self.addEventListener('activate', (e) => {
    e.waitUntil(self.clients.claim());
});

self.addEventListener('fetch', (e) => {
    const url = e.request.url;

    // Intercepta recursos locales y del emulador CDN para guardarlos dinámicamente al jugar la primera vez
    if (url.includes('emulatorjs.org') || url.includes(location.origin)) {
        e.respondWith(
            caches.match(e.request).then((cachedResponse) => {
                if (cachedResponse) {
                    return cachedResponse;
                }

                return fetch(e.request).then((networkResponse) => {
                    if (!networkResponse || networkResponse.status !== 200) {
                        return networkResponse;
                    }

                    const responseToCache = networkResponse.clone();
                    caches.open(CACHE_NAME).then((cache) => {
                        cache.put(e.request, responseToCache);
                    });

                    return networkResponse;
                }).catch(() => {
                    return new Response("Recurso offline no disponible", { status: 503 });
                });
            })
        );
    }
});
