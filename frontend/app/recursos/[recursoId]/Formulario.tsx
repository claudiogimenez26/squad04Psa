"use client";

import { desencodearFecha, encodearFecha } from "@/_lib/fecha";
import { Recurso } from "@/_lib/tipos";
import { Datepicker, Label, Select } from "flowbite-react";
import { usePathname, useRouter, useSearchParams } from "next/navigation";
import React, { useEffect, useState } from "react";

export default function Formulario({
  recursos,
  children
}: {
  recursos: Recurso[];
  children: React.ReactElement;
}) {
  const router = useRouter();
  const pathName = usePathname();
  const searchParams = useSearchParams();

  const recursoId = pathName.split("/").at(-1);

  const [recurso, setRecurso] = useState<Recurso | null>(
    recursos.find((r) => r.id === recursoId) || null
  );
  const [fecha, setFecha] = useState<Date | null>(
    desencodearFecha(searchParams.get("fecha")!)
  );

  useEffect(() => {
    if (recurso && fecha) {
      router.push(`/recursos/${recurso.id}?fecha=${encodearFecha(fecha)}`);
    }
  }, [recurso, router]);

  useEffect(() => {
    if (fecha) {
      router.push(`${pathName}?fecha=${encodearFecha(fecha)}`);
    }
  }, [fecha, router]);

  return (
    <>
      <div className="space-y-4">
        <div className="space-y-2">
          <Label htmlFor="recursoId" value="Seleccione un recurso" />
          <Select
            id="recursoId"
            defaultValue={recurso?.id || recursos[0].id}
            onChange={(e) =>
              setRecurso(
                recursos.find((r) => r.id === e.target.value) as Recurso
              )
            }
          >
            {recursos.map((r) => (
              <option key={r.id} value={r.id}>
                {r.dni} - {r.nombre} {r.apellido}
              </option>
            ))}
          </Select>
        </div>
        <div className="space-y-2">
          <Label htmlFor="fechaCarga" value="Seleccion una fecha" />
          <Datepicker
            id="fechaCarga"
            className="w-80"
            language="es-AR"
            labelTodayButton="Hoy"
            labelClearButton="Limpiar"
            maxDate={new Date()}
            value={fecha}
            onChange={(f) => setFecha(f)}
          />
        </div>
      </div>
      <div>
        <span>
          Visualizando horas cargas en la semana del{" "}
          {fecha?.toLocaleDateString("es-AR")}
        </span>
      </div>
      {children}
    </>
  );
}
