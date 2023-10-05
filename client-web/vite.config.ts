import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  build: {
    outDir: '../src/main/resources/client-web',
    emptyOutDir: true,
    rollupOptions: {
      input: {
        index: './index.html',
        error: './error.html',
      },
      output: {
        entryFileNames: `assets/[hash].js`,
        chunkFileNames: `assets/[hash].js`,
        assetFileNames: `assets/[hash].[ext]`
      }
    }
  },
  server: {
    port: 8080,
    proxy: {
      '/oauth2': 'http://localhost:8083',
      '/api': 'http://localhost:8083',
    }
  }
})