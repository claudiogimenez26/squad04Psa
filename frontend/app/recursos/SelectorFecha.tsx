"use client";

import { Datepicker, Label } from "flowbite-react";

export default function SelectorFecha() {
  return (
    <div className="space-y-2">
      <Label htmlFor="recursoId" value="Seleccion una fecha de una semana" />
      <Datepicker
        id="fechaCarga"
        className="w-80"
        language="es-AR"
        labelTodayButton="Hoy"
        labelClearButton="Limpiar"
        maxDate={new Date()}
      />
    </div>
  );
}
