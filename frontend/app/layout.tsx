import "./globals.css";

import { Inter } from "next/font/google";
import {
  Navbar,
  NavbarBrand,
  NavbarCollapse,
  NavbarLink,
} from "flowbite-react";
import { Recurso } from "@/app/lib/types";
import { LoginContextProvider } from "@/app/context/LoginContext";
import PerfilUsuario from "./PerfilUsuario";

const inter = Inter({
  subsets: ["latin"],
  display: "swap",
  variable: "--font-inter",
});

export default async function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const data = await fetch("http://localhost:8080/recursos");
  const recursos: Recurso[] = await data.json();

  return (
    <html lang="en">
      <body className={`${inter.variable} antialiased font-sans`}>
        <LoginContextProvider recurso={recursos[0]}>
          <Navbar className="border-b">
            <NavbarBrand>
              <span className="self-center text-xl font-semibold">PSA</span>
            </NavbarBrand>
            <NavbarCollapse>
              <NavbarLink href="/">Consultar cargas</NavbarLink>
              <NavbarLink href="/carga-de-horas">Cargar horas</NavbarLink>
              <NavbarLink href="/login">
                <PerfilUsuario />
              </NavbarLink>
            </NavbarCollapse>
          </Navbar>
          <main className="container mx-auto my-6">{children}</main>
        </LoginContextProvider>
      </body>
    </html>
  );
}
