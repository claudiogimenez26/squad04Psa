"use client";

import { Recurso } from "@/_lib/tipos";
import { Datepicker, Label } from "flowbite-react";
import { usePathname, useRouter } from "next/navigation";
import React, { useEffect, useState } from "react";
import SelectorRecurso from "./SelectorRecurso";

export default function Formulario({
  recursos,
  children
}: {
  recursos: Recurso[];
  children: React.ReactElement;
}) {
  const router = useRouter();
  const pathName = usePathname();

  const [fechaBusqueda, setFechaBusqueda] = useState<Date | null>(new Date());

  useEffect(() => {
    if (fechaBusqueda) {
      router.push(
        `${pathName}?fecha=${encodeURIComponent(fechaBusqueda.toLocaleDateString("es-AR"))}`
      );
    }
  }, [fechaBusqueda, router]);

  return (
    <>
      <div className="space-y-4">
        <SelectorRecurso recursos={recursos} />
        <div className="space-y-2">
          <Label
            htmlFor="recursoId"
            value="Seleccion una fecha de una semana"
          />
          <Datepicker
            id="fechaCarga"
            className="w-80"
            language="es-AR"
            labelTodayButton="Hoy"
            labelClearButton="Limpiar"
            maxDate={new Date()}
            value={fechaBusqueda}
            onChange={(f) => setFechaBusqueda(f)}
          />
        </div>
      </div>
      {children}
    </>
  );
}
