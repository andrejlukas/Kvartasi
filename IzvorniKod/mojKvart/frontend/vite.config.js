import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import { loadEnv } from 'vite';

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');

  return {
    base: "/",
    plugins: [react()],
    preview: {
      port: 8080,
      strictPort: true,
    },
    server: {
    port: 8080,
    strictPort: true,
    host: true,
    origin: "http://0.0.0.0:8080",
      proxy: {
        '/api': {
          target: env.VITE_BACKEND_URL,
        },
      },
    },
    define: {
      __APP_ENV__: env.APP_ENV,
    },
  };
});