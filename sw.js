const CACHE_NAME = 'retro-os-cores-v1';

// Recursos estáticos básicos de la interfaz para que cargue la app estando offline
const INITIAL_ASSETS = [
    './',
    './index.html',
    'https://cdn.tailwindcss.com',
    'https://unpkg.com/lucide@latest'
];

// Instalar el Service Worker y almacenar estilos base
self.addEventListener('install', (e) => {
    e.waitUntil(
        caches.open(CACHE_NAME).then((cache) => {
            return cache.addAll(INITIAL_ASSETS);
        }).then(() => self.skipWaiting())
    );
});

// Activar el worker y tomar el control de la red inmediatamente
self.addEventListener('activate', (e) => {
    e.waitUntil(self.clients.claim());
});

// Interceptor estratégico: Cachea dinámicamente las Cores (.wasm y .js) cuando juegas por primera vez
self.addEventListener('fetch', (e) => {
    const url = e.request.url;

    // Solo interceptar peticiones locales o dirigidas a los recursos del emulador
    if (url.includes('emulatorjs.org') || url.includes(location.origin)) {
        e.respondWith(
            caches.match(e.request).then((cachedResponse) => {
                if (cachedResponse) {
                    // Si ya existe de forma local, se sirve instantáneamente desde la memoria interna
                    return cachedResponse;
                }

                // Si no existe, lo descarga por red, lo clona y lo guarda automáticamente
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
                    // Fallback opcional por si no hay datos ni internet
                    return new Response("Recurso offline no disponible", { status: 503 });
                });
            })
        );
    }
});
