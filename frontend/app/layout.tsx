import "./globals.css";

import RecursoActualContextProvider from "@/_context/recursoActualContext";
import { CargasDeHorasPorRecurso, Recurso } from "@/_lib/tipos";
import {
  Navbar,
  NavbarBrand,
  NavbarCollapse,
  NavbarLink
} from "flowbite-react";
import { Inter } from "next/font/google";
import IndicadorRecursoActual from "./IndicadorRecursoActual";

export const dynamic = "force-dynamic";

const inter = Inter({
  subsets: ["latin"],
  display: "swap",
  variable: "--font-inter"
});

export default async function RootLayout({
  children
}: {
  children: React.ReactNode;
}) {
  const [recursos, cargas] = (await Promise.all([
    fetch(`${process.env.BACKEND_URL}/recursos`).then((res) => res.json()),
    fetch(`${process.env.BACKEND_URL}/carga-de-horas`).then((res) => res.json())
  ])) as [Recurso[], CargasDeHorasPorRecurso];

  return (
    <html lang="en">
      <body className={`${inter.variable} font-sans`}>
        <RecursoActualContextProvider recursos={recursos} cargas={cargas}>
          <Navbar className="border-b">
            <NavbarBrand>
              <span className="self-center text-xl font-semibold">PSA</span>
            </NavbarBrand>
            <NavbarCollapse>
              <NavbarLink href="/recursos">Horas por recurso</NavbarLink>
              <NavbarLink href="/proyectos">Costos por proyecto</NavbarLink>
              <NavbarLink href="/carga-de-horas">Cargar horas</NavbarLink>
              <NavbarLink href="/">
                <IndicadorRecursoActual />
              </NavbarLink>
            </NavbarCollapse>
          </Navbar>
          <main className="container mx-auto p-6">{children}</main>
        </RecursoActualContextProvider>
      </body>
    </html>
  );
}
