import "./globals.css";

import RecursoContextProvider from "@/_context/recursoContext";
import { Recurso } from "@/_lib/tipos";
import {
  Navbar,
  NavbarBrand,
  NavbarCollapse,
  NavbarLink
} from "flowbite-react";
import { Inter } from "next/font/google";
import RecursoActual from "./_componentes/RecursoActual";

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
      <body
        className={`${inter.variable} antialiased font-sans min-h-dvh flex flex-col`}
      >
        <RecursoContextProvider recursos={recursos}>
          <Navbar className="border-b">
            <NavbarBrand>
              <span className="self-center text-xl font-semibold">PSA</span>
            </NavbarBrand>
            <NavbarCollapse>
              <NavbarLink href="/consultar-cargas">Consultar cargas</NavbarLink>
              <NavbarLink href="/cargar-horas">Cargar horas</NavbarLink>
              <NavbarLink href="/">
                <RecursoActual />
              </NavbarLink>
            </NavbarCollapse>
          </Navbar>
          <main className="flex-1 flex container mx-auto my-6">{children}</main>
        </RecursoContextProvider>
      </body>
    </html>
  );
}
