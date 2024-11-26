import "./globals.css";

import RecursoActualContextProvider from "@/_context/recursoActualContext";
import { Recurso } from "@/_lib/tipos";
import {
  Navbar,
  NavbarBrand,
  NavbarCollapse,
  NavbarLink
} from "flowbite-react";
import { Inter } from "next/font/google";
import IndicadorRecursoActual from "./IndicadorRecursoActual";

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
  const res = await fetch(`${process.env.BACKEND_URL}/recursos`);
  const recursos: Recurso[] = await res.json();

  return (
    <html lang="en">
      <body className={`${inter.variable} font-sans`}>
        <RecursoActualContextProvider recursos={recursos}>
          <Navbar className="border-b">
            <NavbarBrand>
              <span className="self-center text-xl font-semibold">PSA</span>
            </NavbarBrand>
            <NavbarCollapse>
              <NavbarLink href="/recursos">Cargas de recursos</NavbarLink>
              <NavbarLink href="/proyectos">Costos por proyectos</NavbarLink>
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
