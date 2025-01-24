import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig(({ mode }) => {
  return {
    base: "/",
    plugins: [react()],
    server: {
      port: 3000,
      strictPort: true,
      host: true,
      proxy: {
        '/api': {
          target: "https://mojkvartbackend.onrender.com",
          changeOrigin: true,
          secure: true
        },
      },
    },
    preview: {
      allowedHosts: ['mojkvartfinalversion.onrender.com'],
    }
  };
});
