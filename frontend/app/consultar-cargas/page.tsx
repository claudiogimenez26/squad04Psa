<<<<<<< HEAD
"use client";

import { Datepicker } from "flowbite-react";
import { useState } from "react";

export default function () {
  const [fechaInicio, setFechaInicio] = useState<Date>(new Date());
  const [fechaFin, setFechaFin] = useState<Date>(new Date());

  return (
    <div className="flex justify-center items-center gap-4">
      <span>Desde el</span>
      <Datepicker
        className="w-80"
        language="es-AR"
        labelTodayButton="Hoy"
        labelClearButton="Limpiar"
        maxDate={new Date()}
      />
      <span>hasta el</span>
      <Datepicker
        className="w-80"
        language="es-AR"
        labelTodayButton="Hoy"
        labelClearButton="Limpiar"
        maxDate={new Date()}
      />
    </div>
  );
=======
export default function () {
  return <div>Testing</div>;
>>>>>>> 0aca13b (wip: modularización inicial del formulario de carga de horas)
}
