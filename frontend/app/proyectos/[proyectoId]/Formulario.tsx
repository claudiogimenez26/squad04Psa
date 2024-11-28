"use client";

import { Proyecto } from "@/_lib/tipos";
import { Datepicker, Label, Select } from "flowbite-react";
import { useState } from "react";

export default function Formulario({ proyectos }: { proyectos: Proyecto[] }) {
  const [fechaInicio, setFechaInicio] = useState<Date>(() => {
    const fechaInicio = new Date(fechaFin);
    fechaInicio.setDate(fechaInicio.getDate() - 7);
    return fechaInicio;
  });

  const [fechaFin, setFechaFin] = useState<Date>(new Date());

  function handleCambioFechaFin(fecha: Date) {
    setFechaFin(fecha);

    if (fecha < fechaInicio) {
      const fechaIncio = new Date(fecha);
      fechaIncio.setDate(fecha.getDate() - 7);
      setFechaInicio(fechaIncio);
    }
  }

  return (
    <div className="space-y-6">
      <h1 className="text-4xl font-bold">Consultar costos por proyecto</h1>

      <div className="space-y-2">
        <Label htmlFor="proyectos" value="Seleccion un proyecto" />
        <Select id="proyectos">
          {proyectos.map((p) => (
            <option key={p.id} value={p.id}>
              {p.id} - {p.nombre}
            </option>
          ))}
        </Select>
      </div>

      <div className="space-y-4">
        <Label value="Seleccion rango de fechas" />
        <div className="flex flex-col md:flex-row justify-center items-center gap-4">
          <span>Desde el</span>
          <Datepicker
            name="fechaInicio"
            className="w-80"
            language="es-AR"
            labelTodayButton="Hoy"
            labelClearButton="Limpiar"
            maxDate={fechaFin || new Date()}
            value={fechaInicio}
            onChange={(fecha) => setFechaInicio(fecha as Date)}
          />
          <span>hasta el</span>
          <Datepicker
            name="fechaFin"
            className="w-80"
            language="es-AR"
            labelTodayButton="Hoy"
            labelClearButton="Limpiar"
            maxDate={new Date()}
            value={fechaFin}
            onChange={(fecha) => handleCambioFechaFin(fecha as Date)}
          />
        </div>
      </div>
    </div>
  );
}
