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
      port: 3000,
      strictPort: true,
    },
    server: {
    port: 3000,
    strictPort: true,
    host: true,
      proxy: {
        '/api': {
          target: "https://kvartasialfabackend.onrender.com",
          secure: true,
        },
      },
    },
    define: {
      __APP_ENV__: env.APP_ENV,
    },
  };
});