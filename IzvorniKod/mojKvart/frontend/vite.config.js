import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import { loadEnv } from 'vite';

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');

  return {
    plugins: [react()],
    server: {
      port: 3000,
      proxy: {
        '/api': {
          target: env.VITE_BACKEND_URL,
          //changeOrigin: true,
          //secure: false,
        },
      },
    },
    define: {
      __APP_ENV__: env.APP_ENV,
    },
  };
});
