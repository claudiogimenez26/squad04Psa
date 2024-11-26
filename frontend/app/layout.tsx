import "./globals.css";

import RecursoActual from "@/_componentes/RecursoActual";
import RecursoContextProvider from "@/_context/recursoContext";
import { Recurso } from "@/_lib/tipos";
import {
  Navbar,
  NavbarBrand,
  NavbarCollapse,
  NavbarLink
} from "flowbite-react";
import { Inter } from "next/font/google";

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
        <RecursoContextProvider recursos={recursos}>
          <Navbar className="border-b">
            <NavbarBrand>
              <span className="self-center text-xl font-semibold">PSA</span>
            </NavbarBrand>
            <NavbarCollapse>
              <NavbarLink href="/consultar-cargas">Consultar cargas</NavbarLink>
              <NavbarLink href="/consultar-costos">Consultar costos</NavbarLink>
              <NavbarLink href="/carga-de-horas">Cargar horas</NavbarLink>
              <NavbarLink href="/">
                <RecursoActual />
              </NavbarLink>
            </NavbarCollapse>
          </Navbar>
          <main className="container mx-auto p-6">{children}</main>
        </RecursoContextProvider>
      </body>
    </html>
  );
}
