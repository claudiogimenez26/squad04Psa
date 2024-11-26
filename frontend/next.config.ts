import dotenv from "dotenv";
import dotenvExpand from "dotenv-expand";
import { NextConfig } from "next";
import path from "path";
import { fileURLToPath } from "url";

const PATH_ARCHIVO_DOTENV = "../.env";
const __dirname = path.dirname(fileURLToPath(import.meta.url));

if (process.env.NODE_ENV === "development") {
  try {
    const env = dotenv.config({
      path: path.resolve(__dirname, PATH_ARCHIVO_DOTENV)
    });
    dotenvExpand.expand(env);
    console.info(`Cargado el archivo ${PATH_ARCHIVO_DOTENV}`);
  } catch (error) {
    console.warn(
      `No se pudo cargar el archivo ${PATH_ARCHIVO_DOTENV}, usando variables de entorno locales`
    );
  }
}

const nextConfig: NextConfig = {
  experimental: {
    ppr: "incremental"
  }
};

export default nextConfig;
