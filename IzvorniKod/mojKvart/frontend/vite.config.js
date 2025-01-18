import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import { loadEnv } from 'vite';

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');

  return {
    base: "/",
    plugins: [react()],
    server: {
      port: 3000,
      //strictPort: true,
      //host: true,
      proxy: {
        '/api': {
          target: env.VITE_BACKEND_URL
          //target: "https://kvartasialfabackend.onrender.com",
          //changeOrigin: true,
          //secure: true
        },
      },
    },
    define: {
      __APP_ENV__: env.APP_ENV,
    },
  };
});
